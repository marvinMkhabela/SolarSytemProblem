package za.co.discovery.assignment.controllers;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.dto.PlanetDTO;
import za.co.discovery.assignment.dto.RouteDTO;
import za.co.discovery.assignment.dto.TrafficDTO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.services.DTOService;
import za.co.discovery.assignment.services.PathService;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class RootControllerTest {

    private final String incompleteError = "Failed! Please check that all fields are completed";
    private final String success = "Success!";
    private final String sameValueError = "Failed! Please ensure source and destination are not the same";

    private PathService pathService = Mockito.mock(PathService.class);
    private PlanetDAO planetDAO = Mockito.mock(PlanetDAO.class);
    private RouteDAO routeDAO = Mockito.mock(RouteDAO.class);
    private TrafficDAO trafficDAO = Mockito.mock(TrafficDAO.class);
    private DTOService dtoService = Mockito.mock(DTOService.class);
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

    @Test
    public void verifyThatPlanetManipulationHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.retrievePlanets()).thenReturn(planets);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/planetManipulation"));

        // Verify Behaviour
        resultActions.andExpect(view().name("planetManipulation"))
                .andExpect(model().attribute("planets", planets));
    }

    @Test
    public void verifyThatCreatePlanetHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Planet expectedPlanet = new Planet("U", "Uranus", 3);
        ArgumentCaptor<Planet> planetArgumentCaptor = ArgumentCaptor.forClass(Planet.class);
        ArgumentCaptor<PlanetDTO> planetDTOArgumentCaptor = ArgumentCaptor.forClass(PlanetDTO.class);
        final String existsError = "Failed! Planet already exists";
        PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setName("Uranus");
        planetDTO.setNode("U");
        Mockito.when(dtoService.transcribePlanet(planetDTOArgumentCaptor.capture())).thenReturn(expectedPlanet);

        // Exercise SUT
        ResultActions resultActionsWithNewValue = mockMvc.perform(post("/createPlanet")
                        .param("node", planetDTO.getNode())
                        .param("name", planetDTO.getName())
        );
        Mockito.verify(planetDAO).savePlanet(planetArgumentCaptor.capture());
        Mockito.reset(dtoService);
        ResultActions resultActionsWithEmptyFields = mockMvc.perform(post("/createPlanet")
                        .param("node", "")
                        .param("name", "")
        );
        ResultActions resultActionsWithExistingPlanet = mockMvc.perform(post("/createPlanet")
                        .param("node", "A")
                        .param("name", "Earth")
        );

        // Verify Behaviour
        resultActionsWithNewValue.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", success));
        resultActionsWithEmptyFields.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", incompleteError));
        resultActionsWithExistingPlanet.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", existsError));
        assertThat(planetArgumentCaptor.getValue(), sameBeanAs(expectedPlanet));
        assertThat(planetDTOArgumentCaptor.getValue(), sameBeanAs(planetDTO));
    }

    @Test
    public void verifyThatPlanetEditHasTheCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        final String unknownError = "Failed! Unknown planet";
        Planet expectedPlanet = new Planet("B", "Lunar", 1);
        Mockito.when(pathService.retrievePlanets()).thenReturn(planets);
        ArgumentCaptor<Planet> planetArgumentCaptor = ArgumentCaptor.forClass(Planet.class);

        // Exercise SUT
        ResultActions resultActionsWithValidPlanet = mockMvc.perform(post("/editPlanet")
                        .param("node", "B")
                        .param("name", "Lunar")
        );
        Mockito.verify(planetDAO).updatePlanet(planetArgumentCaptor.capture());
        ResultActions resultActionsWithEmptyPlanet = mockMvc.perform(post("/editPlanet")
                        .param("node", "")
                        .param("name", "")
        );
        ResultActions resultActionsWithUnknownNode = mockMvc.perform(post("/editPlanet")
                        .param("node", "K")
                        .param("name", "Kepler")
        );

        // Verify Behaviour
        resultActionsWithValidPlanet
                .andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", success));
        resultActionsWithEmptyPlanet
                .andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", incompleteError));
        resultActionsWithUnknownNode
                .andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", unknownError));
        assertThat(planetArgumentCaptor.getValue(), sameBeanAs(expectedPlanet));
    }

    @Test
    public void verifyThatPlanetDeleteHasTheCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        ArgumentCaptor<Planet> planetArgumentCaptor = ArgumentCaptor.forClass(Planet.class);
        Mockito.when(pathService.retrievePlanets()).thenReturn(planets);
        ConstraintViolationException exception = Mockito.mock(ConstraintViolationException.class);

        // Exercise Fixture
        ResultActions resultActionsUnknownNode = mockMvc.perform(post("/deletePlanet")
                        .param("node", "K")
                        .param("name", "Krypton")
        );
        ResultActions resultActionsEmptyName = mockMvc.perform(post("/deletePlanet")
                        .param("node", "C")
                        .param("name", "")
        );
        ResultActions resultActionsValidInput = mockMvc.perform(post("/deletePlanet")
                        .param("node", "C")
                        .param("name", "Jupiter")
        );
        Mockito.doThrow(exception).when(planetDAO).deletePlanet(planetArgumentCaptor.capture());
        ResultActions resultActionsException = mockMvc.perform(post("/deletePlanet")
                        .param("node", "A")
                        .param("name", "Earth")
        );

        // Verify Behaviour
        resultActionsEmptyName.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", "Failed! Invalid input combination. Please enter correct name."));
        resultActionsUnknownNode.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", "Failed! Unknown planet"));
        resultActionsValidInput.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", "Success!"));
        resultActionsException.andExpect(view().name("redirect:/planetManipulation"))
                .andExpect(flash().attribute("message", "Failed! Cannot delete planet, there are routes/traffic dependant on it."));
    }

    @Test
    public void verifyThatRouteManipulationHasTheCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.retrievePlanets()).thenReturn(planets);
        Mockito.when(pathService.retrieveRoutes()).thenReturn(routes);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/routeManipulation"));

        // Verify Behaviour
        resultActions.andExpect(view().name("routeManipulation"))
                .andExpect(model().attribute("planets", planets))
                .andExpect(model().attribute("routes", routes));
    }

    @Test
    public void verifyThatRouteEditHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        final String zeroError = "Failed! Please ensure that all fields are completed, zero distances are not valid.";
        Route expectedRoute = new Route(2, planets.get(0), planets.get(2), 4.0);
        RouteDTO expectedRouteDTO = new RouteDTO();
        expectedRouteDTO.setRouteId(2);
        expectedRouteDTO.setOrigin("Earth");
        expectedRouteDTO.setDestination("Jupiter");
        expectedRouteDTO.setDistance(4.0);
        ArgumentCaptor<RouteDTO> routeDTOArgumentCaptor = ArgumentCaptor.forClass(RouteDTO.class);
        ArgumentCaptor<Route> routeArgumentCaptor = ArgumentCaptor.forClass(Route.class);
        Mockito.when(dtoService.transcribeRoute(routeDTOArgumentCaptor.capture())).thenReturn(expectedRoute);

        // Exercise SUT
        ResultActions resultActionsWithValidInput = mockMvc.perform(post("/editRoute")
                        .param("routeId", "2")
                        .param("origin", "Earth")
                        .param("destination", "Jupiter")
                        .param("distance", "4.0")
        );
        Mockito.verify(routeDAO).updateRoute(routeArgumentCaptor.capture());
        Mockito.reset(dtoService);
        ResultActions resultActionsWithEmptyFields = mockMvc.perform(post("/editRoute")
                        .param("routeId", "0")
                        .param("origin", "")
                        .param("destination", "")
                        .param("distance", "0")
        );
        ResultActions resultActionsWithSameSourceAndDestination = mockMvc.perform(post("/editRoute")
                        .param("routeId", "1")
                        .param("origin", "Earth")
                        .param("destination", "Earth")
                        .param("distance", "4.0")
        );

        // Verify Behaviour
        resultActionsWithValidInput.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", success));
        resultActionsWithEmptyFields.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", zeroError));
        resultActionsWithSameSourceAndDestination.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", sameValueError));
        assertThat(routeArgumentCaptor.getValue(), sameBeanAs(expectedRoute));
        assertThat(routeDTOArgumentCaptor.getValue(), sameBeanAs(expectedRouteDTO));
    }

    @Test
    public void verifyThatRouteCreateHasTheCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        final String zeroError = "Failed! Please ensure that all fields are completed, zero distances are not valid.";
        Route expectedRoute = new Route(3, planets.get(1), planets.get(2), 2.0);
        RouteDTO expectedRouteDTO = new RouteDTO();
        expectedRouteDTO.setRouteId(3);
        expectedRouteDTO.setOrigin("Moon");
        expectedRouteDTO.setDestination("Jupiter");
        expectedRouteDTO.setDistance(2.0);
        ArgumentCaptor<RouteDTO> routeDTOArgumentCaptor = ArgumentCaptor.forClass(RouteDTO.class);
        ArgumentCaptor<Route> routeArgumentCaptor = ArgumentCaptor.forClass(Route.class);
        Mockito.when(dtoService.transcribeRoute(routeDTOArgumentCaptor.capture())).thenReturn(expectedRoute);
        Mockito.when(pathService.retrieveRoutes()).thenReturn(routes);

        // Exercise SUT
        ResultActions resultActionsWithValidInput = mockMvc.perform(post("/createRoute")
                        .param("origin", "Moon")
                        .param("destination", "Jupiter")
                        .param("distance", "2.0")
        );
        Mockito.verify(routeDAO).saveRoute(routeArgumentCaptor.capture());
        Mockito.reset(dtoService);
        ResultActions resultActionsWithEmptyFields = mockMvc.perform(post("/createRoute")
                        .param("origin", "")
                        .param("destination", "")
                        .param("distance", "0")
        );
        ResultActions resultActionsWithSameSourceAndDestination = mockMvc.perform(post("/createRoute")
                        .param("origin", "Earth")
                        .param("destination", "Earth")
                        .param("distance", "2.0")
        );

        // Verify Behaviour
        resultActionsWithValidInput.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", success));
        resultActionsWithEmptyFields.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", zeroError));
        resultActionsWithSameSourceAndDestination.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", sameValueError));
        assertThat(routeArgumentCaptor.getValue(), sameBeanAs(expectedRoute));
        assertThat(routeDTOArgumentCaptor.getValue(), sameBeanAs(expectedRouteDTO));
    }

    @Test
    public void verifyThatRouteDeleteHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Route expectedRoute = new Route(1, planets.get(0), planets.get(1), 0);
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setRouteId(1);
        routeDTO.setOrigin("Earth");
        routeDTO.setDestination("Moon");
        ArgumentCaptor<RouteDTO> routeDTOArgumentCaptor = ArgumentCaptor.forClass(RouteDTO.class);
        Mockito.when(dtoService.transcribeRoute(routeDTOArgumentCaptor.capture()))
                .thenReturn(expectedRoute);

        // Exercise SUT
        ResultActions resultActionsZeroId = mockMvc.perform(post("/deleteRoute")
                .param("RouteId", "0"));
        ResultActions resultActionsNonZeroId = mockMvc.perform(post("/deleteRoute")
                        .param("RouteId", "1")
                        .param("origin", "")
                        .param("destination", "")
        );
        ResultActions resultActionsValidInput = mockMvc.perform(post("/deleteRoute")
                        .param("routeId", "1")
                        .param("origin", "Earth")
                        .param("destination", "Moon")
        );

        // Verify Behaviour
        resultActionsZeroId.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", "Failed! Route not found."));
        resultActionsNonZeroId.andExpect(view().name("redirect:/routeManipulation"))
                .andExpect(flash().attribute("message", "Failed! Please confirm delete by entering source and destination."));
        resultActionsValidInput.andExpect(view().name("redirect:/routeManipulation"));
        assertThat(routeDTOArgumentCaptor.getValue(), sameBeanAs(routeDTO));
    }

    @Test
    public void verifyThatTrafficManipulationHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Mockito.when(pathService.retrievePlanets()).thenReturn(planets);
        Mockito.when(pathService.retrieveTraffic()).thenReturn(traffic);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/trafficManipulation"));

        // Verify Behaviour
        resultActions.andExpect(view().name("trafficManipulation"))
                .andExpect(model().attribute("planets", planets))
                .andExpect(model().attribute("traffic", traffic));
    }

    @Test
    public void verifyThatTrafficEditHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        final String zeroError = "Failed! Please ensure that all fields are completed, zero delays are not valid.";
        Traffic expectedTraffic = new Traffic(2, planets.get(0), planets.get(2), 4.0);
        TrafficDTO expectedTrafficDTO = new TrafficDTO();
        expectedTrafficDTO.setTrafficId(2);
        expectedTrafficDTO.setOrigin("Earth");
        expectedTrafficDTO.setDestination("Jupiter");
        expectedTrafficDTO.setDelay(4.0);
        ArgumentCaptor<TrafficDTO> trafficDTOArgumentCaptor = ArgumentCaptor.forClass(TrafficDTO.class);
        ArgumentCaptor<Traffic> trafficArgumentCaptor = ArgumentCaptor.forClass(Traffic.class);
        Mockito.when(dtoService.transcribeTraffic(trafficDTOArgumentCaptor.capture())).thenReturn(expectedTraffic);

        // Exercise SUT
        ResultActions resultActionsWithValidInput = mockMvc.perform(post("/editTraffic")
                        .param("trafficId", "2")
                        .param("origin", "Earth")
                        .param("destination", "Jupiter")
                        .param("delay", "4.0")
        );
        Mockito.verify(trafficDAO).updateTraffic(trafficArgumentCaptor.capture());
        Mockito.reset(dtoService);
        ResultActions resultActionsWithEmptyFields = mockMvc.perform(post("/editTraffic")
                        .param("trafficId", "0")
                        .param("origin", "")
                        .param("destination", "")
                        .param("delay", "0")
        );
        ResultActions resultActionsWithSameSourceAndDestination = mockMvc.perform(post("/editTraffic")
                        .param("trafficId", "1")
                        .param("origin", "Earth")
                        .param("destination", "Earth")
                        .param("delay", "4.0")
        );

        // Verify Behaviour
        resultActionsWithValidInput.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", success));
        resultActionsWithEmptyFields.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", zeroError));
        resultActionsWithSameSourceAndDestination.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", sameValueError));
        assertThat(trafficArgumentCaptor.getValue(), sameBeanAs(expectedTraffic));
        assertThat(trafficDTOArgumentCaptor.getValue(), sameBeanAs(expectedTrafficDTO));
    }

    @Test
    public void verifyThatTrafficCreateHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        final String zeroError = "Failed! Please ensure that all fields are completed, zero delays are not valid.";
        Traffic expectedTraffic = new Traffic(3, planets.get(1), planets.get(2), 2.0);
        TrafficDTO expectedTrafficDTO = new TrafficDTO();
        expectedTrafficDTO.setTrafficId(3);
        expectedTrafficDTO.setOrigin("Moon");
        expectedTrafficDTO.setDestination("Jupiter");
        expectedTrafficDTO.setDelay(2.0);
        ArgumentCaptor<TrafficDTO> trafficDTOArgumentCaptor = ArgumentCaptor.forClass(TrafficDTO.class);
        ArgumentCaptor<Traffic> trafficArgumentCaptor = ArgumentCaptor.forClass(Traffic.class);
        Mockito.when(dtoService.transcribeTraffic(trafficDTOArgumentCaptor.capture())).thenReturn(expectedTraffic);
        Mockito.when(pathService.retrieveTraffic()).thenReturn(traffic);

        // Exercise SUT
        ResultActions resultActionsWithValidInput = mockMvc.perform(post("/createTraffic")
                        .param("origin", "Moon")
                        .param("destination", "Jupiter")
                        .param("delay", "2.0")
        );
        Mockito.verify(trafficDAO).saveTraffic(trafficArgumentCaptor.capture());
        Mockito.reset(dtoService);
        ResultActions resultActionsWithEmptyFields = mockMvc.perform(post("/createTraffic")
                        .param("origin", "")
                        .param("destination", "")
                        .param("delay", "0")
        );
        ResultActions resultActionsWithSameSourceAndDestination = mockMvc.perform(post("/createTraffic")
                        .param("origin", "Earth")
                        .param("destination", "Earth")
                        .param("delay", "2.0")
        );

        // Verify Behaviour
        resultActionsWithValidInput.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", success));
        resultActionsWithEmptyFields.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", zeroError));
        resultActionsWithSameSourceAndDestination.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", sameValueError));
        assertThat(trafficArgumentCaptor.getValue(), sameBeanAs(expectedTraffic));
        assertThat(trafficDTOArgumentCaptor.getValue(), sameBeanAs(expectedTrafficDTO));
    }

    @Test
    public void verifyThatTrafficDeleteHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setUpFixture();
        Traffic expectedTraffic = new Traffic(1, planets.get(0), planets.get(1), 0);
        TrafficDTO trafficDTO = new TrafficDTO();
        trafficDTO.setTrafficId(1);
        trafficDTO.setOrigin("Earth");
        trafficDTO.setDestination("Moon");
        ArgumentCaptor<TrafficDTO> trafficDTOArgumentCaptor = ArgumentCaptor.forClass(TrafficDTO.class);
        Mockito.when(dtoService.transcribeTraffic(trafficDTOArgumentCaptor.capture()))
                .thenReturn(expectedTraffic);

        // Exercise SUT
        ResultActions resultActionsZeroId = mockMvc.perform(post("/deleteTraffic")
                .param("trafficId", "0"));
        ResultActions resultActionsNonZeroId = mockMvc.perform(post("/deleteTraffic")
                        .param("trafficId", "1")
                        .param("origin", "")
                        .param("destination", "")
        );
        ResultActions resultActionsValidInput = mockMvc.perform(post("/deleteTraffic")
                        .param("trafficId", "1")
                        .param("origin", "Earth")
                        .param("destination", "Moon")
        );

        // Verify Behaviour
        resultActionsZeroId.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", "Failed! Traffic not found."));
        resultActionsNonZeroId.andExpect(view().name("redirect:/trafficManipulation"))
                .andExpect(flash().attribute("message", "Failed! Please confirm delete by entering source and destination."));
        resultActionsValidInput.andExpect(view().name("redirect:/trafficManipulation"));
        assertThat(trafficDTOArgumentCaptor.getValue(), sameBeanAs(trafficDTO));
    }

    @Test
    public void verifyThatAdminPageHasCorrectView() throws Exception {

        // Set Up Fixture
        setUpFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/admin"));

        // Verify Behaviour
        resultActions.andExpect(view().name("admin"));
    }

    private void setUpFixture() {

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

        RootController rootController = new RootController(pathService, planetDAO, routeDAO, trafficDAO, dtoService);
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