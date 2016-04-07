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
import za.co.discovery.assignment.Models.Vertex;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Vertex.class, DataSourceConfig.class, PersistenceConfig.class,
        VertexDAO.class},
        loader = AnnotationConfigContextLoader.class)
public class VertexdaoIT {

    @Autowired
    private SessionFactory sessionFactory;
    private VertexDAO vertexDAO;

    @Before
    public void initilizeTest() {
        vertexDAO = new VertexDAO(sessionFactory);
    }

    @Test
    public void verifyIfSavePersistsObject() {

        //Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex = new Vertex("J", "Jupiter");
        List<Vertex> expectedVertexes = Arrays.asList(vertex);

        //Exercise SUT
        vertexDAO.save(vertex);
        Criteria criteria = session.createCriteria(Vertex.class);
        List<Vertex> actualVertexes = (List<Vertex>) criteria.list();

        //Verify Behaviour
        assertThat(actualVertexes, sameBeanAs(expectedVertexes));
    }

    @Test
    public void verifyIfTheUpdateMethodUpdatesTheDB() {

        // Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex = new Vertex("M", "Mercury");
        Vertex correctVertex = new Vertex("M", "Mars");
        List<Vertex> expectedVertexes = Arrays.asList(correctVertex);
        session.save(vertex);

        // Exercise SUT
        vertexDAO.update(correctVertex);
        Criteria criteria = session.createCriteria(Vertex.class);
        criteria.add(Restrictions.eq("node", correctVertex.getNode()));
        List<Vertex> actualVertexes = (List<Vertex>) criteria.list();

        // Verify Behaviour
        assertThat(actualVertexes, sameBeanAs(expectedVertexes));
    }

    @Test
    public void verifyIfRetrieveRetrievesTheCorrectDBEntry() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex0 = new Vertex("U", "Uranus");
        Vertex expectedVertex = new Vertex("V", "Venus");
        session.save(vertex0);
        session.save(expectedVertex);

        //Exercise SUT
        Vertex retrievedVertex = vertexDAO.retrieve(expectedVertex.getNode());

        //Verify Behaviour
        assertThat(retrievedVertex, sameBeanAs(expectedVertex));
    }

    @Test
    public void verifyIfRetrieveAllRetrievesAllEntriesInTheDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex0 = new Vertex("N", "Neptune");
        Vertex vertex1 = new Vertex("S", "Saturn");
        List<Vertex> expectedVertexes = Arrays.asList(vertex0, vertex1);
        session.save(vertex0);
        session.save(vertex1);

        //Exercise SUT
        List<Vertex> actualVertexes = vertexDAO.retrieveAll();

        //Verify Behaviour
        assertThat(actualVertexes, sameBeanAs(expectedVertexes));

    }

    @Test
    public void verifyIfDeleteDeletesEntriesFromDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex0 = new Vertex("E", "Earth");
        Vertex vertex1 = new Vertex("A", "Asgard");
        List<Vertex> expectedVertexes = Arrays.asList(vertex0);
        session.save(vertex0);
        session.save(vertex1);

        //Exercise SUT
        vertexDAO.delete(vertex1.getNode());
        Criteria criteria = session.createCriteria(Vertex.class);
        List<Vertex> actualVertexes = (List<Vertex>) criteria.list();

        //Verify Behaviour
        assertThat(actualVertexes, sameBeanAs(expectedVertexes));

    }


}