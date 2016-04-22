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
import za.co.discovery.assignment.models.Traffic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TrafficDAO.class, PersistenceConfig.class, DataSourceConfig.class,
        Planet.class, PlanetDAO.class},
    loader = AnnotationConfigContextLoader.class)
public class TrafficDaoIT {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TrafficDAO trafficDAO;
    @Autowired
    private PlanetDAO planetDAO;

    @Test
    public void verifyThatSaveSavesEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet origin = new Planet("A", "Earth", 1);
        Planet destination = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(origin);
        planetDAO.savePlanet(destination);
        Traffic traffic = new Traffic(1, origin, destination, 3);

        // Exercise Fixture
        trafficDAO.saveTraffic(traffic);
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.eq("trafficId", traffic.getTrafficId()));
        Traffic actualTraffic = (Traffic) criteria.uniqueResult();

        // Verify Behaviour
        assertThat(actualTraffic, sameBeanAs(traffic));
    }

    @Test
    public void verifyThatRetrieveRetrievesCorrectEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Traffic firstTraffic = new Traffic(1, firstPlanet, secondPlanet, 2);
        Traffic secondTraffic = new Traffic(2, secondPlanet, firstPlanet, 3);
        session.save(firstTraffic);
        session.save(secondTraffic);

        // Exercise SUT
        Traffic actualTraffic = trafficDAO.retrieveTraffic(firstTraffic.getTrafficId());

        // Verify Behaviour
        assertThat(actualTraffic, sameBeanAs(firstTraffic));

    }

    @Test
    public void verifyThatRetrieveAllRetrievesAllEntries() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Traffic firstTraffic = new Traffic(1, firstPlanet, secondPlanet, 5);
        Traffic secondTraffic = new Traffic(2, firstPlanet, secondPlanet, 7);

        List<Traffic> traffic = Arrays.asList(firstTraffic, secondTraffic);
        session.save(traffic.get(0));
        session.save(traffic.get(1));

        // Exercise SUT
        List<Traffic> actualTraffic = trafficDAO.retrieveAll();

        // Verify Behaviour
        assertThat(actualTraffic, sameBeanAs(traffic));
    }

    @Test
    public void verifyThatUpdateChangesDBEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Traffic originalTraffic = new Traffic(1, firstPlanet, secondPlanet, 11);
        Traffic expectedTraffic = new Traffic(1, secondPlanet, firstPlanet, 13);
        session.save(originalTraffic);

        // Exercise SUT
        trafficDAO.updateTraffic(expectedTraffic);
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.eq("trafficId", originalTraffic.getTrafficId()));
        Traffic actualTraffic = (Traffic) criteria.uniqueResult();

        // Verify Behaviour
        assertThat(actualTraffic, sameBeanAs(expectedTraffic));
    }

    @Test
    public void verifyThatDeleteDeletesDBEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Traffic firstTraffic = new Traffic(1, firstPlanet, secondPlanet, 17);
        Traffic secondTraffic = new Traffic(2, secondPlanet, firstPlanet, 19);
        List<Traffic> expectedTraffic = Collections.singletonList(firstTraffic);
        session.save(firstTraffic);
        session.save(secondTraffic);

        // Exercise SUT
        trafficDAO.deleteTraffic(secondTraffic);
        Criteria criteria = session.createCriteria(Traffic.class);
        List<Traffic> actualTraffic = (List<Traffic>) criteria.list();

        // Verify Behaviour
        assertThat(actualTraffic, sameBeanAs(expectedTraffic));
    }

}