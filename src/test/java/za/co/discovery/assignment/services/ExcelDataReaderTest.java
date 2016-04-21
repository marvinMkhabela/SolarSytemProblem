package za.co.discovery.assignment.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import za.co.discovery.assignment.configuration.TestResourceConfig;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExcelDataReader.class, TestResourceConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class ExcelDataReaderTest {

    @Autowired
    private ExcelDataReader excelDataReader;
    private List<Planet> planets = Arrays.asList(new Planet("A", "Earth"), new Planet("B", "Moon"));


    @Test
    public void verifyThatReadPlanetsReturnsCorrectPlanets() {

        try {
            // Exercise SUT
            List<Planet> actualPlanets = excelDataReader.readPlanets();

            // Verify Behaviour
            assertThat(actualPlanets, sameBeanAs(planets));
        } catch (Exception e) {
            System.out.println("IO Exception in planet reader");
            System.exit(1);
        }
    }

    @Test
    public void verifyThatReadRoutesReturnsTheCorrectRoutes() {

        try {
            // Set Up Fixture
            List<Route> expectedRoutes = Collections.singletonList(new Route(1, planets.get(0), planets.get(1), 0.44));

            // Exercise SUT
            List<Route> actualRoutes = excelDataReader.readRoutes(planets);

            // Verify Behaviour
            assertThat(actualRoutes, sameBeanAs(expectedRoutes));
        } catch (Exception e) {
            System.out.println("IO Exception in route reader");
            System.exit(1);
        }

    }

    @Test
    public void verifyThatReadTrafficReturnsTheCorrectTraffic() {

        try {
            // Set Up Fixture
            List<Traffic> expectedTraffic = Collections.singletonList(new Traffic(1, planets.get(0), planets.get(1), 0.3));

            // Exercise SUT
            List<Traffic> actualTraffic = excelDataReader.readTraffic(planets);

            // Verify Behaviour
            assertThat(actualTraffic, sameBeanAs(expectedTraffic));
        } catch (Exception e) {
            System.out.println("IO Exception in traffic reader");
            System.exit(1);
        }

    }
}