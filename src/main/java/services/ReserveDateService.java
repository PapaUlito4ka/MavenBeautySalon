package services;

import dao.ReserveDateDao;
import models.ReserveDate;
import models.Worker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReserveDateService {
    private final ReserveDateDao dao = new ReserveDateDao();

    public ReserveDateService() { }

    public ReserveDate findDate(int id) {
        return dao.findById(id);
    }

    public void save(ReserveDate instance) {
        dao.save(instance);
    }

    public void delete(ReserveDate instance) {
        dao.delete(instance);
    }

    public void update(ReserveDate instance) {
        dao.update(instance);
    }

    public List<ReserveDate> findAll() {
        return dao.findAll();
    }

    public List<ReserveDate> findLastWeek() {
        return dao.findLastWeek();
    }
    public List<ReserveDate> findLastMonth() {
        return dao.findLastMonth();
    }
    public List<ReserveDate> getDateWorker(LocalDate reserveDate, Worker worker) {
        return dao.getDateWorker(reserveDate, worker.getId());
    }
    public int getMaxPrice() {
        return dao.getMaxPrice();
    }
    public int getMinPrice() {
        return dao.getMinPrice();
    }
}
