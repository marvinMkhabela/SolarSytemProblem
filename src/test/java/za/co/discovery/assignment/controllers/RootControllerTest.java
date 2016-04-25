package za.co.discovery.assignment.controllers;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.services.PathService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class RootControllerTest {

    private PathService pathService = Mockito.mock(PathService.class);
    private PlanetDAO planetDAO = Mockito.mock(PlanetDAO.class);
    private RouteDAO routeDAO = Mockito.mock(RouteDAO.class);
    private TrafficDAO trafficDAO = Mockito.mock(TrafficDAO.class);
    private MockMvc mockMvc;

    private List<Planet> planets;
    private List<Route> routes;
    private List<Traffic> traffic;
    private List<PathResponseDto> paths;


    @Test
    public void verifyThatHomePageHasCorrectTemplate() throws Exception {

        // Set Up Fixture
        setUpFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/"));

        // Verify Behaviour
        resultActions.andExpect(view().name("home"));
    }

    @Test
    public void verifyThatPlanetPageHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.retrievePlanets()).thenReturn(planets);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/planets"));

        // Verify Behaviour
        resultActions.andExpect(view().name("planets"));
        resultActions.andExpect(model().attribute("planets", planets));
    }

    @Test
    public void verifyThatRoutePageHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.retrieveRoutes()).thenReturn(routes);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/routes"));

        // Verify Behaviour
        resultActions.andExpect(view().name("routes"));
        resultActions.andExpect(model().attribute("routes", routes));
    }

    @Test
    public void verifyThatPathPageHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.refreshPaths()).thenReturn(paths);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/paths"));

        // Verify Behaviour
        resultActions.andExpect(view().name("paths"));
        resultActions.andExpect(model().attribute("paths", paths));
    }

    @Test
    public void verifyThatTrafficPageHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.retrieveTraffic()).thenReturn(traffic);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/traffic"));

        // Verify Behaviour
        resultActions.andExpect(view().name("traffic"));
        resultActions.andExpect(model().attribute("traffic", traffic));
    }

    public void setUpFixture() {

        planets = Arrays.asList(new Planet("A", "Earth", 0), new Planet("B", "Moon", 1), new Planet("C", "Jupiter", 2));
        routes = Arrays.asList(new Route(1, planets.get(0), planets.get(1), 0.44),
                new Route(2, planets.get(0), planets.get(2), 1.89));
        traffic = Arrays.asList(new Traffic(1, planets.get(0), planets.get(1), 0.3),
                new Traffic(2, planets.get(0), planets.get(2), 0.9));

        routes.get(0).appendTrafficDelay(0.3);
        routes.get(1).appendTrafficDelay(0.9);

        PathResponseDto earthPath = new PathResponseDto();
        earthPath.setName("Earth");
        earthPath.setPath("Travel from Earth");
        PathResponseDto moonPath = new PathResponseDto();
        moonPath.setName("Moon");
        moonPath.setPath("Travel from Earth, to Moon");
        PathResponseDto jupiterPath = new PathResponseDto();
        jupiterPath.setName("Jupiter");
        jupiterPath.setPath("Travel from Earth, to Jupiter");
        paths = Arrays.asList(earthPath, moonPath, jupiterPath);

        RootController rootController = new RootController(pathService, planetDAO, routeDAO, trafficDAO);
        mockMvc = standaloneSetup(rootController)
                .setViewResolvers(getInternalResourceViewResolver())
                .build();

    }

    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

}