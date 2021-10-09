package backend;

import models.ReserveDate;
import models.Service;
import models.Worker;
import services.ReserveDateService;
import services.WorkerService;

import java.util.Arrays;

public class WorkerTableData {
    public String id;
    public String name;
    public String surname;
    public String spec;
    public String[] services;

    public WorkerTableData() {
        id = "";
        name = "";
        surname = "";
        spec = "";
        services = new String[]{};
    }
    public WorkerTableData(String name_, String surname_, String spec_,
                           String[] services_)
    {
        id = "";
        name = name_;
        surname = surname_;
        spec = spec_;
        services = services_;
    }
    public WorkerTableData(String name_, String surname_, String spec_,
                           String[] services_, String id_)
    {
        id = id_;
        name = name_;
        surname = surname_;
        spec = spec_;
        services = services_;
    }

    public void fill(int id_) {
        WorkerService workerService = new WorkerService();
        Worker worker = workerService.findWorker(id_);
        id = Integer.toString(worker.getId());
        name = worker.getName();
        surname = worker.getSurname();
        spec = worker.getSpec();
        services = new String[worker.getServices().size()];
        int i = 0;
        for (Service service : worker.getServices()) {
            services[i] = service.getName();
            i++;
        }
    }

    public boolean equals(WorkerTableData other) {
        return name.equals(other.name) &&
                surname.equals(other.surname) &&
                spec.equals(other.spec) &&
                Arrays.equals(services, other.services);
    }

    public Object[] toArray() {
        return new Object[] { id, name, surname, spec, services };
    }

}
