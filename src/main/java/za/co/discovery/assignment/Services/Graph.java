package za.co.discovery.assignment.Services;

import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;

import java.util.ArrayList;

public class Graph {

    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;

    protected Graph() {
    }

    public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public void appendTrafficData(ArrayList<Traffic> traffic) {

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

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }
}
