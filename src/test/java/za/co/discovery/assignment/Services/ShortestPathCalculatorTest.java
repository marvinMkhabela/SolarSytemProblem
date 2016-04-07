package za.co.discovery.assignment.Services;

import org.junit.Test;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Vertex;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;

public class ShortestPathCalculatorTest {

    @Test
    public void verifyThatAlgorithmComputesTheShortestPathsFromEarth() {

        // Set up fixture
        List<Vertex> vertices = new ArrayList<Vertex>();
        vertices.add(new Vertex("A", "Earth"));
        vertices.add(new Vertex("B", "Moon"));
        vertices.add(new Vertex("C", "Mars"));
        vertices.add(new Vertex("D", "Mercury"));

        List<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge(1, "A", "B", 2f, 3f));
        edges.add(new Edge(2, "B", "C", 3f, 5f));
        edges.add(new Edge(3, "C", "D", 5f, 7f));
        edges.add(new Edge(4, "A", "D", 3f, 5f));

        Graph graph = new Graph(vertices, edges);
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator(graph);
        String[] expectedPaths = new String[4];
        expectedPaths[0] = new String("A");
        expectedPaths[1] = new String("A,B");
        expectedPaths[2] = new String("A,B,C");
        expectedPaths[3] = new String("A,D");

        // Exercise SUT
        String[] actualPaths = shortestPathCalculator.calculatePathsFromEarth();

        // Verify Behaviour
        assertThat(actualPaths, sameBeanAs(expectedPaths));
    }

    @Test
    public void verifyThatminimumFunctionReturnsTheIndexOfTheTrueMinimum() {

        // Set up fixture
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        float[] values = {999f, 1002f, 1001f, 1000f};
        boolean[] booleans = {true, true, true, true};
        int expectedResult = 3;

        // Exercise SUT
        int actualResult = shortestPathCalculator.findMinimum(values, booleans);

        // Verify Behaviour
        assertThat(actualResult, is(expectedResult));

    }

    @Test
    public void verifyThatArrayOrReturnsAggregatedOrOperation() {

        // Set up fixture
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator();
        boolean[] booleans = {false, true, false};
        boolean expectedResult = true;

        // Exercise SUT
        boolean actualResult = shortestPathCalculator.arrayOr(booleans);

        // Verify Behaviour
        assertThat(actualResult, is(expectedResult));
    }

}