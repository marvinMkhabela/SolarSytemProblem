package za.co.discovery.assignment;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.Configuration.DataSourceConfig;
import za.co.discovery.assignment.Configuration.PersistenceConfig;
import za.co.discovery.assignment.DAO.TrafficDAO;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;
import za.co.discovery.assignment.Services.Graph;
import za.co.discovery.assignment.Services.StartUpDataMigrationService;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.mock;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class, PersistenceConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class PathRepositoryCreationIT {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    @Test
    public void verifyTheValidityOfInitDataMethod() {

        // SetUp Fixture
        TrafficDAO trafficDAO = mock(TrafficDAO.class);
        StartUpDataMigrationService startUpDataMigrationService = mock(StartUpDataMigrationService.class);

        Vertex vertex0 = new Vertex("A", "Earth");
        Vertex vertex1 = new Vertex("B", "Moon");
        List<Vertex> vertices = Arrays.asList(vertex0, vertex1);
        Edge edge0 = new Edge(1, "A", "B", 2f);
        List<Edge> edges = Arrays.asList(edge0);
        Graph graph = new Graph(vertices, edges);
        List<Traffic> traffic = Arrays.asList(new Traffic(1, "A", "B", 3f));

        Pathdto expectedPath = new Pathdto();
        expectedPath.setName("Moon");
        expectedPath.setPath("Travel from Earth, to Moon");

        Mockito.when(startUpDataMigrationService.createGraph()).thenReturn(graph);
        Mockito.when(startUpDataMigrationService.retrieveAllVertices()).thenReturn(vertices);
        Mockito.when(trafficDAO.retrieveAll()).thenReturn(traffic);
        PathRepositoryCreation pathRepositoryCreation = new PathRepositoryCreation(sessionFactory, txManager);

        // Exercise SUT
        pathRepositoryCreation.initData();
        Pathdto actualPath = pathRepositoryCreation.findPath("Moon");

        // Verify Behaviour
        assertThat(actualPath, sameBeanAs(expectedPath));

    }

}