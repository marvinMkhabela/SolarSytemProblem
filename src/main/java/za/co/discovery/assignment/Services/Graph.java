package za.co.discovery.assignment.Services;

import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;

import java.util.List;

public class Graph {

    private List<Vertex> vertices;
    private List<Edge> edges;

    protected Graph() {
    }

    public Graph(List<Vertex> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public void appendTrafficData(List<Traffic> traffic) {

        float placeholder;
        for (Traffic t : traffic) {
            for (Edge e : edges) {
                if (e.getEdgeId() == t.getRoute()) {
                    placeholder = e.getDistance() + t.getTrafficDelay();
                    e.setTotalTravelTime(placeholder);
                }
            }
        }
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
}
