package za.co.discovery.assignment.DTO;

import za.co.discovery.assignment.Models.Vertex;

import java.util.List;

public class PathDTO {

    private String name;
    private String path;

    public PathDTO() {
    }

    public PathDTO(String rawPath, List<Vertex> vertices) {
        String[] pathAttributes = decipherRawPath(rawPath, vertices);
        name = pathAttributes[0];
        path = pathAttributes[1];
    }

    public String[] decipherRawPath(String rawPath, List<Vertex> vertices) {

        String[] tempPath = rawPath.split(",");
        String[] pathAttributes = new String[2];
        String namedPath = new String("Travel from ");
        int n = tempPath.length;
        String name = findVertexNameByNode(tempPath[n - 1], vertices);
        pathAttributes[0] = name;


        for (String s : tempPath) {
            namedPath += findVertexNameByNode(s, vertices) + ", to ";
        }
        n = namedPath.length();
        namedPath = namedPath.substring(0, n - 5);
        pathAttributes[1] = namedPath;


        return pathAttributes;
    }

    public String findVertexNameByNode(String node, List<Vertex> vertices) {

        String targetVertex = new String();
        for (Vertex v : vertices) {
            if (v.getNode().equals(node)) {
                targetVertex = v.getName();
            }
        }

        return targetVertex;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
