package dao;

import models.ReserveDate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateSessionFactoryUtil;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;

public class ReserveDateDao {

    public ReserveDate findById (int id) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        ReserveDate date = session.get(ReserveDate.class, id);
        session.close();
        return date;
    }

    public void save(ReserveDate instance) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().withOptions()
                .openSession();
        Transaction trans = session.beginTransaction();
        session.save(instance);
        trans.commit();
        session.close();
    }

    public void update(ReserveDate instance) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().withOptions()
                .openSession();
        Transaction trans = session.beginTransaction();
        session.update(instance);
        trans.commit();
        session.close();
    }

    public void delete(ReserveDate instance) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().withOptions()
                .openSession();
        Transaction trans = session.beginTransaction();
        session.delete(instance);
        trans.commit();
        session.close();
    }

    public List<ReserveDate> findAll() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<ReserveDate> dates = (List<ReserveDate>) session
                .createQuery("from ReserveDate")
                .list();
        session.close();
        return dates;
    }

    public List<ReserveDate> findLastWeek() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<ReserveDate> dates = (List<ReserveDate>) session
                .createSQLQuery("select * from ReserveDate\n" +
                        "where date between date_sub(now(),INTERVAL 1 WEEK) and now();")
                .addEntity(ReserveDate.class)
                .list();
        session.close();
        return dates;
    }

    public List<ReserveDate> findLastMonth() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<ReserveDate> dates = (List<ReserveDate>) session
                .createSQLQuery("SELECT * FROM BeautySalon.ReserveDate\n" +
                        "WHERE date\n" +
                        "BETWEEN (CURRENT_DATE() - INTERVAL 1 MONTH) AND CURRENT_DATE();")
                .addEntity(ReserveDate.class)
                .list();
        session.close();
        return dates;
    }

    public List<ReserveDate> getDateWorker(LocalDate reserveDate, int workerId) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<ReserveDate> dates = (List<ReserveDate>) session
                .createSQLQuery("select * from BeautySalon.ReserveDate " +
                        "where DATE(date)=:reserveDate and workerId=:workerId " +
                        "order by fromTime")
                .setParameter("reserveDate", reserveDate.toString())
                .setParameter("workerId", workerId)
                .addEntity(ReserveDate.class)
                .list();
        session.close();
        return dates;
    }

    public int getMinPrice() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        int res = ((Integer) session
                .createSQLQuery("select min(Service.price) " +
                        "from ReserveDate " +
                        "join Service on Service.id = ReserveDate.serviceId;")
                .uniqueResult())
                .intValue();
        session.close();
        return res;
    }
    public int getMaxPrice() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        int res = ((Integer) session
                .createSQLQuery("select max(Service.price) " +
                        "from ReserveDate " +
                        "join Service on Service.id = ReserveDate.serviceId;")
                .uniqueResult())
                .intValue();
        session.close();
        return res;
    }
}
