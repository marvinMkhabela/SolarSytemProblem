package za.co.discovery.assignment.DAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Models.Route;

import java.util.List;

@Service
@Transactional
public class RouteDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public RouteDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Route route) {
        Session session = sessionFactory.getCurrentSession();
        session.save(route);
    }

    public void update(Route route) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(route);
    }

    public Route retrieve(int routeid) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Route.class);
        criteria.add(Restrictions.eq("routeId", routeid));

        return (Route) criteria.uniqueResult();
    }

    public List<Route> retrieveAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Route.class);

        return (List<Route>) criteria.list();
    }

    public int delete(int routeid) {

        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM routes AS R WHERE R.routeId = :targetroute";
        Query query = session.createQuery(qry);
        query.setParameter("targetroute", routeid);

        return query.executeUpdate();
    }
}
