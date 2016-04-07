package za.co.discovery.assignment.DAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Models.Edge;

import java.util.List;

@Service
@Transactional
public class EdgeDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public EdgeDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Edge edge) {
        Session session = sessionFactory.getCurrentSession();
        session.save(edge);
    }

    public void update(Edge edge) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(edge);
    }

    public Edge retrieve(int edgeId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);
        criteria.add(Restrictions.eq("edgeId", edgeId));

        return (Edge) criteria.uniqueResult();
    }

    public List<Edge> retrieveAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Edge.class);

        return (List<Edge>) criteria.list();
    }

    public int delete(int edgeId) {

        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM edges AS E WHERE E.edgeId = :targetroute";
        Query query = session.createQuery(qry);
        query.setParameter("targetroute", edgeId);

        return query.executeUpdate();
    }
}
