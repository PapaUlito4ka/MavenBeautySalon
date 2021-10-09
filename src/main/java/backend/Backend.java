package backend;

import services.*;
import models.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Backend {
    private ClientService clientService;
    private WorkerService workerService;
    private ReserveDateService reserveDateService;
    private ServiceService serviceService;

    public Backend() {
        clientService = new ClientService();
        workerService = new WorkerService();
        reserveDateService = new ReserveDateService();
        serviceService = new ServiceService();
    }
    public ArrayList<ClientTableData> getClientsTable() {
        List<Client> data = clientService.findAll();
        data.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        ArrayList<ClientTableData> tableData = new ArrayList<>();
        for (Client ins : data) {
            ClientTableData clientTableData = new ClientTableData();
            clientTableData.fill(ins.getId());
            tableData.add(clientTableData);
        }
        return tableData;
    }
    public void saveClient(ClientTableData data) throws Exception {
        String name = data.name;
        String surname = data.surname;
        if (clientService.contains(name, surname)) {
            throw new Exception("Client already exists");
        }
        Client client = new Client(name, surname);
        clientService.save(client);
    }
    public void editClient(ClientTableData data) throws Exception {
        String name = data.name;
        String surname = data.surname;
        Client client = clientService.findClient(Integer.parseInt(data.id));
        if (clientService.find(name, surname).getId() != Integer.parseInt(data.id))
            throw new Exception("Client already exists");
        client.setName(name);
        client.setSurname(surname);
        clientService.update(client);
    }
    public void deleteClient(int[] data) {
        for (int id : data) {
            Client client = clientService.findClient(id);
            for (ReserveDate date : client.getReserveDates()) {
                reserveDateService.delete(date);
            }
            clientService.delete(client);
        }
    }

    public ArrayList<WorkerTableData> getWorkersTable() {
        List<Worker> data = workerService.findAll();
        data.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        ArrayList<WorkerTableData> tableData = new ArrayList<>();
        for (Worker ins : data) {
            WorkerTableData workerTableData = new WorkerTableData();
            workerTableData.fill(ins.getId());
            tableData.add(workerTableData);
        }
        return tableData;
    }
    public void saveWorker(WorkerTableData data) throws Exception {
        String name = data.name;
        String surname = data.surname;
        String spec = data.spec;
        if (workerService.contains(name, surname)) throw new Exception("Worker already exists");
        Set<Service> services = new HashSet<>();
        for (int i = 0; i < data.services.length; i++) {
            services.add(serviceService.find(data.services[i]));
        }
        Worker worker = new Worker(name, surname, spec);
        for (Service service : services) {
            worker.addService(service);
            service.addWorker(worker);
            serviceService.update(service);
        }
        workerService.save(worker);
    }
    public void editWorker(WorkerTableData data) throws Exception {
        String name = data.name;
        String surname = data.surname;
        String spec = data.spec;
        if (workerService.find(name, surname).getId() != Integer.parseInt(data.id))
            throw new Exception("Worker already exists");
        Set<Service> services = new HashSet<>();
        for (int i = 0; i < data.services.length; i++) {
            services.add(serviceService.find(data.services[i]));
        }
        Worker worker = workerService.findWorker(Integer.parseInt(data.id));
        worker.setName(name);
        worker.setSurname(surname);
        worker.setSpec(spec);
        for (Service service : worker.getServices()) {
            service.removeWorker(worker);
            serviceService.update(service);
        }
        worker.setServices(new HashSet<>());
        for (Service service : services) {
            worker.addService(service);
            service.addWorker(worker);
            serviceService.update(service);
        }
        workerService.update(worker);
    }
    public void deleteWorker(int[] data) {
        for (int id : data) {
            Worker worker = workerService.findWorker(id);
            for (Service service : worker.getServices()) {
                service.removeWorker(worker);
                serviceService.update(service);
            }
            workerService.delete(worker);
        }
    }

    public ArrayList<ReserveDateTableData> getReserveDatesTable() {
        List<ReserveDate> data = reserveDateService.findAll();
        data.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        ArrayList<ReserveDateTableData> tableData = new ArrayList<>();
        for (ReserveDate ins : data) {
            ReserveDateTableData reserveDateTableData = new ReserveDateTableData();
            reserveDateTableData.fill(ins.getId());
            tableData.add(reserveDateTableData);
        }
        return tableData;
    }
    public void saveReserveDate(ReserveDateTableData data) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data.date, formatter).plusDays(1);
        LocalTime timeFrom = LocalTime.parse(data.fromTime);
        LocalTime timeTo = LocalTime.parse(data.toTime);
        Client client = clientService.find(data.client);
        Worker worker = workerService.find(data.worker);
        Service service = serviceService.find(data.service);
        ReserveDate reserveDate = new ReserveDate(
                date,
                timeFrom,
                timeTo,
                client,
                worker,
                service
        );
        client.addReserveDate(reserveDate);
        clientService.update(client);
        reserveDateService.save(reserveDate);
    }
    public void editReserveDate(ReserveDateTableData data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data.date, formatter).plusDays(1);
        LocalTime timeFrom = LocalTime.parse(data.fromTime);
        LocalTime timeTo = LocalTime.parse(data.toTime);
        Client client = clientService.find(data.client);
        Worker worker = workerService.find(data.worker);
        Service service = serviceService.find(data.service);
        ReserveDate reserveDate = reserveDateService.findDate(Integer.parseInt(data.id));
        Client prevClient = reserveDate.getClient();
        prevClient.removeReserveDate(reserveDate);
        clientService.update(prevClient);
        reserveDate.setDate(date);
        reserveDate.setFrom(timeFrom);
        reserveDate.setTo(timeTo);
        reserveDate.setClient(client);
        reserveDate.setWorker(worker);
        reserveDate.setService(service);
        client.addReserveDate(reserveDate);
        clientService.update(client);
        reserveDateService.update(reserveDate);
    }
    public void deleteReserveDate(int[] data) {
        for (int id : data) {
            ReserveDate reserveDate = reserveDateService.findDate(id);
            Client client = reserveDate.getClient();
            client.removeReserveDate(reserveDate);
            clientService.update(client);
            reserveDateService.delete(reserveDate);
        }
    }

    public ArrayList<ServiceTableData> getServicesTable() {
        List<Service> data = serviceService.findAll();
        data.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        ArrayList<ServiceTableData> tableData = new ArrayList<>();
        for (Service ins : data) {
            ServiceTableData serviceTableData = new ServiceTableData();
            serviceTableData.fill(ins.getId());
            tableData.add(serviceTableData);
        }
        return tableData;
    }
    public void saveService(ServiceTableData data) throws Exception {
        String name = data.name;
        if (serviceService.contains(name)) throw new Exception("Service already exists");
        int price = Integer.parseInt(data.price);
        Set<Worker> workers = new HashSet<>();
        for (int i = 0; i < data.workers.length; i++) {
            workers.add(workerService.find(data.workers[i]));
        }
        Service service = new Service(name, price);
        for (Worker worker : workers) {
            service.addWorker(worker);
        }
        serviceService.save(service);
        for (Worker worker : workers) {
            worker.addService(service);
            workerService.update(worker);
        }
    }
    public void editService(ServiceTableData data) throws Exception {
        String name = data.name;
        if (serviceService.find(name).getId() != Integer.parseInt(data.id))
            throw new Exception("Service already exists");
        int price = Integer.parseInt(data.price);
        Set<Worker> workers = new HashSet<>();
        for (int i = 0; i < data.workers.length; i++) {
            workers.add(workerService.find(data.workers[i]));
        }
        Service service = serviceService.findService(Integer.parseInt(data.id));
        service.setName(name);
        service.setPrice(price);
        for (Worker worker : service.getWorkers()) {
            worker.removeService(service);
            workerService.update(worker);
        }
        service.setWorkers(new HashSet<>());
        for (Worker worker : workers) {
            service.addWorker(worker);
            worker.addService(service);
            workerService.update(worker);
        }
        serviceService.update(service);
    }
    public void deleteService(int[] data) {
        for (int id : data) {
            Service service = serviceService.findService(id);
            for (Worker worker : service.getWorkers()) {
                worker.removeService(service);
                workerService.update(worker);
            }
            serviceService.delete(service);
        }
    }

    public ArrayList<LocalTime> generateReserveTime() {
        ArrayList<LocalTime> times = new ArrayList<>();
        LocalTime start = LocalTime.parse("09:00:00");
        LocalTime end = LocalTime.parse("18:00:01");
        while (start.isBefore(end)) {
            times.add(start);
            start = start.plusHours(1);
        }
        return times;
    }

    public List<String> generateDates(LocalDate from, LocalDate to) {
        List<String> dates = new ArrayList<>();
        while (!from.isAfter(to)) {
            dates.add(from.toString());
            from = from.plusDays(1);
        }
        Collections.reverse(dates);
        return dates;
    }

    public List<String> generateDates(String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromLocalDate = LocalDate.parse(from, formatter);
        LocalDate toLocalDate = LocalDate.parse(to, formatter);
        return generateDates(fromLocalDate, toLocalDate);
    }

    public ArrayList<String> getUnreservedTime(String date_, String worker_, String time_) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(date_, formatter);
        Worker worker = workerService.find(worker_);
        ArrayList<LocalTime> reserveTime = generateReserveTime();
        ArrayList<ReserveDate> reserveDates = new ArrayList<>(reserveDateService.getDateWorker(date, worker));
        for (ReserveDate reserveDate : reserveDates) {
            if (!reserveDate.getFrom().toString().equals(time_)) {
                reserveTime.remove(reserveDate.getFrom());
            }
        }
        ArrayList<String> res = new ArrayList<>();
        for (LocalTime t : reserveTime) res.add(t.toString());
        return res;
    }

    public List<String> getUniqueServices() {
        List<Service> services = serviceService.findAll();
        ArrayList<String> serviceNames = new ArrayList<>();
        for (Service service : services) {
            serviceNames.add(service.getName());
        }
        return serviceNames;
    }
    public List<String> getUniqueWorkers() {
        List<Worker> workers = workerService.findAll();
        ArrayList<String> workerNames = new ArrayList<>();
        for (Worker worker : workers) {
            workerNames.add(worker.getFull());
        }
        return workerNames;
    }
    public List<String> getUniqueReserveDates() {
        List<ReserveDate> reserveDates = reserveDateService.findAll();
        ArrayList<String> reserveDates_ = new ArrayList<>();
        Set<String> uniqueDates = new HashSet<>();
        for (ReserveDate reserveDate : reserveDates) {
            if (!uniqueDates.contains(reserveDate.getDate().toString())) {
                reserveDates_.add(reserveDate.getDate().toString());
                uniqueDates.add(reserveDate.getDate().toString());
            }
        }
        return reserveDates_;
    }
    public List<String> getUniqueClients() {
        List<Client> clients = clientService.findAll();
        ArrayList<String> clientNames = new ArrayList<>();
        for (Client client : clients) {
            clientNames.add(client.getFull());
        }
        return clientNames;
    }

    public List<String> getWorkersService(String serviceName) {
        if (!serviceService.contains(serviceName)) return new ArrayList<>();
        Service service = serviceService.find(serviceName);
        List<Worker> workers = workerService.findAll();
        ArrayList<String> workerNames = new ArrayList<>();
        for (Worker worker : workers) {
            for (Service s : worker.getServices()) {
                if (s.getName().equals(serviceName)) {
                    workerNames.add(worker.getFull());
                    break;
                }
            }
        }
        return workerNames;
    }
    public String getServicePrice(String serviceName) {
        return Integer.toString(serviceService.find(serviceName).getPrice());
    }

}

