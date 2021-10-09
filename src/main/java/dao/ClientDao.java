package dao;

import models.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateSessionFactoryUtil;
import java.util.List;
import java.util.TimeZone;

public class ClientDao {

    public Client findById (int id) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Client client = session.get(Client.class, id);
        session.close();
        return client;
    }

    public void save(Client instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Transaction trans = session.beginTransaction();
        session.save(instance);
        trans.commit();
        session.close();
    }

    public void update(Client instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory().withOptions()
                .openSession();
        Transaction trans = session.beginTransaction();
        session.update(instance);
        trans.commit();
        session.close();
    }

    public void delete(Client instance) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Transaction trans = session.beginTransaction();
        session.delete(instance);
        trans.commit();
        session.close();
    }

    public List<Client> findAll() {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        List<Client> clients = (List<Client>) session
                .createQuery("from Client").list();
        session.close();
        return clients;
    }

    public boolean contains(String clientName, String clientSurname) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        boolean res = !session
                .createSQLQuery("select name, surname from Client where name=:clientName and surname=:clientSurname")
                .setParameter("clientName", clientName)
                .setParameter("clientSurname", clientSurname)
                .list()
                .isEmpty();
        session.close();
        return res;
    }

    public Client find(String clientName, String clientSurname) {
        Session session = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession();
        Client client = (Client) session
                .createSQLQuery("select * from BeautySalon.Client where name=:clientName and surname=:clientSurname")
                .setParameter("clientName", clientName)
                .setParameter("clientSurname", clientSurname)
                .addEntity(Client.class)
                .list()
                .get(0);
        session.close();
        return client;
    }
}
