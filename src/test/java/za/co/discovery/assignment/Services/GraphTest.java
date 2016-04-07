package za.co.discovery.assignment.Services;

import org.junit.Test;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;

import java.util.ArrayList;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class GraphTest {

    @Test
    public void verifyThatUpdateTrafficUpdatesEdgesCorrectly() {

        // Set up fixture
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        ArrayList<Traffic> traffic = new ArrayList<Traffic>();

        edges.add(new Edge(1, "A", "B", 5f));
        edges.add(new Edge(2, "B", "C", 7f));
        traffic.add(new Traffic(1, "A", "B", 3f));
        traffic.add(new Traffic(2, "B", "C", 2f));
        traffic.add(new Traffic(3, "C", "D", 11f));
        Graph actualGraph = new Graph(vertices, edges);

        ArrayList<Edge> expectedEdges = new ArrayList<Edge>();
        expectedEdges.add(new Edge(1, "A", "B", 5f, 8f));
        expectedEdges.add(new Edge(2, "B", "C", 7f, 9f));
        Graph expectedGraph = new Graph(vertices, expectedEdges);

        // Exercise SUT
        actualGraph.appendTrafficData(traffic);

        //Verify Behaviour
        assertThat(actualGraph, sameBeanAs(expectedGraph));
    }
}