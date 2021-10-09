package backend;

import models.ReserveDate;
import services.ReserveDateService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReserveDateFilter {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private String client;
    private String worker;
    private int priceFrom;
    private int priceTo;
    private ReserveDateService reserveDateService;

    public ReserveDateFilter(String dateFrom_, String dateTo_,
                             String timeFrom_, String timeTo_,
                             String client_, String worker_,
                             String priceFrom_, String priceTo_) throws Exception
    {
        reserveDateService = new ReserveDateService();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateFrom = LocalDate.parse(dateFrom_, formatter);
        dateTo = LocalDate.parse(dateTo_, formatter);
        timeFrom = LocalTime.parse(timeFrom_);
        timeTo = LocalTime.parse(timeTo_);
        client = client_;
        worker = worker_;
        priceFrom = parsePriceFrom(priceFrom_);
        priceTo = parsePriceTo(priceTo_);
    }

    public ArrayList<ReserveDateTableData> getFilteredRecords() {
        List<ReserveDate> reserveDates = reserveDateService.findAll();
        reserveDates.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        ArrayList<ReserveDateTableData> filtered = new ArrayList<>();
        for (ReserveDate date : reserveDates) {
            if (matchesDate(date.getDate()) &&
                matchesTime(date.getFrom()) &&
                matchesClient(date.getClient().getFull()) &&
                matchesWorker(date.getWorker().getFull()) &&
                matchesPrice(date.getService().getPrice()))
            {
                ReserveDateTableData data = new ReserveDateTableData();
                data.fill(date.getId());
                filtered.add(data);
            }
        }
        return filtered;
    }

    public boolean matchesDate(LocalDate date_) {
        return date_.compareTo(dateFrom) * dateTo.compareTo(date_) >= 0;
    }
    public boolean matchesTime(LocalTime time_) {
        return time_.compareTo(timeFrom) * timeTo.compareTo(time_) >= 0;
    }
    public boolean matchesClient(String client_) {
        return client_.contains(client);
    }
    public boolean matchesWorker(String worker_) {
        return worker_.contains(worker);
    }
    public boolean matchesPrice(int price_) {
        return priceFrom <= price_ && price_ <= priceTo;
    }
    private int parsePriceFrom(String priceFrom_) throws Exception {
        try {
            int price;
            if (priceFrom_.isEmpty()) price = reserveDateService.getMinPrice();
            else price = Integer.parseInt(priceFrom_);
            return price;
        }
        catch (NumberFormatException exc) {
            throw new Exception("Price from filter must be a number");
        }
    }
    private int parsePriceTo(String priceTo_) throws Exception {
        try {
            int price;
            if (priceTo_.isEmpty()) price = reserveDateService.getMaxPrice();
            else price = Integer.parseInt(priceTo_);
            return price;
        }
        catch (NumberFormatException exc) {
            throw new Exception("Price to filter must be a number");
        }
    }
}
