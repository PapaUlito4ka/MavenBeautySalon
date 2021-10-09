package services;

import dao.ServiceDao;
import models.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceService {
    private final ServiceDao dao = new ServiceDao();

    public ServiceService() { }

    public Service findService(int id) {
        return dao.findById(id);
    }

    public void save(Service instance) {
        dao.save(instance);
    }

    public void delete(Service instance) {
        dao.delete(instance);
    }

    public void update(Service instance) {
        dao.update(instance);
    }

    public List<Service> findAll() {
        return dao.findAll();
    }

    public boolean contains(String serviceName) {
        return dao.contains(serviceName);
    }
    public Service find(String serviceName) {
        return dao.find(serviceName);
    }
    public int getMaxPrice() {
        return dao.getMaxPrice();
    }
    public int getMinPrice() {
        return dao.getMinPrice();
    }
}
