package backend;

import models.Client;
import models.ReserveDate;
import services.ClientService;

import java.util.Arrays;

public class ClientTableData {
    public String id;
    public String name;
    public String surname;
    public String[] reserveDates;
    public String[] services;

    public ClientTableData() {
        id = "";
        name = "";
        surname = "";
        reserveDates = new String[]{};
        services = new String[]{};
    }

    public ClientTableData(String name_, String surname_,
                           String[] reserveDates_, String[] services_)
    {
        id = "";
        name = name_;
        surname = surname_;
        reserveDates = reserveDates_;
        services = services_;
    }
    public ClientTableData(String name_, String surname_,
                           String[] reserveDates_, String[] services_, String id_)
    {
        id = id_;
        name = name_;
        surname = surname_;
        reserveDates = reserveDates_;
        services = services_;
    }
    public void fill(int id_) {
        ClientService clientService = new ClientService();
        Client ins = clientService.findClient(id_);
        id = Integer.toString(ins.getId());
        name = ins.getName();
        surname = ins.getSurname();
        reserveDates = new String[ins.getReserveDates().size()];
        services = new String[ins.getReserveDates().size()];
        int i = 0;
        for (ReserveDate date : ins.getReserveDates()) {
            reserveDates[i] = date.getDate().toString();
            services[i] = date.getService().getName();
            i++;
        }
    }

    public boolean equals(ClientTableData other) {
        return name.equals(other.name) &&
                surname.equals(other.surname) &&
                Arrays.equals(reserveDates, other.reserveDates) &&
                Arrays.equals(services, other.services);
    }

    public Object[] toArray() {
        return new Object[] { id, name, surname, reserveDates, services };
    }
}
