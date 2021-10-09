package dao;

import models.Service;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateSessionFactoryUtil;

import java.math.BigInteger;
import java.util.List;

public class ServiceDao {

    public Service findById (int id) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Service service = session
                .get(Service.class, id);
        session.close();
        return service;
    }

    public void save(Service instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.save(instance);
        trans.commit();
        session.close();
    }

    public void update(Service instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.update(instance);
        trans.commit();
        session.close();
    }

    public void delete(Service instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        session.delete(instance);
        trans.commit();
        session.close();
    }

    public List<Service> findAll() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<Service> services = (List<Service>) session
                .createQuery("from Service").list();
        session.close();
        return services;
    }

    public boolean contains(String serviceName) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        boolean res = !session
                .createSQLQuery("select name from BeautySalon.Service where name=:serviceName")
                .setParameter("serviceName", serviceName)
                .list()
                .isEmpty();
        session.close();
        return res;
    }

    public Service find(String serviceName) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Service service = (Service) session
                .createSQLQuery("select * from BeautySalon.Service where name=:serviceName")
                .setParameter("serviceName", serviceName)
                .addEntity(Service.class)
                .list()
                .get(0);
        session.close();
        return service;
    }

    public int getMaxPrice() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        int res = ((Integer) session
                .createSQLQuery("select max(price) from Service")
                .uniqueResult())
                .intValue();
        session.close();
        return res;
    }
    public int getMinPrice() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        int res = ((Integer) session
                .createSQLQuery("select min(price) from Service")
                .uniqueResult())
                .intValue();
        session.close();
        return res;
    }
}
