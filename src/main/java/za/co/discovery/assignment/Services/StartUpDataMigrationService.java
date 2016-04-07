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
import java.util.ArrayList;
import java.util.List;

@Service
public class StartUpDataMigrationService {

    private ExcelDataExtractionService excelDataExtractionService;
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
        excelDataExtractionService = new ExcelDataExtractionService(new File(classLoader.getResource("worksheet.xlsx").getFile()));

        try {
            ArrayList<Vertex> vertices = excelDataExtractionService.readSheet1();
            for (Vertex v : vertices) {
                vertexDAO.save(v);
            }
            ArrayList<Edge> edges = excelDataExtractionService.readSheet2();
            for (Edge e : edges) {
                edgeDAO.save(e);
            }
            ArrayList<Traffic> traffic = excelDataExtractionService.readSheet3();
            for (Traffic t : traffic) {
                trafficDAO.save(t);
            }
        } catch (IOException e) {
            System.out.println("Caught IO Exception");
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
