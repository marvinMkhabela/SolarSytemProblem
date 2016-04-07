package za.co.discovery.assignment.DAO;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Models.Vertex;

import java.util.List;

@Service
@Transactional
public class VertexDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public VertexDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Vertex vertex) {
        Session session = sessionFactory.getCurrentSession();
        session.save(vertex);
    }

    public void update(Vertex vertex) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(vertex);
    }

    public Vertex retrieve(String node) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Vertex.class);
        criteria.add(Restrictions.eq("node", node));

        return (Vertex) criteria.uniqueResult();
    }

    public List<Vertex> retrieveAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Vertex.class);

        return (List<Vertex>) criteria.list();
    }

    public int delete(String node) {

        Session session = sessionFactory.getCurrentSession();
        String qry = "DELETE FROM vertices AS V WHERE V.node = :targetnode";
        Query query = session.createQuery(qry);
        query.setParameter("targetnode", node);

        return query.executeUpdate();
    }

}
