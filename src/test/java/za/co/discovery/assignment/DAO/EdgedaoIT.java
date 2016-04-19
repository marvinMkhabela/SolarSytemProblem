package za.co.discovery.assignment.DAO;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Configuration.DataSourceConfig;
import za.co.discovery.assignment.Configuration.PersistenceConfig;
import za.co.discovery.assignment.Models.Edge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Edge.class, EdgeDAO.class, DataSourceConfig.class,
        PersistenceConfig.class},
                loader = AnnotationConfigContextLoader.class)

public class EdgedaoIT {

    @Autowired
    private SessionFactory sessionFactory;
    private EdgeDAO edgeDAO;

    @Before
    public void initilizeTest() {
        edgeDAO = new EdgeDAO(sessionFactory);
    }

    @Test
    public void verifyIfSavePersistsObject() {

        //Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(2, "A", "B", 0.2f);
        List<Edge> expectedEdges = Collections.singletonList(edge);

        //Exercise SUT
        edgeDAO.save(edge);
        Criteria criteria = session.createCriteria(Edge.class);
        List<Edge> actualEdges = (List<Edge>) criteria.list();

        //Verify Behaviour
        assertThat(actualEdges, sameBeanAs(expectedEdges));
    }

    @Test
    public void verifyIfTheUpdateMethodUpdatesTheDB() {

        // Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(3, "A", "B", 0.3f);
        Edge correctEdge = new Edge(3, "C", "D", 3f);
        List<Edge> expectedEdges = Collections.singletonList(correctEdge);
        session.save(edge);

        // Exercise SUT
        edgeDAO.update(correctEdge);
        Criteria criteria = session.createCriteria(Edge.class);
        criteria.add(Restrictions.eq("edgeId", correctEdge.getEdgeId()));
        List<Edge> actualEdges = (List<Edge>) criteria.list();

        // Verify Behaviour
        assertThat(actualEdges, sameBeanAs(expectedEdges));
    }

    @Test
    public void verifyIfRetrieveRetrievesTheCorrectDBEntry() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Edge edge = new Edge(5, "A", "B", 0.5f);
        Edge expectedEdge = new Edge(7, "C", "D", 0.7f);
        session.save(edge);
        session.save(expectedEdge);

        //Exercise SUT
        Edge retrievedEdge = edgeDAO.retrieve(expectedEdge.getEdgeId());

        //Verify Behaviour
        assertThat(retrievedEdge, sameBeanAs(expectedEdge));
    }

    @Test
    public void verifyIfRetrieveAllRetrievesAllEntriesInTheDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Edge edge0 = new Edge(11, "A", "B", 0.11f);
        Edge edge1 = new Edge(13, "C", "D", 0.13f);
        List<Edge> expectedEdges = Arrays.asList(edge0, edge1);
        session.save(edge0);
        session.save(edge1);

        //Exercise SUT
        List<Edge> actualEdges = edgeDAO.retrieveAll();

        //Verify Behaviour
        assertThat(actualEdges, sameBeanAs(expectedEdges));

    }

    @Test
    public void verifyIfDeleteDeletesEntriesFromDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Edge edge0 = new Edge(17, "A", "B", 0.17f);
        Edge edge1 = new Edge(19, "C", "D", 0.19f);
        List<Edge> expectedEdges = Collections.singletonList(edge0);
        session.save(edge0);
        session.save(edge1);

        //Exercise SUT
        edgeDAO.delete(edge1.getEdgeId());
        Criteria criteria = session.createCriteria(Edge.class);
        List<Edge> actualEdges = (List<Edge>) criteria.list();

        //Verify Behaviour
        assertThat(actualEdges, sameBeanAs(expectedEdges));

    }
}