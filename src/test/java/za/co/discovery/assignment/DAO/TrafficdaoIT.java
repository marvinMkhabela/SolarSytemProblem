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
import za.co.discovery.assignment.Models.Traffic;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class, PersistenceConfig.class, Traffic.class, TrafficDAO.class},
                loader = AnnotationConfigContextLoader.class)

public class TrafficdaoIT {
    @Autowired
    private SessionFactory sessionFactory;
    private TrafficDAO trafficDAO;

    @Before
    public void initilizeTest() {
        trafficDAO = new TrafficDAO(sessionFactory);
    }

    @Test
    public void verifyIfSavePersistsObject() {

        //Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic = new Traffic(2, "A", "B", 2f);
        List<Traffic> expectedTraffic = Arrays.asList(traffic);

        //Exercise SUT
        trafficDAO.save(traffic);
        Criteria criteria = session.createCriteria(Traffic.class);
        List<Traffic> actualTraffic = (List<Traffic>) criteria.list();

        //Verify Behaviour
        assertThat(expectedTraffic, sameBeanAs(actualTraffic));
    }

    @Test
    public void verifyIfTheUpdateMethodUpdateTheDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic = new Traffic(3, "A", "B", 3f);
        Traffic correctTraffic = new Traffic(3, "C", "D", 0.3f);
        List<Traffic> expectedTraffic = Arrays.asList(correctTraffic);
        session.save(traffic);

        //Exercise SUT
        trafficDAO.update(correctTraffic);
        Criteria criteria = session.createCriteria(Traffic.class);
        criteria.add(Restrictions.eq("routeId", correctTraffic.getRoute()));
        List<Traffic> actualTraffic = (List<Traffic>) criteria.list();

        //Verify Behaviour
        assertThat(expectedTraffic, sameBeanAs(actualTraffic));
    }

    @Test
    public void verifyIfRetrieveRetrievesTheCorrectDBEntry() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic = new Traffic(5, "A", "B", 5f);
        Traffic expectedTraffic = new Traffic(7, "C", "D", 7f);
        session.save(traffic);
        session.save(expectedTraffic);

        //Exercise SUT
        Traffic retrievedTraffic = trafficDAO.retrieve(expectedTraffic.getRoute());

        //Verify Behaviour
        assertThat(retrievedTraffic, sameBeanAs(expectedTraffic));
    }

    @Test
    public void verifyIfRetrieveAllRetrievesAllEntriesInTheDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic0 = new Traffic(11, "A", "B", 11f);
        Traffic traffic1 = new Traffic(13, "C", "D", 13f);
        List<Traffic> expectedTraffic = Arrays.asList(traffic0, traffic1);
        session.save(traffic0);
        session.save(traffic1);

        //Exercise SUT
        List<Traffic> actualTraffic = trafficDAO.retrieveAll();

        //Verify Behaviour
        assertThat(expectedTraffic, sameBeanAs(actualTraffic));

    }

    @Test
    public void verifyIfDeleteDeletesEntriesFromDB() {

        //Set up fixture
        Session session = sessionFactory.getCurrentSession();
        Traffic traffic0 = new Traffic(17, "A", "B", 17f);
        Traffic traffic1 = new Traffic(19, "C", "D", 19f);
        List<Traffic> expectedTraffic = Arrays.asList(traffic0);
        session.save(traffic0);
        session.save(traffic1);

        //Exercise SUT
        trafficDAO.delete(traffic1.getRoute());
        Criteria criteria = session.createCriteria(Traffic.class);
        List<Traffic> actualTraffic = (List<Traffic>) criteria.list();

        //Verify Behaviour
        assertThat(expectedTraffic, sameBeanAs(actualTraffic));

    }
}