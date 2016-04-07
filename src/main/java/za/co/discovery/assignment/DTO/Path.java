package za.co.discovery.assignment.DTO;

import za.co.discovery.assignment.Models.Vertex;

import java.util.Stack;

public class Path {

    private Stack<Vertex> path = new Stack<Vertex>();

    public Path() {
    }

    public Stack<Vertex> getPath() {
        return path;
    }

    public void setPath(Stack<Vertex> path) {
        this.path = path;
    }

    public void addToPath(Vertex v) {
        path.push(v);
    }

    public Stack<Vertex> removePath() {

        Vertex vertex = new Vertex();
        while (!path.isEmpty()) {
            vertex = path.pop();
        }
        path.add(vertex);
        return path;
    }

    public void replacePath(Path path) {
        this.path = removePath();
        Stack<Vertex> vertexStack = path.getPath();
        Stack<Vertex> temporaryVertex = new Stack<Vertex>();

        while (!vertexStack.isEmpty()) {
            temporaryVertex.push(vertexStack.pop());
        }

        while (!temporaryVertex.isEmpty()) {
            this.path.push(temporaryVertex.pop());
        }
    }

}
