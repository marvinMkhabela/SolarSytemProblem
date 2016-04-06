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
import za.co.discovery.assignment.Models.Route;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Route.class, RouteDAO.class, DataSourceConfig.class,
        PersistenceConfig.class},
                loader = AnnotationConfigContextLoader.class)

public class RoutedaoIT {

    @Autowired
    private SessionFactory sessionFactory;
    private RouteDAO routeDAO;

    @Before
    public void initilizeTest() {
        routeDAO = new RouteDAO(sessionFactory);
    }

    @Test
    public void verifyIfSavePersistsObject() {

        //Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Route route = new Route(2,"A", "B", 0.2f);
        List<Route> expectedRoutes = Arrays.asList(route);

        //Exercise SUT
        routeDAO.save(route);
        Criteria criteria = session.createCriteria(Route.class);
        List<Route> actualRoutes = (List<Route>) criteria.list();

        //Verify Behaviour
        assertThat(actualRoutes, sameBeanAs(expectedRoutes));
    }

    @Test
    public void verifyIfTheUpdateMethodUpdatesTheDB() {

        // Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Route route = new Route(3,"A", "B", 0.3f);
        Route correctRoute = new Route(3,"C", "D", 3f);
        List<Route> expectedRoutes = Arrays.asList(correctRoute);
        session.save(route);

        // Exercise SUT
        routeDAO.update(correctRoute);
        Criteria criteria = session.createCriteria(Route.class);
        criteria.add(Restrictions.eq("routeId", correctRoute.getRouteId()));
        List<Route> actualRoutes = (List<Route>) criteria.list();

        // Verify Behaviour
        assertThat(actualRoutes, sameBeanAs(expectedRoutes));
    }

    @Test
    public void verifyIfRetrieveRetrievesTheCorrectDBEntry() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Route route = new Route(5, "A", "B", 0.5f);
        Route expectedRoute = new Route(7, "C", "D", 0.7f);
        session.save(route);
        session.save(expectedRoute);

        //Exercise SUT
        Route retrievedRoute = routeDAO.retrieve(expectedRoute.getRouteId());

        //Verify Behaviour
        assertThat(retrievedRoute, sameBeanAs(expectedRoute));
    }

    @Test
    public void verifyIfRetrieveAllRetrievesAllEntriesInTheDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Route route0 = new Route(11, "A", "B", 0.11f);
        Route route1 = new Route(13, "C", "D", 0.13f);
        List<Route> expectedRoutes = Arrays.asList(route0, route1);
        session.save(route0);
        session.save(route1);

        //Exercise SUT
        List<Route> actualRoutes = routeDAO.retrieveAll();

        //Verify Behaviour
        assertThat(actualRoutes, sameBeanAs(expectedRoutes));

    }

    @Test
    public void verifyIfDeleteDeletesEntriesFromDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Route route0 = new Route(17, "A", "B", 0.17f);
        Route route1 = new Route(19, "C", "D", 0.19f);
        List<Route> expectedRoutes = Arrays.asList(route0);
        session.save(route0);
        session.save(route1);

        //Exercise SUT
        routeDAO.delete(route1.getRouteId());
        Criteria criteria = session.createCriteria(Route.class);
        List<Route> actualRoutes = (List<Route>) criteria.list();

        //Verify Behaviour
        assertThat(actualRoutes, sameBeanAs(expectedRoutes));

    }
}