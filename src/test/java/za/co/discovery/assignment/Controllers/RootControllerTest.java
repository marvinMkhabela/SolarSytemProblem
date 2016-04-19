package za.co.discovery.assignment.Controllers;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.DAO.EdgeDAO;
import za.co.discovery.assignment.DAO.TrafficDAO;
import za.co.discovery.assignment.DAO.VertexDAO;
import za.co.discovery.assignment.DTO.PathDTO;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;
import za.co.discovery.assignment.Services.Graph;
import za.co.discovery.assignment.Services.StartUpDataMigrationService;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentCaptor.forClass;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class RootControllerTest {

    private MockMvc mockMvc;
    private SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
    private VertexDAO vertexDAO = Mockito.mock(VertexDAO.class);
    private EdgeDAO edgeDAO = Mockito.mock(EdgeDAO.class);
    private TrafficDAO trafficDAO = Mockito.mock(TrafficDAO.class);
    private StartUpDataMigrationService startUpDataMigrationService = Mockito.mock(StartUpDataMigrationService.class);

    private List<Vertex> vertices;
    private List<Edge> edges;
    private List<Traffic> traffic;
    private RootController rootController;

    @Before
    public void initData() {
        vertices = Arrays.asList(new Vertex("A", "Earth"), new Vertex("B", "Moon"), new Vertex("C", "Jupiter"));
        edges = Arrays.asList(new Edge(1, "A", "B", 3f), new Edge(2, "B", "C", 5f));
        traffic = Arrays.asList(new Traffic(1, "A", "B", 2f), new Traffic(2, "B", "C", 2f));
    }

    @Test
    public void verifyThatAllDataIsInitializedOnStartUp() {

        // Set Up Fixture
        List<Vertex> vertices = Arrays.asList(new Vertex("A", "Earth"), new Vertex("B", "Moon"), new Vertex("C", "Jupiter"));
        List<Edge> edges = Arrays.asList(new Edge(1, "A", "B", 3f), new Edge(2, "B", "C", 5f));
        List<Traffic> traffic = Arrays.asList(new Traffic(1, "A", "B", 2f), new Traffic(2, "B", "C", 2f));
        Graph graph = new Graph(vertices, edges);

        Mockito.when(startUpDataMigrationService.createGraph()).thenReturn(graph);
        Mockito.when(startUpDataMigrationService.retrieveAllVertices()).thenReturn(vertices);
        Mockito.when(startUpDataMigrationService.retrieveAllEdges()).thenReturn(edges);
        Mockito.when(startUpDataMigrationService.retrieveAllTraffic()).thenReturn(traffic);

        rootController = new RootController(vertexDAO, edgeDAO, trafficDAO, startUpDataMigrationService);

        PathDTO path0 = new PathDTO();
        path0.setName("Earth");
        path0.setPath("Travel from Earth");
        PathDTO path1 = new PathDTO();
        path1.setName("Moon");
        path1.setPath("Travel from Earth, to Moon");
        PathDTO path2 = new PathDTO();
        path2.setName("Jupiter");
        path2.setPath("Travel from Earth, to Moon, to Jupiter");
        List<PathDTO> expectedPaths = Arrays.asList(path0, path1, path2);

        // Exercise SUT
        rootController.home();

        // Verify Behaviour
        assertThat(rootController.vertices, sameBeanAs(vertices));
        assertThat(rootController.edges, sameBeanAs(edges));
        assertThat(rootController.traffic, sameBeanAs(traffic));
        assertThat(rootController.paths, sameBeanAs(expectedPaths));
    }

    @Test
    public void verifyThatSectionEvaluationSetsCorrectModelAttribute() throws Exception {

        // Set up fixture
        PathDTO path0 = new PathDTO();
        path0.setName("Earth");
        path0.setPath("Travel from Earth");
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/selectedDestination")
                .param("selectedDestination", "Earth"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("choiceMade", is("true")));
        resultActions.andExpect(model().attribute("targetPath", sameBeanAs(path0)));
    }

    @Test
    public void verifyThatQueryHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/query"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("vertices", sameBeanAs(vertices)));
        resultActions.andExpect(view().name("queryPath"));
    }

    @Test
    public void verifyThatUpdatePlanetHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/update"));

        //Verify Behaviour
        resultActions.andExpect(model().attribute("vertices", sameBeanAs(vertices)));
        resultActions.andExpect(view().name("updatePlanet"));
    }

    @Test
    public void verifyThatCreatePlanetHasCorrectView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/create"));

        // Verify Behaviour
        resultActions.andExpect(view().name("createPlanet"));
    }

    @Test
    public void verifyThatDeletePlanetHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/delete"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("vertices", sameBeanAs(vertices)));
        resultActions.andExpect(view().name("deletePlanet"));
    }

    @Test
    public void verifyThatDeleteRouteHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/deleteRoute"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("edges", sameBeanAs(edges)));
        resultActions.andExpect(view().name("deleteRoute"));
    }

    @Test
    public void verifyThatUpdateRouteHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/updateRoute"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("edges", sameBeanAs(edges)));
        resultActions.andExpect(view().name("updateRoute"));
    }

    @Test
    public void verifyThatCreateRouteHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/createRoute"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("vertices", sameBeanAs(vertices)));
        resultActions.andExpect(view().name("createRoute"));
    }

    @Test
    public void verifyThatDeleteTrafficHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/deleteTraffic"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("traffic", sameBeanAs(traffic)));
        resultActions.andExpect(view().name("deleteTraffic"));
    }

    @Test
    public void verifyThatUpdateTrafficHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/updateTraffic"));

        // Verify Behaviour
        resultActions.andExpect(model().attribute("traffic", sameBeanAs(traffic)));
        resultActions.andExpect(view().name("updateTraffic"));
    }

    @Test
    public void verifyThatCreateTrafficHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/createTraffic"));

        // Verify Behaviour
        resultActions.andExpect(view().name("createTraffic"));
        resultActions.andExpect(model().attribute("vertices", vertices));
    }

    @Test
    public void verifyThatMainMenuHasCorrectView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/menu"));

        // Verify Behaviour
        resultActions.andExpect(view().name("menu"));
    }

    @Test
    public void verifyThatUpdateNodeHasCorrectContent() throws Exception {

        // Set up fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/updateNode")
                .param("updateNode", "A"));

        // Verify Behaviour
        resultActions.andExpect(content().string("Earth"));
    }

    @Test
    public void verifyThatPersistUpdateHasCorrectModelAndView() throws Exception {

        // Set Up Fixture
        setupFixture();
        Vertex expectedVertex = new Vertex("C", "Gas Giant");
        ArgumentCaptor<Vertex> vertexCaptor = forClass(Vertex.class);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/updateNewName")
                .param("updatedName", "Gas Giant")
                .param("updateNode", "C"));
        Mockito.verify(vertexDAO).update(vertexCaptor.capture());

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
        assertThat(vertexCaptor.getValue(), sameBeanAs(expectedVertex));
    }

    @Test
    public void verifyThatEvaluatePlanetEntryHasCorrectContent() throws Exception {

        // Set up fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions0 = mockMvc.perform(get("/createPlanet")
                .param("creationNode", "M'")
                .param("creationName", "M83"));

        ResultActions resultActions1 = mockMvc.perform(get("/createPlanet")
                .param("creationNode", "A")
                .param("creationName", "Blue"));


        // Verify Behaviour
        resultActions0.andExpect(content().string("No"));
        resultActions1.andExpect(content().string("Yes"));
    }

    @Test
    public void verifyThatStorePlanetEntryHasCorrectVertexAndView() throws Exception {

        // Set Up Fixture
        setupFixture();
        Vertex expectedVertex = new Vertex("K", "Krypton");
        ArgumentCaptor<Vertex> vertexCaptor = forClass(Vertex.class);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/persistEntry")
                .param("creationNode", "K")
                .param("creationName", "Krypton"));
        Mockito.verify(vertexDAO).save(vertexCaptor.capture());

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
        assertThat(vertexCaptor.getValue(), sameBeanAs(expectedVertex));
    }

    @Test
    public void verifyThatConfirmedDeleteHasCorrectView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/deletePlanet")
                .param("planetName", "Earth"));

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
    }

    @Test
    public void verifyThatRouteDeletionHasCorrectView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/confirmRouteDeletion")
                .param("deletionId", "1"));

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
    }

    @Test
    public void verifyThatRouteUpdateHasCorrectEdgeAndView() throws Exception {

        // Set Up Fixture
        setupFixture();
        Edge expectedEdge = new Edge(1, "A", "B", 5f);
        ArgumentCaptor<Edge> edgeCaptor = forClass(Edge.class);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/confirmRouteUpdate")
                .param("selectedId", "1")
                .param("origin", "A")
                .param("destination", "B")
                .param("distance", "5"));
        Mockito.verify(edgeDAO).update(edgeCaptor.capture());

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
        assertThat(edgeCaptor.getValue(), sameBeanAs(expectedEdge));
    }

    @Test
    public void verifyThatProposeRouteCreationHasCorrectContent() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions0 = mockMvc.perform(get("/proposeRouteCreation")
                .param("origin", "A")
                .param("destination", "C"));
        ResultActions resultActions1 = mockMvc.perform(get("/proposeRouteCreation")
                .param("origin", "A")
                .param("destination", "B"));

        // Verify Behaviour
        resultActions0.andExpect(content().string("No"));
        resultActions1.andExpect(content().string("Yes"));
    }

    @Test
    public void verifyThatCreateConfirmedRouteHasCorrectEdgeAndView() throws Exception {

        // Set Up Fixture
        setupFixture();
        Edge expectedEdge = new Edge(3, "A", "C", 8f);
        ArgumentCaptor<Edge> edgeCaptor = forClass(Edge.class);

        // Exercise SUT
        ResultActions resultAction = mockMvc.perform(get("/createConfirmedRoute")
                .param("origin", "A")
                .param("destination", "C")
                .param("distance", "8"));
        Mockito.verify(edgeDAO).save(edgeCaptor.capture());

        // Verify Behaviour
        resultAction.andExpect(view().name("redirect:/"));
        assertThat(edgeCaptor.getValue(), sameBeanAs(expectedEdge));
    }

    @Test
    public void verifyThatDeleteConfirmedTrafficHasCorrectView() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/deleteConfirmedTraffic")
                .param("trafficId", "1"));

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
    }

    @Test
    public void verifyThatConfirmedTrafficUpdateHasCorrectTrafficAndView() throws Exception {

        //Set Up Fixture
        setupFixture();
        Traffic expectedTraffic = new Traffic(1, "A", "B", 11f);
        ArgumentCaptor<Traffic> trafficCaptor = forClass(Traffic.class);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/confirmedTrafficUpdate")
                .param("trafficId", "1")
                .param("origin", "A")
                .param("destination", "B")
                .param("delay", "11"));
        Mockito.verify(trafficDAO).update(trafficCaptor.capture());

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
        assertThat(trafficCaptor.getValue(), sameBeanAs(expectedTraffic));
    }

    @Test
    public void verifyThatProposeTrafficCreationHasCorrectContent() throws Exception {

        // Set Up Fixture
        setupFixture();

        // Exercise SUT
        ResultActions resultActions0 = mockMvc.perform(get("/proposeTrafficCreation")
                .param("origin", "A")
                .param("destination", "B"));
        ResultActions resultActions1 = mockMvc.perform(get("/proposeTrafficCreation")
                .param("origin", "A")
                .param("destination", "C"));

        //Verify Behaviour
        resultActions0.andExpect(content().string("Yes"));
        resultActions1.andExpect(content().string("No"));
    }

    @Test
    public void verifyThatCreateConfirmedTrafficHasCorrectTrafficAndView() throws Exception {

        // Set Up Fixture
        setupFixture();
        Traffic expectedTraffic = new Traffic(3, "A", "C", 5f);
        ArgumentCaptor<Traffic> trafficCaptor = forClass(Traffic.class);

        // Exercise SUT
        ResultActions resultActions = mockMvc.perform(get("/createConfirmedTraffic")
                .param("origin", "A")
                .param("destination", "C")
                .param("delay", "5"));
        Mockito.verify(trafficDAO).save(trafficCaptor.capture());

        // Verify Behaviour
        resultActions.andExpect(view().name("redirect:/"));
        assertThat(trafficCaptor.getValue(), sameBeanAs(expectedTraffic));
    }

    private void setupFixture() {
        Graph graph = new Graph(vertices, edges);

        Mockito.when(startUpDataMigrationService.createGraph()).thenReturn(graph);
        Mockito.when(startUpDataMigrationService.retrieveAllVertices()).thenReturn(vertices);
        Mockito.when(startUpDataMigrationService.retrieveAllEdges()).thenReturn(edges);
        Mockito.when(startUpDataMigrationService.retrieveAllTraffic()).thenReturn(traffic);

        rootController = new RootController(vertexDAO, edgeDAO, trafficDAO, startUpDataMigrationService);
        rootController.home();

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