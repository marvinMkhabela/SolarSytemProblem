package za.co.discovery.assignment.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.models.Traffic;

import java.io.Serializable;
import java.util.List;

@Transactional
@Repository
public class TrafficDAO implements Serializable {

    private SessionFactory sessionFactory;

    @Autowired
    public TrafficDAO(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void saveTraffic(Traffic traffic){

        Session session = sessionFactory.getCurrentSession();
        session.save(traffic);
    }

    public Traffic retrieveTraffic(int trafficId) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.eq("trafficId", trafficId));

        return (Traffic) criteria.uniqueResult();
    }

    public List<Traffic> retrieveAll() {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Traffic.class);

        return (List<Traffic>) criteria.list();
    }

    public void updateTraffic(Traffic traffic) {

        Session session = sessionFactory.getCurrentSession();
        session.merge(traffic);
    }

    public void deleteTraffic(Traffic traffic) {

        Session session = sessionFactory.getCurrentSession();
        session.delete(traffic);
    }

}
