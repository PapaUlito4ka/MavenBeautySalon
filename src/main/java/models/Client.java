package models;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "Client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<ReserveDate> reserveDates;

    public Client() {
        reserveDates = new HashSet<>();
    }

    public Client(String name_, String surname_) {
        setName(name_);
        setSurname(surname_);
        reserveDates = new HashSet<>();
    }

    public Client(String name_, String surname_, Set<ReserveDate> reserveDates_) {
        setName(name_);
        setSurname(surname_);
        setReserveDates(reserveDates_);
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
    public Set<ReserveDate> getReserveDates() {
        return reserveDates;
    }
    public void setReserveDates(Set<ReserveDate> reserveDates_) {
        reserveDates = reserveDates_;
    }
    public void removeReserveDate(ReserveDate date_) {
        reserveDates.remove(date_);
    }
    public void addReserveDate(ReserveDate date_) {
        reserveDates.add(date_);
    }
}
