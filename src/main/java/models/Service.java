package models;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    @ManyToMany(mappedBy = "services", fetch = FetchType.EAGER)
    private Set<Worker> workers;

    public Service() {
        workers = new HashSet<>();
    }
    public Service(String name_, int price_) {
        setName(name_);
        setPrice(price_);
        workers = new HashSet<>();
    }
    public Service(String name_, int price_, Set<Worker> workers_) {
        setName(name_);
        setPrice(price_);
        setWorkers(workers_);
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name_) {
        name = name_;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price_) {
        price = price_;
    }
    public Set<Worker> getWorkers() {
        return workers;
    }
    public void setWorkers(Set<Worker> workers_) {
        workers = workers_;
    }
    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }
    public void addWorker(Worker worker) {
        workers.add(worker);
    }
}
