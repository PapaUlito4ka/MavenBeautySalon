package dao;

import models.Client;
import models.Worker;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateSessionFactoryUtil;
import java.util.List;

public class WorkerDao {

    public Worker findById (int id) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Worker worker = session
                .get(Worker.class, id);
        session.close();
        return worker;
    }

    public void save(Worker instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.save(instance);
        trans.commit();
        session.close();
    }

    public void update(Worker instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.update(instance);
        trans.commit();
        session.close();
    }

    public void delete(Worker instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.delete(instance);
        trans.commit();
        session.close();
    }

    public List<Worker> findAll() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<Worker> workers = (List<Worker>) session
                .createQuery("from Worker")
                .list();
        session.close();
        return workers;
    }

    public boolean contains(String workerName, String workerSurname) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        boolean res = !session
                .createSQLQuery("select name, surname from BeautySalon.Worker where name=:workerName and surname=:workerSurname")
                .setParameter("workerName", workerName)
                .setParameter("workerSurname", workerSurname)
                .list()
                .isEmpty();
        session.close();
        return res;
    }

    public Worker find(String workerName, String workerSurname) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Worker worker = (Worker) session
                .createSQLQuery("select * from BeautySalon.Worker where name=:workerName and surname=:workerSurname")
                .setParameter("workerName", workerName)
                .setParameter("workerSurname", workerSurname)
                .addEntity(Worker.class)
                .list()
                .get(0);
        session.close();
        return worker;
    }
}
