package za.co.discovery.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.calculators.ShortestPathCalculator;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.state.Graph;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class PathService {

    private PlatformTransactionManager txManager;
    private PathTranscriptionService pathTranscriptionService;
    private ExcelDataReader excelDataReader;
    private PlanetDAO planetDAO;
    private TrafficDAO trafficDAO;
    private RouteDAO routeDAO;

    @Autowired
    public PathService(@Qualifier("transactionManager") PlatformTransactionManager txManager, TrafficDAO trafficDAO,
                       PathTranscriptionService pathTranscriptionService, RouteDAO routeDAO, PlanetDAO planetDAO,
                       ExcelDataReader excelDataReader) {

        this.txManager = txManager;
        this.trafficDAO = trafficDAO;
        this.pathTranscriptionService = pathTranscriptionService;
        this.routeDAO = routeDAO;
        this.planetDAO = planetDAO;
        this.excelDataReader = excelDataReader;
    }

    @PostConstruct
    public void initAllData() {

        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    List<Planet> planets = excelDataReader.readPlanets();
                    List<Route> routes = excelDataReader.readRoutes(planets);
                    List<Traffic> traffic = excelDataReader.readTraffic(planets);

                    planets.forEach(planetDAO::savePlanet);
                    traffic.forEach(trafficDAO::saveTraffic);
                    routes.forEach(routeDAO::saveRoute);

                } catch (Exception e) {
                    System.out.println("Application failed to start");
                    System.exit(2);
                }
            }
        });
    }

    public List<PathResponseDto> refreshPaths() {

        List<Planet> planets = this.retrievePlanets();
        List<Route> routes = this.retrieveRoutes();
        List<Traffic> traffic = this.retrieveTraffic();
        Graph graph = new Graph(planets, routes, traffic);
        graph.appendTrafficData();

        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator(graph);
        String[] rawPaths = shortestPathCalculator.calculateShortestPathFromEarth();

        return pathTranscriptionService.decipherRawPaths(rawPaths, planets);
    }

    public PathResponseDto findPath(String name) {

        List<PathResponseDto> paths = refreshPaths();
        PathResponseDto targetPath = null;
        for (PathResponseDto p : paths) {
            if (p.getName().equals(name)) {
                targetPath = p;
            }
        }

        String targetName = targetPath.getName();
        if (!targetName.equals(name)) {
            targetPath.setName(name);
            targetPath.setPath("Isolated planet!");
        }

        return targetPath;
    }

    public List<Planet> retrievePlanets() {
        return planetDAO.retrieveAll();
    }

    public List<Route> retrieveRoutes() {
        return routeDAO.retrieveAll();
    }

    public List<Traffic> retrieveTraffic() {
        return trafficDAO.retrieveAll();
    }
}
