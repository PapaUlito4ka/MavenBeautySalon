package backend;

import models.ReserveDate;
import models.Service;
import models.Worker;
import services.WorkerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorkerFilter {
    private String name;
    private String surname;
    private String spec;
    private String[] services;
    private WorkerService workerService;

    public WorkerFilter(String name_, String surname_,
                        String spec_, String[] services_)
    {
        workerService = new WorkerService();
        name = name_;
        surname = surname_;
        spec = spec_;
        services = services_;
    }

    public ArrayList<WorkerTableData> getFilteredRecords() {
        List<Worker> workers = workerService.findAll();
        workers.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        ArrayList<WorkerTableData> filtered = new ArrayList<>();
        for (Worker worker : workers) {
            if (matchesName(worker.getName()) &&
                matchesSurname(worker.getSurname()) &&
                matchesSpec(worker.getSpec()) &&
                matchesServices(worker.getServices()))
            {
                WorkerTableData data = new WorkerTableData();
                data.fill(worker.getId());
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
    public boolean matchesSpec(String spec_) {
        return spec_.contains(spec);
    }
    public boolean matchesServices(Set<Service> services_) {
        for (String s : services) {
            boolean key = false;
            for (Service service : services_) {
                if (service.getName().equals(s)) {
                    key = true;
                    break;
                }
            }
            if (!key) return false;
        }
        return true;
    }
}
