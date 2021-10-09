package backend;

import models.Client;
import models.ReserveDate;
import services.ClientService;
import services.ReserveDateService;

import java.util.Arrays;

public class ReserveDateTableData {
    public String id;
    public String date;
    public String fromTime;
    public String toTime;
    public String client;
    public String worker;
    public String service;
    public String price;

    public ReserveDateTableData() {
        id = "";
        date = "";
        fromTime = "";
        toTime = "";
        client = "";
        worker = "";
        service = "";
        price = "";
    }
    public ReserveDateTableData(String date_, String fromTime_, String toTime_,
                                String client_, String worker_, String service_, String price_)
    {
        id = "";
        date = date_;
        fromTime = fromTime_;
        toTime = toTime_;
        client = client_;
        worker = worker_;
        service = service_;
        price = price_;
    }
    public ReserveDateTableData(String date_, String fromTime_, String toTime_,
                                String client_, String worker_, String service_,
                                String price_, String id_)
    {
        id = id_;
        date = date_;
        fromTime = fromTime_;
        toTime = toTime_;
        client = client_;
        worker = worker_;
        service = service_;
        price = price_;
    }

    public void fill(int id_) {
        ReserveDateService reserveDateService = new ReserveDateService();
        ReserveDate reserveDate = reserveDateService.findDate(id_);
        id = Integer.toString(reserveDate.getId());
        date = reserveDate.getDate().toString();
        fromTime = reserveDate.getFrom().toString();
        toTime = reserveDate.getTo().toString();
        client = reserveDate.getClient().getFull();
        worker = reserveDate.getWorker().getFull();
        service = reserveDate.getService().getName();
        price = Integer.toString(reserveDate.getService().getPrice());
    }

    public boolean equals(ReserveDateTableData other) {
        return date.equals(other.date) &&
                fromTime.equals(other.fromTime) &&
                toTime.equals(other.toTime) &&
                client.equals(other.client) &&
                worker.equals(other.worker) &&
                service.equals(other.worker) &&
                price.equals(other.price);
    }

    public Object[] toArray() {
        return new Object[] { id, date, fromTime, toTime, client, worker, service, price };
    }

}
