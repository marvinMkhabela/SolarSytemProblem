package za.co.discovery.assignment.Services;

import org.junit.Before;
import org.junit.Test;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;

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
        ArrayList<Vertex> expectedVertexes = new ArrayList<Vertex>();
        Vertex vertex1 = new Vertex("A", "Earth");
        expectedVertexes.add(vertex1);
        Vertex vertex2 = new Vertex("B", "Moon");
        expectedVertexes.add(vertex2);

        //Exercise SUT
        try {
            ArrayList<Vertex> actualVertexes = excelDataExtractionService.readSheet1();

            //Verify Behaviour
            assertThat(expectedVertexes, sameBeanAs(actualVertexes));
        } catch (IOException e) {
            System.out.println("Caught IOException in read sheet 1 test");
        }


    }

    @Test
    public void readSheet2ExtractsTheAppropriateDataFromSheet() {

        //Set up fixture
        ArrayList<Edge> expectedEdges = new ArrayList<Edge>();
        Edge edge1 = new Edge(1, "A", "B", 0.44f);
        expectedEdges.add(edge1);
        Edge edge2 = new Edge(2, "A", "C", 1.89f);
        expectedEdges.add(edge2);

        //Exercise SUT
        try {
            ArrayList<Edge> actualEdges = excelDataExtractionService.readSheet2();

            //Verify Behaviour
            assertThat(expectedEdges, sameBeanAs(actualEdges));
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