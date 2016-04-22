package za.co.discovery.assignment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import za.co.discovery.assignment.configurations.WebServiceConfig;
import za.co.discovery.assignment.endpoints.PathEndPoint;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.services.PathService;
import za.co.discovery.assignment.services.PathTranscriptionService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebServiceConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class SoapTest {

    private PathService pathService = Mockito.mock(PathService.class);
    private PathTranscriptionService pathTranscriptionService = Mockito.mock(PathTranscriptionService.class);
    private List<Planet> planets;
    private List<Route> routes;
    private List<Traffic> traffic;
    private List<PathResponseDto> paths;

    @Before
    public void initializeTest() {
        planets = Arrays.asList(new Planet("A", "Earth", 0), new Planet("B", "Moon", 1));
        routes = Collections.singletonList(new Route(1, planets.get(0), planets.get(1), 0.2));
        traffic = Collections.singletonList(new Traffic(1, planets.get(0), planets.get(1), 0.3));
        PathResponseDto firstPath = new PathResponseDto();
        firstPath.setName("Earth");
        firstPath.setPath("Travel from Earth");
        PathResponseDto secondPath = new PathResponseDto();
        secondPath.setName("Moon");
        secondPath.setPath("Travel from Earth, to Moon");
        paths = Arrays.asList(firstPath, secondPath);
    }

    @Test
    public void verifyThatTheCorrectPathsAreReturnedByEndPoint() {

        // Set Up Fixture
        GetPathRequest request = new GetPathRequest();
        request.setName("Earth");
        GetPathResponse expectedResponse = new GetPathResponse();
        expectedResponse.setPath(paths.get(0));
        String[] rawPaths = {"0", "0,1"};

        Mockito.when(pathService.refreshPaths()).thenReturn(paths);
        Mockito.when(pathTranscriptionService.decipherRawPaths(rawPaths, planets)).thenReturn(paths);
        Mockito.when(pathService.findPath("Earth")).thenReturn(paths.get(0));
        Mockito.when(pathService.findPath("Moon")).thenReturn(paths.get(1));

        // Exercise SUT
        PathEndPoint pathEndPoint = new PathEndPoint(pathService);
        GetPathResponse actualResponse = pathEndPoint.getPath(request);

        // Verify Behaviour
        assertThat(actualResponse, sameBeanAs(expectedResponse));

    }

}
