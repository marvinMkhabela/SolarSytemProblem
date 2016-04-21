package za.co.discovery.assignment.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.models.Route;

import java.util.List;

@Transactional
@Repository
public class RouteDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public RouteDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void saveRoute(Route route){

        Session session = sessionFactory.getCurrentSession();
        session.save(route);
    }

    public Route retrieveRoute(int routeId) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Route.class);
        criteria.add(Restrictions.eq("routeId", routeId));

        return (Route) criteria.uniqueResult();
    }

    public List<Route> retrieveAll() {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Route.class);

        return (List<Route>) criteria.list();
    }

    public void updateRoute(Route route) {

        Session session = sessionFactory.getCurrentSession();
        session.merge(route);
    }

    public void deleteRoute(Route route) {

        Session session = sessionFactory.getCurrentSession();
        session.delete(route);
    }
}
