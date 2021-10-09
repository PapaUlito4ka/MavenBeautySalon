package services;

import dao.ClientDao;
import models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final ClientDao dao = new ClientDao();

    public ClientService() { }

    public Client findClient(int id) {
        return dao.findById(id);
    }

    public void save(Client instance) {
        dao.save(instance);
    }

    public void delete(Client instance) {
        dao.delete(instance);
    }

    public void update(Client instance) {
        dao.update(instance);
    }

    public List<Client> findAll() {
        return dao.findAll();
    }

    public boolean contains(String clientName, String clientSurname) {
        return dao.contains(clientName, clientSurname);
    }

    public Client find(String clientName, String clientSurname) {
        return dao.find(clientName, clientSurname);
    }

    public Client find(String full) {
        return dao.find(full.split(" ")[0], full.split(" ")[1]);
    }
}
