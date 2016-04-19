package za.co.discovery.assignment.Services;

import org.springframework.stereotype.Service;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Vertex;

import java.util.List;

@Service
public class ShortestPathCalculator {

    private Graph graph;
    private String[] paths;

    protected ShortestPathCalculator() {
    }

    public ShortestPathCalculator(Graph graph) {
        this.graph = graph;
        paths = new String[graph.getVertices().size()];
    }

    public String[] calculatePathsFromEarth() {

        List<Vertex> vertices = graph.getVertices();
        List<Edge> edges = graph.getEdges();
        int n = vertices.size();

        float[] distances = new float[n];
        boolean[] availability = new boolean[n];

        for (int i = 0; i < n; i++) {
            distances[i] = (float) Integer.MAX_VALUE;
            availability[i] = true;
            paths[i] = "A";
        }

        int minimumIdx = 0;
        distances[minimumIdx] = 0f;
        availability[minimumIdx] = false;

        Vertex targetVertex;
        int destinationIdx;
        String alternatePath;
        float alternateDistance;
        boolean runLoop = arrayOr(availability);

        while (runLoop) {

            targetVertex = vertices.get(minimumIdx);
            availability[minimumIdx] = false;

            for (Edge neighbour : edges) {

                if (neighbour.getOrigin().equals(targetVertex.getNode())) {
                    destinationIdx = findVertexIndexByNode(neighbour.getDestination());
                    alternateDistance = distances[minimumIdx] + neighbour.getTotalTravelTime();

                    if (destinationIdx != -1) {
                        if (alternateDistance < distances[destinationIdx]) {
                            alternatePath = new String(paths[minimumIdx]);
                            alternatePath = alternatePath + "," + vertices.get(destinationIdx).getNode();
                            paths[destinationIdx] = alternatePath;
                            distances[destinationIdx] = alternateDistance;
                        }
                    }

                }
            }
            minimumIdx = findMinimum(distances, availability);
            runLoop = arrayOr(availability);
        }

        return paths;
    }

    public int findMinimum(float[] values, boolean[] availability) {

        int minimumIdx = 1;
        float minimum = Float.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if ((values[i] <= minimum) && (availability[i])) {
                minimumIdx = i;
                minimum = values[i];
            }
        }

        return minimumIdx;
    }

    public boolean arrayOr(boolean[] booleans) {

        boolean res = false;
        for (boolean bool : booleans) {
            res = (res || bool);
        }
        return res;
    }

    public int findVertexIndexByNode(String node) {

        int idx = -1;
        Vertex vertex;
        List<Vertex> vertices = graph.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            vertex = vertices.get(i);
            if (vertex.getNode().equals(node)) {
                idx = i;
            }
        }
        return idx;
    }
}
