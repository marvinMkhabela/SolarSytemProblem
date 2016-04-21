package za.co.discovery.assignment.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.models.Planet;

import java.util.List;

@Transactional
@Repository
public class PlanetDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PlanetDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void savePlanet(Planet planet){

        Session session = sessionFactory.getCurrentSession();
        session.save(planet);
    }

    public Planet retrievePlanet(String node) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Planet.class);
        criteria.add(Restrictions.eq("node", node));

        return (Planet) criteria.uniqueResult();
    }

    public List<Planet> retrieveAll() {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Planet.class);

        return (List<Planet>) criteria.list();
    }

    public void updatePlanet(Planet planet) {

        Session session = sessionFactory.getCurrentSession();
        session.merge(planet);
    }

    public void deletePlanet(Planet planet) {

        Session session = sessionFactory.getCurrentSession();
        session.delete(planet);
    }
}
