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
import za.co.discovery.assignment.Models.Planet;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Planet.class, DataSourceConfig.class, PersistenceConfig.class,
        PlanetDAO.class},
        loader = AnnotationConfigContextLoader.class)
public class PlanetdaoIT {

    @Autowired
    private SessionFactory sessionFactory;
    private PlanetDAO planetDAO;

    @Before
    public void initilizeTest() {
        planetDAO = new PlanetDAO(sessionFactory);
    }

    @Test
    public void verifyIfSavePersistsObject() {

        //Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet planet = new Planet("J", "Jupiter");
        List<Planet> expectedPlanets = Arrays.asList(planet);

        //Exercise SUT
        planetDAO.save(planet);
        Criteria criteria = session.createCriteria(Planet.class);
        List<Planet> actualPlanets = (List<Planet>) criteria.list();

        //Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(expectedPlanets));
    }

    @Test
    public void verifyIfTheUpdateMethodUpdatesTheDB() {

        // Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Planet planet = new Planet("M", "Mercury");
        Planet correctPlanet = new Planet("M", "Mars");
        List<Planet> expectedPlanets = Arrays.asList(correctPlanet);
        session.save(planet);

        // Exercise SUT
        planetDAO.update(correctPlanet);
        Criteria criteria = session.createCriteria(Planet.class);
        criteria.add(Restrictions.eq("node", correctPlanet.getNode()));
        List<Planet> actualPlanets = (List<Planet>) criteria.list();

        // Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(expectedPlanets));
    }

    @Test
    public void verifyIfRetrieveRetrievesTheCorrectDBEntry() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Planet planet0 = new Planet("U", "Uranus");
        Planet expectedPlanet = new Planet("V", "Venus");
        session.save(planet0);
        session.save(expectedPlanet);

        //Exercise SUT
        Planet retrievedPlanet = planetDAO.retrieve(expectedPlanet.getNode());

        //Verify Behaviour
        assertThat(retrievedPlanet, sameBeanAs(expectedPlanet));
    }

    @Test
    public void verifyIfRetrieveAllRetrievesAllEntriesInTheDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Planet planet0 = new Planet("N", "Neptune");
        Planet planet1 = new Planet("S", "Saturn");
        List<Planet> expectedPlanets = Arrays.asList(planet0, planet1);
        session.save(planet0);
        session.save(planet1);

        //Exercise SUT
        List<Planet> actualPlanets = planetDAO.retrieveAll();

        //Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(expectedPlanets));

    }

    @Test
    public void verifyIfDeleteDeletesEntriesFromDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Planet planet0 = new Planet("E", "Earth");
        Planet planet1 = new Planet("A", "Asgard");
        List<Planet> expectedPlanets = Arrays.asList(planet0);
        session.save(planet0);
        session.save(planet1);

        //Exercise SUT
        planetDAO.delete(planet1.getNode());
        Criteria criteria = session.createCriteria(Planet.class);
        List<Planet> actualPlanets = (List<Planet>) criteria.list();

        //Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(expectedPlanets));

    }


}