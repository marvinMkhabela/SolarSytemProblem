package za.co.discovery.assignment.DAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Models.Planet;

import java.util.List;

@Service
@Transactional
public class PlanetDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PlanetDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public void save(Planet planet) {
        Session session = sessionFactory.getCurrentSession();
        session.save(planet);
    }

    public void update(Planet planet) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(planet);
    }

    public Planet retrieve(String node) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Planet.class);
        criteria.add(Restrictions.eq("node", node));

        return (Planet)criteria.uniqueResult();
    }

    public List<Planet> retrieveAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Planet.class);

        return (List<Planet>) criteria.list();
    }

    public int delete(String node) {

        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM planets AS P WHERE P.node = :targetnode";
        Query query = session.createQuery(qry);
        query.setParameter("targetnode", node);

        return query.executeUpdate();
    }

}
