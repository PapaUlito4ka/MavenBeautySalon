package backend;

import models.Service;
import models.Worker;
import services.ServiceService;

import java.util.Arrays;

public class ServiceTableData {
    public String id;
    public String name;
    public String price;
    public String[] workers;

    public ServiceTableData() {
        id = "";
        name = "";
        price = "";
        workers = new String[]{};
    }
    public ServiceTableData(String name_, String price_, String[] workers_)
    {
        id = "";
        name = name_;
        price = price_;
        workers = workers_;
    }
    public ServiceTableData(String name_, String price_, String[] workers_, String id_)
    {
        id = id_;
        name = name_;
        price = price_;
        workers = workers_;
    }
    public void fill(int id_) {
        ServiceService serviceService = new ServiceService();
        Service service = serviceService.findService(id_);
        id = Integer.toString(service.getId());
        name = service.getName();
        price = Integer.toString(service.getPrice());
        workers = new String[service.getWorkers().size()];
        int i = 0;
        for (Worker worker : service.getWorkers()) {
            workers[i] = worker.getFull();
            i++;
        }
    }
    public boolean equals(ServiceTableData other) {
        return name.equals(other.name) &&
                price.equals(other.price) &&
                Arrays.equals(workers, other.workers);
    }
    public Object[] toArray() {
        return new Object[] { id, name, price, workers };
    }
}
