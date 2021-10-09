package reports;

import reports.beans.WorkerData;
import models.ReserveDate;
import services.ReserveDateService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Reporting {
    private static JasperReport jasperDesign;
    private static JasperPrint jasperPrint;
    private static final String fileName = "src/main/resources/BeautySalon.jrxml";
    private static final String xmlOutFile = "src/main/resources/Report.pdf";
    private static final String htmlOutFile = "src/main/resources/Report.html";
    private ReserveDateService reserveDateService;
    private List<ReserveDate> reserveDates;
    private Map<String, Object> parameter;
    private LocalDate fromLocalDate;

    public Reporting(boolean getLastMonth) {
        reserveDateService = new ReserveDateService();
        parameter = new HashMap<String, Object>();
        if (getLastMonth) {
            reserveDates = reserveDateService.findLastMonth();
            parameter.put("fromLocalDate", LocalDate.now().minusMonths(1));
        }
        else {
            reserveDates = reserveDateService.findLastWeek();
            parameter.put("fromLocalDate", LocalDate.now().minusDays(7));
        }
        parameter.put("toLocalDate", LocalDate.now());
    }

    public void makePdf() throws JRException, FileNotFoundException {
        List<WorkerData> workerProfitDataList = getData();
        List<WorkerData> workerClientsDataList = getData();
        fillWorkerProfitData(getWorkerProfitData(workerProfitDataList));
        fillWorkerClientsData(getWorkerClientsData(workerClientsDataList));
        generateDocument();
    }

    private List<WorkerData> getData() {
        List<WorkerData> workerDataList = new ArrayList<>();
        Map<String, WorkerData> workerMap = new HashMap<>();
        int totalEarned = 0;
        int totalClients = 0;
        for (ReserveDate date : reserveDates) {
            String nameSurname = date.getWorker().getName() + " " + date.getWorker().getSurname();
            if (workerMap.containsKey(nameSurname)) {
                workerMap.get(nameSurname).setClientCnt(workerMap.get(nameSurname).getClientCnt() + 1);
                workerMap.get(nameSurname).setTotalEarnings(
                        workerMap.get(nameSurname).getTotalEarnings() + date.getService().getPrice()
                );
            }
            else {
                WorkerData workerData = new WorkerData(
                        date.getWorker().getName(),
                        date.getWorker().getSurname(),
                        date.getWorker().getSpec(),
                        date.getService().getPrice(),
                        1
                );
                workerMap.put(nameSurname, workerData);
            }
        }
        for (Map.Entry<String, WorkerData> entry : workerMap.entrySet()) {
            workerDataList.add(entry.getValue());
            totalEarned += entry.getValue().getTotalEarnings();
            totalClients += entry.getValue().getClientCnt();
        }
        parameter.put("totalEarned", totalEarned);
        parameter.put("totalClients", totalClients);
        return workerDataList;
    }

    private List<WorkerData> getWorkerProfitData(List<WorkerData> workerDataList) {
        workerDataList.sort((o1, o2) -> o2.getTotalEarnings() - o1.getTotalEarnings());
        return workerDataList;
    }

    private List<WorkerData> getWorkerClientsData(List<WorkerData> workerDataList) {
        workerDataList.sort((o1, o2) -> o2.getClientCnt() - o1.getClientCnt());
        return workerDataList;
    }

    private void fillWorkerProfitData(List<WorkerData> workerDataList) {
        JRBeanCollectionDataSource workerCollectionDataSource =
                new JRBeanCollectionDataSource(workerDataList);
        parameter.put("workersProfitDataSource", workerCollectionDataSource);
    }

    private void fillWorkerClientsData(List<WorkerData> workerDataList) {
        JRBeanCollectionDataSource workerCollectionDataSource =
                new JRBeanCollectionDataSource(workerDataList);

        parameter.put("workersClientsDataSource", workerCollectionDataSource);
    }

    private void generateDocument() throws RuntimeException {
        Lock lock = new ReentrantLock();
        Runnable fillReport = () -> {
            try {
                lock.lock();
                jasperDesign = JasperCompileManager.compileReport(fileName);
                jasperPrint = JasperFillManager.fillReport(
                        jasperDesign,
                        parameter,
                        new JREmptyDataSource()
                );
                lock.unlock();
            }
            catch (JRException e) {
                lock.unlock();
                throw new RuntimeException("Something bad happened :(", e);
            }
        };
        Runnable generateXml = () -> {
            try {
                lock.lock();
                File xmlFile = new File(xmlOutFile);
                OutputStream xmlOutputStream = new FileOutputStream(xmlFile);
                JasperExportManager.exportReportToPdfStream(jasperPrint, xmlOutputStream);
                lock.unlock();
            }
            catch (FileNotFoundException | JRException e) {
                lock.unlock();
                throw new RuntimeException("Xml generation error", e);
            }
        };
        Runnable generateHtml = () -> {
            try {
                lock.lock();
                JasperExportManager.exportReportToHtmlFile(jasperPrint, htmlOutFile);
                lock.unlock();
            } catch (JRException e) {
                lock.unlock();
                throw new RuntimeException("Html generation error", e);
            }
        };
        Thread fillTread = new Thread(fillReport);
        Thread xmlThread = new Thread(generateXml);
        Thread htmlThread = new Thread(generateHtml);

        fillTread.start();
        xmlThread.start();
        htmlThread.start();
    }

}
