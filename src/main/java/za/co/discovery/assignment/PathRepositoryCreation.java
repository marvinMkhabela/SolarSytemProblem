package za.co.discovery.assignment;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import za.co.discovery.assignment.DAO.EdgeDAO;
import za.co.discovery.assignment.DAO.TrafficDAO;
import za.co.discovery.assignment.DAO.VertexDAO;
import za.co.discovery.assignment.DTO.PathDTO;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;
import za.co.discovery.assignment.Services.Graph;
import za.co.discovery.assignment.Services.ShortestPathCalculator;
import za.co.discovery.assignment.Services.StartUpDataMigrationService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PathRepositoryCreation {

    private List<PathDTO> paths = new ArrayList<PathDTO>();
    private SessionFactory sessionFactory;
    protected PlatformTransactionManager txManager;

    @Autowired
    public PathRepositoryCreation(SessionFactory sessionFactory, @Qualifier("transactionManager") PlatformTransactionManager txManager) {
        this.sessionFactory = sessionFactory;
        this.txManager = txManager;
    }

    public PathRepositoryCreation() {
    }

    @PostConstruct
    public void initData() {

        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                VertexDAO vertexDAO = new VertexDAO(sessionFactory);
                EdgeDAO edgeDAO = new EdgeDAO(sessionFactory);
                TrafficDAO trafficDAO = new TrafficDAO(sessionFactory);

                StartUpDataMigrationService startUpDataMigrationService = new StartUpDataMigrationService(sessionFactory, vertexDAO, edgeDAO, trafficDAO);
                startUpDataMigrationService.readXLSXFile();
                Graph graph = startUpDataMigrationService.createGraph();
                List<Vertex> vertices = startUpDataMigrationService.retrieveAllVertices();
                List<Traffic> traffic = trafficDAO.retrieveAll();
                graph.appendTrafficData(traffic);
                ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator(graph);
                String[] rawPaths = shortestPathCalculator.calculatePathsFromEarth();

                for (String s : rawPaths) {
                    paths.add(new PathDTO(s, vertices));
                }
            }
        });

    }

    public Pathdto findPath(String name) {
        Assert.assertNotNull(name);

        PathDTO result = null;
        for (PathDTO path : this.paths) {
            if (name.equals(path.getName())) {
                result = path;
            }
        }

        if (result == null) {
            result = new PathDTO("Http 404", Arrays.asList(new Vertex("404", "Not Found")));
        }

        Pathdto res = new Pathdto();
        res.setName(result.getName());
        res.setPath(result.getPath());

        return res;
    }

}