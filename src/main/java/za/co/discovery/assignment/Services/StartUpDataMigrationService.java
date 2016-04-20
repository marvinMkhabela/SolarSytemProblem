package za.co.discovery.assignment.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.DAO.EdgeDAO;
import za.co.discovery.assignment.DAO.TrafficDAO;
import za.co.discovery.assignment.DAO.VertexDAO;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class StartUpDataMigrationService {

    private VertexDAO vertexDAO;
    private EdgeDAO edgeDAO;
    private TrafficDAO trafficDAO;

    @Autowired
    public StartUpDataMigrationService(VertexDAO vertexDAO, EdgeDAO edgeDAO, TrafficDAO trafficDAO) {
        this.vertexDAO = vertexDAO;
        this.edgeDAO = edgeDAO;
        this.trafficDAO = trafficDAO;
    }

    public void readXLSXFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        ExcelDataExtractionService excelDataExtractionService = new ExcelDataExtractionService(new File(classLoader.getResource("worksheet.xlsx").getFile()));

        try {
            List<Vertex> vertices = excelDataExtractionService.readSheet1();
            for (Vertex v : vertices) {
                vertexDAO.save(v);
            }
            List<Edge> edges = excelDataExtractionService.readSheet2();
            for (Edge e : edges) {
                edgeDAO.save(e);
            }
            List<Traffic> traffic = excelDataExtractionService.readSheet3();
            for (Traffic t : traffic) {
                trafficDAO.save(t);
            }
        } catch (IOException e) {
            System.out.println("Caught IOException");
        }
    }

    public Graph createGraph() {
        List<Vertex> vertices = retrieveAllVertices();
        List<Edge> edges = retrieveAllEdges();
        List<Traffic> traffic = retrieveAllTraffic();

        Graph graph = new Graph(vertices, edges);
        graph.appendTrafficData(traffic);

        return graph;
    }

    public List<Vertex> retrieveAllVertices() {
        return vertexDAO.retrieveAll();
    }

    public List<Edge> retrieveAllEdges() {
        return edgeDAO.retrieveAll();
    }

    public List<Traffic> retrieveAllTraffic() {
        return trafficDAO.retrieveAll();
    }
}
