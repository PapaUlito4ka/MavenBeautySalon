package models;

import com.sun.istack.NotNull;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "ReserveDate")
public class ReserveDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    private LocalDate date;
    @Basic
    private LocalTime fromTime;
    @Basic
    private LocalTime toTime;
    @ManyToOne
    @JoinColumn(name = "clientId", nullable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "workerId", nullable = false)
    private Worker worker;
    @ManyToOne
    @JoinColumn(name = "serviceId", nullable = false)
    private Service service;

    public ReserveDate() {}
    public ReserveDate(LocalDate date_, LocalTime from_, LocalTime to_,
                       Client client_, Worker worker_, Service service_) {
        setDate(date_);
        setFrom(from_);
        setTo(to_);
        setClient(client_);
        setWorker(worker_);
        setService(service_);
    }

    public int getId() {
        return id;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date_) {
        date = date_;
    }
    public LocalTime getFrom() {
        return fromTime;
    }
    public void setFrom(LocalTime from_) {
        fromTime = from_;
    }
    public LocalTime getTo() {
        return toTime;
    }
    public void setTo(LocalTime to_) {
        toTime = to_;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client_) {
        client = client_;
    }
    public Worker getWorker() {
        return worker;
    }
    public void setWorker(Worker worker_) {
        worker = worker_;
    }
    public Service getService() {
        return service;
    }
    public void setService(Service service_) {
        service = service_;
    }
}
