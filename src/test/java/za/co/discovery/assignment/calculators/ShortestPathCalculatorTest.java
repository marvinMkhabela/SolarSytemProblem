package za.co.discovery.assignment.calculators;

import org.junit.Test;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.state.Graph;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class ShortestPathCalculatorTest {

    @Test
    public void verifyThatTheCalculatorComputesTheCorrectPathsFromEarth() {

        // Set Up Fixture
        Planet earth = new Planet("A", "Earth", 0);
        Planet moon = new Planet("B", "Moon", 1);
        Planet jupiter = new Planet("C", "Jupiter", 2);
        List<Planet> planets = Arrays.asList(earth, moon, jupiter);

        Route firstRoute = new Route(1, earth, moon, 2);
        Route secondRoute = new Route(2, moon, jupiter, 3);
        Route thirdRoute = new Route(3, earth, jupiter, 5);
        List<Route> routes = Arrays.asList(firstRoute, secondRoute, thirdRoute);

        Traffic firstTraffic = new Traffic(1, earth, moon, 2);
        Traffic secondTraffic = new Traffic(2, moon, jupiter, 3);
        List<Traffic> traffic = Arrays.asList(firstTraffic, secondTraffic);

        Graph graph = new Graph(planets, routes, traffic);
        graph.appendTrafficData();
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator(graph);
        String[] expectedRawPaths = {"0", "0,1", "0,2"};

        // Exercise SUT
        String[] actualRawPaths = shortestPathCalculator.calculateShortestPathFromEarth();

        // Verify Behaviour
        assertThat(actualRawPaths, sameBeanAs(expectedRawPaths));

    }
}