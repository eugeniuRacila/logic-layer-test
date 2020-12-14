package project.sep3.DAO.orders;


import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import project.sep3.entities.Customer;
import project.sep3.models.Order;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    private final SessionFactory sessionFactory;

    public OrderDAOImpl() {
        Configuration con = new Configuration().configure().addAnnotatedClass(Order.class);
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
        sessionFactory = con.buildSessionFactory(reg);
    }

    private Session getNewSession() {
        return sessionFactory.openSession();
    }

    private void saveOrder(Session session, Order order) {
        Transaction tx = session.beginTransaction();
        session.save(order);
        tx.commit();
    }

    private void updateOrder(Session session, Order order){
        Transaction tx = session.beginTransaction();
        session.update(order);
        tx.commit();
    }

    @Override
    public Order getById(String id) {
        Session session = getNewSession();
        Transaction transaction = session.beginTransaction();
        String sql = "FROM Order o WHERE o.id IN (:id)";
        Query query = session.createQuery(sql);
        //query.addEntity(Order.class);
        query.setParameter("id", Integer.parseInt(id));
        List results = query.list();
        transaction.commit();
        session.close();

        if (results.isEmpty())
            return null;

        return (Order) results.get(0);
    }

    @Override
    public Order create(Order order) {
        Session session = getNewSession();
        saveOrder(session, order);
        session.close();

        return order;
    }

    @Override
    public Order take(Order order) {
        Session session = getNewSession();
        updateOrder(session, order);
        session.close();
        return order;
    }

    @Override
    public void update(Order order) {
        Session session = getNewSession();
        updateOrder(session,order);
        session.close();
    }

    @Override
    public List<Order> readAll() {
        Session session = getNewSession();
        Query query = session.createQuery("from Order");
        query.setCacheable(true);
        System.out.println(query.list().size());
        List orders = query.list();
        session.close();
        return orders;
    }
}
