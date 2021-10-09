package services;

import dao.WorkerDao;
import models.Worker;

import java.util.ArrayList;
import java.util.List;

public class WorkerService {
    private final WorkerDao dao = new WorkerDao();

    public WorkerService() { }

    public Worker findWorker(int id) {
        return dao.findById(id);
    }

    public void save(Worker instance) {
        dao.save(instance);
    }

    public void delete(Worker instance) {
        dao.delete(instance);
    }

    public void update(Worker instance) {
        dao.update(instance);
    }

    public List<Worker> findAll() {
        return dao.findAll();
    }

    public boolean contains(String workerName, String workerSurname) {
        return dao.contains(workerName, workerSurname);
    }
    public Worker find(String workerName, String workerSurname) {
        return dao.find(workerName, workerSurname);
    }

    public Worker find(String full) {
        return dao.find(full.split(" ")[0], full.split(" ")[1]);
    }
}
