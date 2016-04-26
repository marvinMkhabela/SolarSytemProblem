package za.co.discovery.assignment.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.dto.PlanetDTO;
import za.co.discovery.assignment.dto.RouteDTO;
import za.co.discovery.assignment.dto.TrafficDTO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;

public class DTOServiceTest {

    private DTOService dtoService;
    private PlanetDAO planetDAO = Mockito.mock(PlanetDAO.class);
    private RouteDAO routeDAO = Mockito.mock(RouteDAO.class);
    private TrafficDAO trafficDAO = Mockito.mock(TrafficDAO.class);

    @Before
    public void initTest() {

        // Set Up Fixture
        List<Planet> planets = Arrays.asList(new Planet("A", "Earth", 0), new Planet("B", "Moon", 1), new Planet("C", "Jupiter", 2));
        List<Route> routes = Arrays.asList(new Route(1, planets.get(0), planets.get(1), 0.44),
                new Route(2, planets.get(0), planets.get(2), 1.89));
        List<Traffic> traffic = Arrays.asList(new Traffic(1, planets.get(0), planets.get(1), 0.3),
                new Traffic(2, planets.get(0), planets.get(2), 0.9));

        Mockito.when(planetDAO.retrieveAll()).thenReturn(planets);
        Mockito.when(routeDAO.retrieveAll()).thenReturn(routes);
        Mockito.when(trafficDAO.retrieveAll()).thenReturn(traffic);

        dtoService = new DTOService(planetDAO, routeDAO, trafficDAO);
    }

    @Test
    public void verifyThatTranscribePlanetReturnsCorrectPlanet() {

        // Set Up Fixture
        Planet expectedPlanet = new Planet("D", "Saturn", 3);
        PlanetDTO newPlanetDTO = new PlanetDTO();
        newPlanetDTO.setNode("D");
        newPlanetDTO.setName("Saturn");
        PlanetDTO containedPlanetDTO = new PlanetDTO();
        containedPlanetDTO.setNode("C");
        containedPlanetDTO.setName("Jupiter");

        // Exercise SUT
        Planet actualPlanet = dtoService.transcribePlanet(newPlanetDTO);
        Planet nullPlanet = dtoService.transcribePlanet(containedPlanetDTO);

        // Verify Behaviour
        assertThat(actualPlanet, sameBeanAs(expectedPlanet));
        assertThat(nullPlanet, is(nullValue()));

    }

    @Test
    public void verifyThatTranscribeRouteReturnsCorrectRoute() {

        // Set Up fixture
        Route expectedRoute = new Route(3, new Planet("A", "Earth", 0), new Planet("B", "Moon", 1), 0.44);
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setRouteId(3);
        routeDTO.setOrigin("Earth");
        routeDTO.setDestination("Moon");
        routeDTO.setDistance(0.44);

        RouteDTO nullRouteDTO = new RouteDTO();
        nullRouteDTO.setRouteId(2);
        nullRouteDTO.setOrigin("Earth");
        nullRouteDTO.setDestination("Saturn");
        nullRouteDTO.setDistance(0);

        // Exercise SUT
        Route actualRoute = dtoService.transcribeRoute(routeDTO);
        Route nullRoute = dtoService.transcribeRoute(nullRouteDTO);

        // Verify Behaviour
        assertThat(actualRoute, sameBeanAs(expectedRoute));
        assertThat(nullRoute, is(nullValue()));
    }

    @Test
    public void verifyThatTranscribeTrafficReturnsCorrectTraffic() {

        // Set Up Fixture
        Traffic expectedTraffic = new Traffic(3, new Planet("A", "Earth", 0), new Planet("B", "Moon", 1), 0.44);
        TrafficDTO trafficDTO = new TrafficDTO();
        trafficDTO.setTrafficId(3);
        trafficDTO.setOrigin("Earth");
        trafficDTO.setDestination("Moon");
        trafficDTO.setDelay(0.44);

        TrafficDTO nullTrafficDTO = new TrafficDTO();
        nullTrafficDTO.setOrigin("Earth");
        nullTrafficDTO.setDestination("Saturn");
        nullTrafficDTO.setDelay(0);

        // Exercise SUT
        Traffic actualTraffic = dtoService.transcribeTraffic(trafficDTO);
        Traffic nullTraffic = dtoService.transcribeTraffic(nullTrafficDTO);

        // Verify Behaviour
        assertThat(actualTraffic, sameBeanAs(expectedTraffic));
        assertThat(nullTraffic, is(nullValue()));

    }
}