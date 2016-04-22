package za.co.discovery.assignment.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.configuration.TestResourceConfig;
import za.co.discovery.assignment.configurations.DataSourceConfig;
import za.co.discovery.assignment.configurations.PersistenceConfig;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, DataSourceConfig.class, TestResourceConfig.class,
        PlanetDAO.class, RouteDAO.class, TrafficDAO.class, ExcelDataReader.class,
        PathTranscriptionService.class, PathService.class},
        loader = AnnotationConfigContextLoader.class)
public class PathServiceIT {

    @Autowired
    private PathService pathService;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void verifyThatThePathRefreshMethodRefreshesPaths() {

        // Set Up Fixture
        Session session = sessionFactory.getCurrentSession();
        List<Planet> expectedPlanets = Arrays.asList(new Planet("A", "Earth", 0), new Planet("B", "Moon", 1), new Planet("C", "Jupiter", 2));
        List<Route> expectedRoutes = Arrays.asList(new Route(1, expectedPlanets.get(0), expectedPlanets.get(1), 0.44),
                new Route(2, expectedPlanets.get(0), expectedPlanets.get(2), 1.89));
        List<Traffic> expectedTraffic = Arrays.asList(new Traffic(1, expectedPlanets.get(0), expectedPlanets.get(1), 0.3),
                new Traffic(2, expectedPlanets.get(0), expectedPlanets.get(2), 0.9));

        expectedRoutes.get(0).appendTrafficDelay(0.3);
        expectedRoutes.get(1).appendTrafficDelay(0.9);

        session.save(expectedPlanets.get(2));
        session.save(expectedRoutes.get(1));
        session.save(expectedTraffic.get(1));

        PathResponseDto earthPath = new PathResponseDto();
        earthPath.setName("Earth");
        earthPath.setPath("Travel from Earth");
        PathResponseDto moonPath = new PathResponseDto();
        moonPath.setName("Moon");
        moonPath.setPath("Travel from Earth, to Moon");
        PathResponseDto jupiterPath = new PathResponseDto();
        jupiterPath.setName("Jupiter");
        jupiterPath.setPath("Travel from Earth, to Jupiter");
        List<PathResponseDto> expectedPaths = Arrays.asList(earthPath, moonPath, jupiterPath);


        // Exercise SUT
        List<Planet> actualPlanets = pathService.retrievePlanets();
        List<Route> actualRoutes = pathService.retrieveRoutes();
        List<Traffic> actualTraffic = pathService.retrieveTraffic();
        List<PathResponseDto> actualPaths = pathService.refreshPaths();

        // Verify Behaviour
        assertThat(actualPlanets, sameBeanAs(expectedPlanets));
        assertThat(actualRoutes, sameBeanAs(expectedRoutes));
        assertThat(actualTraffic, sameBeanAs(expectedTraffic));
        assertThat(actualPaths, sameBeanAs(expectedPaths));
    }

}