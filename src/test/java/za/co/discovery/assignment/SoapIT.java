package za.co.discovery.assignment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import za.co.discovery.assignment.Configuration.DataSourceConfig;
import za.co.discovery.assignment.Configuration.PersistenceConfig;
import za.co.discovery.assignment.Configuration.WebServiceConfig;
import za.co.discovery.assignment.DAO.EdgeDAO;
import za.co.discovery.assignment.DAO.TrafficDAO;
import za.co.discovery.assignment.DAO.VertexDAO;
import za.co.discovery.assignment.Endpoints.PathEndpoint;
import za.co.discovery.assignment.Services.StartUpDataMigrationService;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebServiceConfig.class, PathEndpoint.class, PathRepositoryCreation.class,
        DataSourceConfig.class, PersistenceConfig.class, EdgeDAO.class, VertexDAO.class,
        TrafficDAO.class, StartUpDataMigrationService.class},
        loader = AnnotationConfigContextLoader.class)
public class SoapIT {

    @Autowired
    private PathEndpoint pathEndpoint;

    @Test
    public void verifyThatPathEndPointReturnsCorrectPath() {

        // Set Up Fixture
        GetPathRequest request = new GetPathRequest();
        request.setName("Jupiter");
        GetPathResponse expectedResponse = new GetPathResponse();

        Pathdto pathdto = new Pathdto();
        pathdto.setName("Jupiter");
        pathdto.setPath("Travel from Earth, to Jupiter");
        expectedResponse.setPath(pathdto);

        // Exercise SUT
        GetPathResponse actualResponse = pathEndpoint.getPath(request);

        // Verify Behaviour
        assertThat(actualResponse, sameBeanAs(expectedResponse));

    }
}
