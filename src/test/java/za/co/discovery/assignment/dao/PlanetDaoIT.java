package za.co.discovery.assignment.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.configurations.DataSourceConfig;
import za.co.discovery.assignment.configurations.PersistenceConfig;
import za.co.discovery.assignment.models.Planet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PlanetDAO.class, DataSourceConfig.class, PersistenceConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class PlanetDaoIT {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private PlanetDAO planetDAO;

    @Test
    public void verifyThatSaveSavesEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet planet = new Planet("A", "Earth", 1);

        // Exercise Fixture
        planetDAO.savePlanet(planet);
        Criteria criteria = session.createCriteria(Planet.class);
        criteria.add(Restrictions.eq("node", planet.getNode()));
        Planet actualPlanet = (Planet) criteria.uniqueResult();

        // Verify Behaviour
        assertThat(actualPlanet, sameBeanAs(planet));
    }

    @Test
    public void verifyThatRetrieveRetrievesCorrectEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        session.save(firstPlanet);
        session.save(secondPlanet);

        // Exercise SUT
        Planet actualPlanet = planetDAO.retrievePlanet(firstPlanet.getNode());

        // Verify Behaviour
        assertThat(actualPlanet, sameBeanAs(firstPlanet));

    }

    @Test
    public void verifyThatRetrieveAllRetrievesAllEntries() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        List<Planet> planets = Arrays.asList(new Planet("A", "Earth", 1), new Planet("B", "Moon", 2));
        session.save(planets.get(0));
        session.save(planets.get(1));

        // Exercise SUT
        List<Planet> actualPlanets = planetDAO.retrieveAll();

        // Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(planets));
    }

    @Test
    public void verifyThatUpdateChangesDBEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet originalPlanet = new Planet("A", "Big Blue", 1);
        Planet expectedPlanet = new Planet("A", "Earth", 1);
        session.save(originalPlanet);

        // Exercise SUT
        planetDAO.updatePlanet(expectedPlanet);
        Criteria criteria = session.createCriteria(Planet.class);
        criteria.add(Restrictions.eq("node", originalPlanet.getNode()));
        Planet actualPlanet = (Planet) criteria.uniqueResult();

        // Verify Behaviour
        assertThat(actualPlanet, sameBeanAs(expectedPlanet));
    }

    @Test
    public void verifyThatDeleteDeletesDBEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        List<Planet> expectedPlanet = Collections.singletonList(firstPlanet);
        session.save(firstPlanet);
        session.save(secondPlanet);

        // Exercise SUT
        planetDAO.deletePlanet(secondPlanet);
        Criteria criteria = session.createCriteria(Planet.class);
        List<Planet> actualPlanets = (List<Planet>) criteria.list();

        // Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(expectedPlanet));
    }

}