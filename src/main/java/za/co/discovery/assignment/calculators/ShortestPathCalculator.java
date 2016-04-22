package za.co.discovery.assignment.calculators;

import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.state.Graph;

import java.util.List;

public class ShortestPathCalculator {

    private Graph graph;

    public ShortestPathCalculator(Graph graph) {
        this.graph = graph;
    }

    public String[] calculateShortestPathFromEarth() {

        List<Planet> planets = graph.getPlanets();
        List<Route> routes = graph.getRoutes();
        int n = planets.size();

        String[] rawPaths = new String[n];
        double[] distances = new double[n];
        boolean[] availability = new boolean[n];

        for (int i = 0; i < n; i++) {
            rawPaths[i] = Integer.toString(0);
            distances[i] = Double.MAX_VALUE;
            availability[i] = true;
        }

        Planet targetPlanet;
        Planet neighbour;
        double alternateDistance;
        String alternatePath;
        int minimumIdx = 0;
        distances[minimumIdx] = 0;
        boolean notComplete = true;

        while (notComplete) {

            targetPlanet = planets.get(minimumIdx);
            availability[minimumIdx] = false;

            for (Route r : routes) {

                String source = r.getOrigin().getNode();
                if (source.equals(targetPlanet.getNode())) {
                    neighbour = r.getDestination();
                    alternateDistance = distances[minimumIdx] + r.getTotalTravelTime();
                    alternatePath = rawPaths[minimumIdx] + "," + Integer.toString(neighbour.getNumericIdx());

                    if (alternateDistance <= distances[neighbour.getNumericIdx()]) {
                        rawPaths[neighbour.getNumericIdx()] = alternatePath;
                        distances[neighbour.getNumericIdx()] = alternateDistance;
                    }
                } else {
                    System.out.println("\n\n\n" + " Source : " + r.getOrigin().getName() + " Destination :" + r.getDestination().getName());
                }

            }

            minimumIdx = findMinimum(distances, availability);
            notComplete = arrayOrStatement(availability);
        }

        return rawPaths;
    }

    private int findMinimum(double[] nums, boolean[] booleans) {

        final int notFound = -1;
        int minimumIdx = notFound;
        double minimum = Double.MAX_VALUE;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= minimum && booleans[i]) {
                minimum = nums[i];
                minimumIdx = i;
            }
        }

        return minimumIdx;
    }

    private boolean arrayOrStatement(boolean[] booleans) {

        boolean result = false;
        for (boolean b : booleans) {
            result |= b;
        }

        return result;
    }
}
