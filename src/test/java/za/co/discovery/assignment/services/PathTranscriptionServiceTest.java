package za.co.discovery.assignment.services;

import org.junit.Test;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.models.Planet;

import java.util.Arrays;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class PathTranscriptionServiceTest {

    @Test
    public void verifyIfDecipherPathExtractsTheCorrectPaths() {

        // Set Up Fixture
        String[] rawPaths = {"0", "0,1", "0,2"};
        Planet earth = new Planet("A", "Earth", 0);
        Planet moon = new Planet("B", "Moon", 1);
        Planet jupiter = new Planet("C", "Jupiter", 2);
        List<Planet> planets = Arrays.asList(earth, moon, jupiter);

        PathResponseDto firstPath = new PathResponseDto();
        firstPath.setName("Earth");
        firstPath.setPath("Travel from Earth");
        PathResponseDto secondPath = new PathResponseDto();
        secondPath.setName("Moon");
        secondPath.setPath("Travel from Earth, to Moon");
        PathResponseDto thirdPath = new PathResponseDto();
        thirdPath.setName("Jupiter");
        thirdPath.setPath("Travel from Earth, to Jupiter");
        List<PathResponseDto> expectedPaths = Arrays.asList(firstPath, secondPath, thirdPath);

        PathTranscriptionService pathTranscriptionService = new PathTranscriptionService();

        // Exercise SUT
        List<PathResponseDto> actualPaths = pathTranscriptionService.decipherRawPaths(rawPaths, planets);

        // Verify Behaviour
        assertThat(actualPaths, sameBeanAs(expectedPaths));

    }
}