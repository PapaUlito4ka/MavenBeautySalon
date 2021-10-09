package backend;

import models.ReserveDate;
import models.Service;
import models.Worker;
import services.ServiceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServiceFilter {
    private String name;
    private int priceFrom;
    private int priceTo;
    private String[] workers;
    private ServiceService serviceService;

    public ServiceFilter(String name_,
                         String priceFrom_,
                         String priceTo_,
                         String[] workers_) throws Exception
    {
        serviceService = new ServiceService();
        name = name_;
        priceFrom = parsePriceFrom(priceFrom_);
        priceTo = parsePriceTo(priceTo_);
        workers = workers_;
    }

    public ArrayList<ServiceTableData> getFilteredRecords() {
        List<Service> services = serviceService.findAll();
        services.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        ArrayList<ServiceTableData> filtered = new ArrayList<>();
        for (Service service : services) {
            if (matchesName(service.getName()) &&
                matchesPrice(service.getPrice()) &&
                matchesWorkers(service.getWorkers()))
            {
                ServiceTableData data = new ServiceTableData();
                data.fill(service.getId());
                filtered.add(data);
            }
        }
        return filtered;
    }

    public boolean matchesName(String name_) {
        return name_.contains(name);
    }
    public boolean matchesPrice(int price_) {
        return priceFrom <= price_ && price_ <= priceTo;
    }
    public boolean matchesWorkers(Set<Worker> workers_) {
        for (String s : workers) {
            boolean key = false;
            for (Worker worker : workers_) {
                if (worker.getFull().equals(s)) {
                    key = true;
                    break;
                }
            }
            if (!key) return false;
        }
        return true;
    }

    private int parsePriceFrom(String priceFrom_) throws Exception {
        try {
            int price;
            if (priceFrom_.isEmpty()) price = serviceService.getMinPrice();
            else price = Integer.parseInt(priceFrom_);
            return price;
        }
        catch (NumberFormatException exc) {
            throw new Exception("Price from filter must be a number");
        }
    }
    private int parsePriceTo(String priceTo_) throws Exception {
        try {
            int price;
            if (priceTo_.isEmpty()) price = serviceService.getMaxPrice();
            else price = Integer.parseInt(priceTo_);
            return price;
        }
        catch (NumberFormatException exc) {
            throw new Exception("Price to filter must be a number");
        }
    }
}
