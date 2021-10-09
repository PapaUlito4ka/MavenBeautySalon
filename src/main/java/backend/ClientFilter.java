package backend;

import models.Client;
import models.ReserveDate;
import services.ClientService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientFilter {
    private String name;
    private String surname;
    private String[] reserveDates;
    private String[] services;
    private ClientService clientService;

    public ClientFilter(String name_, String surname_, String[] reserveDates_, String[] services_)
    {
        clientService = new ClientService();
        name = name_;
        surname = surname_;
        reserveDates = reserveDates_;
        services = services_;
    }

    public ArrayList<ClientTableData> getFilteredRecords() {
        List<Client> clients = clientService.findAll();
        clients.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        ArrayList<ClientTableData> filtered = new ArrayList<>();
        for (Client client : clients) {
            if (matchesName(client.getName()) &&
                matchesSurname(client.getSurname()) &&
                matchesReserveDates(client.getReserveDates()) &&
                matchesServices(client.getReserveDates()))
            {
                ClientTableData data = new ClientTableData();
                data.fill(client.getId());
                filtered.add(data);
            }
        }
        return filtered;
    }

    public boolean matchesName(String name_) {
        return name_.contains(name);
    }
    public boolean matchesSurname(String surname_) {
        return surname_.contains(surname);
    }
    public boolean matchesReserveDates(Set<ReserveDate> reserveDates_) {
        for (String s : reserveDates) {
            boolean key = false;
            for (ReserveDate date : reserveDates_) {
                if (date.getDate().toString().equals(s)) {
                    key = true;
                    break;
                }
            }
            if (!key) return false;
        }
        return true;
    }
    public boolean matchesServices(Set<ReserveDate> reserveDates_) {
        for (String s : services) {
            boolean key = false;
            for (ReserveDate date : reserveDates_) {
                if (date.getService().getName().equals(s)) {
                    key = true;
                    break;
                }
            }
            if (!key) return false;
        }
        return true;
    }
}
