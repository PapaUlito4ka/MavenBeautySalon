package models;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Worker")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String spec;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "WorkerToService",
            joinColumns = { @JoinColumn(name = "workerId") },
            inverseJoinColumns = { @JoinColumn(name = "serviceId") }
    )
    private Set<Service> services;

    public Worker() {
        services = new HashSet<>();
    }
    public Worker(String name_, String surname_, String spec_) {
        setName(name_);
        setSurname(surname_);
        setSpec(spec_);
        services = new HashSet<>();
    }
    public Worker(String name_, String surname_, String spec_, Set<Service> services_) {
        setName(name_);
        setSurname(surname_);
        setSpec(spec_);
        setServices(services_);
    }

    public int getId() {
        return id;
    }
    public String getFull() {
        return getName() + " " + getSurname();
    }
    public String getName() {
        return name;
    }
    public void setName(String name_) {
        name = name_;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname_) {
        surname = surname_;
    }
    public String getSpec() {
        return spec;
    }
    public void setSpec(String spec_) {
        spec = spec_;
    }
    public Set<Service> getServices() {
        return services;
    }
    public void setServices(Set<Service> services_) {
        services = services_;
    }
    public void removeService(Service service) {
        services.remove(service);
    }
    public void addService(Service service) {
        services.add(service);
    }
}
