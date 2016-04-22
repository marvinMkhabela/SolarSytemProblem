package za.co.discovery.assignment.state;

import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;

import java.util.List;

public class Graph {

    private List<Planet> planets;
    private List<Route> routes;
    private List<Traffic> traffic;

    public Graph(List<Planet> planets, List<Route> routes, List<Traffic> traffic) {
        this.planets = planets;
        this.routes = routes;
        this.traffic = traffic;
    }

    public void appendTrafficData() {

        for (Traffic t : traffic) {
            routes.stream().filter(r -> r.getRouteId() == t.getTrafficId()).forEach(r -> {
                r.appendTrafficDelay(t.getDelay());
            });
        }
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Traffic> getTraffic() {
        return traffic;
    }
}
