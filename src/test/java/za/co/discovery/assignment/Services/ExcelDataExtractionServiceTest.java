package za.co.discovery.assignment.Services;

import org.junit.Before;
import org.junit.Test;
import za.co.discovery.assignment.Models.Planet;
import za.co.discovery.assignment.Models.Route;
import za.co.discovery.assignment.Models.Traffic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class ExcelDataExtractionServiceTest {

    ExcelDataExtractionService excelDataExtractionService;

    @Before
    public void initializeTest() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("worksheetTest.xlsx").getFile());
        excelDataExtractionService = new ExcelDataExtractionService(file);
    }

    @Test
    public void readSheet1ExtractsTheAppropriateDataFromSheet() {

        //Set up fixture
        ArrayList<Planet> expectedPlanets = new ArrayList<Planet>();
        Planet planet1 = new Planet("A", "Earth");
        expectedPlanets.add(planet1);
        Planet planet2 = new Planet("B", "Moon");
        expectedPlanets.add(planet2);

        //Exercise SUT
        try {
            ArrayList<Planet> actualPlanets = excelDataExtractionService.readSheet1();

            //Verify Behaviour
            assertThat(expectedPlanets, sameBeanAs(actualPlanets));
        } catch (IOException e) {
            System.out.println("Caught IOException in read sheet 1 test");
        }


    }

    @Test
    public void readSheet2ExtractsTheAppropriateDataFromSheet() {

        //Set up fixture
        ArrayList<Route> expectedRoutes = new ArrayList<Route>();
        Route route1 = new Route(1, "A", "B", 0.44f);
        expectedRoutes.add(route1);
        Route route2 = new Route(2, "A", "C", 1.89f);
        expectedRoutes.add(route2);

        //Exercise SUT
        try {
            ArrayList<Route> actualRoutes = excelDataExtractionService.readSheet2();

            //Verify Behaviour
            assertThat(expectedRoutes, sameBeanAs(actualRoutes));
        } catch (IOException e) {
            System.out.println("Caught IOException in read sheet 2 test");
        }

    }

    @Test
    public void readSheet3ExtractsTheAppropriateDataFromSheet() {

        //Set up fixture
        ArrayList<Traffic> expectedTraffic = new ArrayList<Traffic>();
        Traffic traffic1 = new Traffic(1, "A", "B", 0.30f);
        expectedTraffic.add(traffic1);
        Traffic traffic2 = new Traffic(2, "A", "C", 0.90f);
        expectedTraffic.add(traffic2);

        //Exercise SUT
        try {
            ArrayList<Traffic> actualTraffic = excelDataExtractionService.readSheet3();

            //Verify Behaviour
            assertThat(expectedTraffic, sameBeanAs(actualTraffic));

        } catch (IOException e) {
            System.out.println("Caught IOException in read sheet 3 test");
        }


    }


}