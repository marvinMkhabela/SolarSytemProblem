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
import za.co.discovery.assignment.models.Route;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RouteDAO.class, PersistenceConfig.class, DataSourceConfig.class,
        Planet.class, PlanetDAO.class},
        loader = AnnotationConfigContextLoader.class)
public class RouteDaoIT {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RouteDAO routeDAO;
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
        Route route = new Route(1, origin, destination, 3);

        // Exercise Fixture
        routeDAO.saveRoute(route);
        Criteria criteria = session.createCriteria(Route.class);
        criteria.add(Restrictions.eq("routeId", route.getRouteId()));
        Route actualRoute = (Route) criteria.uniqueResult();

        // Verify Behaviour
        assertThat(actualRoute, sameBeanAs(route));
    }

    @Test
    public void verifyThatRetrieveRetrievesCorrectEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Route firstRoute = new Route(1, firstPlanet, secondPlanet, 2);
        Route secondRoute = new Route(2, secondPlanet, firstPlanet, 3);
        session.save(firstRoute);
        session.save(secondRoute);

        // Exercise SUT
        Route actualRoute = routeDAO.retrieveRoute(firstRoute.getRouteId());

        // Verify Behaviour
        assertThat(actualRoute, sameBeanAs(firstRoute));

    }

    @Test
    public void verifyThatRetrieveAllRetrievesAllEntries() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Route firstRoute = new Route(1, firstPlanet, secondPlanet, 5);
        Route secondRoute = new Route(2, firstPlanet, secondPlanet, 7);

        List<Route> route = Arrays.asList(firstRoute, secondRoute);
        session.save(route.get(0));
        session.save(route.get(1));

        // Exercise SUT
        List<Route> actualRoute = routeDAO.retrieveAll();

        // Verify Behaviour
        assertThat(actualRoute, sameBeanAs(route));
    }

    @Test
    public void verifyThatUpdateChangesDBEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Route originalRoute = new Route(1, firstPlanet, secondPlanet, 11);
        Route expectedRoute = new Route(1, secondPlanet, firstPlanet, 13);
        session.save(originalRoute);

        // Exercise SUT
        routeDAO.updateRoute(expectedRoute);
        Criteria criteria = session.createCriteria(Route.class);
        criteria.add(Restrictions.eq("routeId", originalRoute.getRouteId()));
        Route actualRoute = (Route) criteria.uniqueResult();

        // Verify Behaviour
        assertThat(actualRoute, sameBeanAs(expectedRoute));
    }

    @Test
    public void verifyThatDeleteDeletesDBEntry() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Planet firstPlanet = new Planet("A", "Earth", 1);
        Planet secondPlanet = new Planet("B", "Moon", 2);
        planetDAO.savePlanet(firstPlanet);
        planetDAO.savePlanet(secondPlanet);
        Route firstRoute = new Route(1, firstPlanet, secondPlanet, 17);
        Route secondRoute = new Route(2, secondPlanet, firstPlanet, 19);
        List<Route> expectedRoute = Collections.singletonList(firstRoute);
        session.save(firstRoute);
        session.save(secondRoute);

        // Exercise SUT
        routeDAO.deleteRoute(secondRoute);
        Criteria criteria = session.createCriteria(Route.class);
        List<Route> actualRoute = (List<Route>) criteria.list();

        // Verify Behaviour
        assertThat(actualRoute, sameBeanAs(expectedRoute));
    }

}