package za.co.discovery.assignment.DAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Models.Traffic;

import java.util.List;

@Transactional
public class TrafficDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public TrafficDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Traffic traffic) {
        Session session = sessionFactory.getCurrentSession();
        session.save(traffic);
    }

    public void update(Traffic traffic) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(traffic);
    }

    public Traffic retrieve(int routeid) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.eq("routeId", routeid));

        return (Traffic) criteria.uniqueResult();
    }

    public List<Traffic> retrieveAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);

        return (List<Traffic>) criteria.list();
    }

    public int delete(int routeid) {

        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM traffic AS T WHERE T.routeId = :targetTraffic";
        Query query = session.createQuery(qry);
        query.setParameter("targetTraffic", routeid);

        return query.executeUpdate();
    }
}
