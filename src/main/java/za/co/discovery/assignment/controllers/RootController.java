package za.co.discovery.assignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.services.PathService;

import java.util.List;

@Controller
public class RootController {

    private PathService pathService;
    private PlanetDAO planetDAO;
    private RouteDAO routeDAO;
    private TrafficDAO trafficDAO;

    @Autowired
    public RootController(PathService pathService, PlanetDAO planetDAO, RouteDAO routeDAO, TrafficDAO trafficDAO) {
        this.pathService = pathService;
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
        this.trafficDAO = trafficDAO;
    }

    @RequestMapping("/")
    public String homePage() {

        return "home";
    }

    @RequestMapping("/planets")
    public String planetPage(Model model) {

        List<Planet> planets = pathService.retrievePlanets();
        model.addAttribute("planets", planets);
        return "planets";
    }

    @RequestMapping("/routes")
    public String routePage(Model model) {

        List<Route> routes = pathService.retrieveRoutes();
        model.addAttribute("routes", routes);
        return "routes";
    }

    @RequestMapping("/traffic")
    public String trafficPage(Model model) {

        List<Traffic> traffic = pathService.retrieveTraffic();
        model.addAttribute("traffic", traffic);
        return "traffic";
    }

    @RequestMapping("/paths")
    public String pathPage(Model model) {

        List<PathResponseDto> paths = pathService.refreshPaths();
        model.addAttribute("paths", paths);
        return "paths";
    }

    @RequestMapping("/planetManipulation")
    public String planetManipulation(Model model) {
        Planet planet = new Planet("node", "name", 0);
        model.addAttribute("planet", planet);
        return "planetManipulation";
    }

    @RequestMapping(value = "/editPlanet", method = RequestMethod.POST)
    public String planetEdit(@ModelAttribute(value = "planet") Planet planet) {

        System.out.println(planet.getNode() + "\t" + planet.getName());

        List<Planet> planets = pathService.retrievePlanets();
        boolean flag = true;
        for (Planet p : planets) {
            if (p.getName().equals(planet.getName())) {
                flag = false;
                break;
            }
        }

        Planet savePlanet = new Planet(planet.getNode(), planet.getName(), 0);

        if (flag) {
            planetDAO.savePlanet(savePlanet);
            return "paths";
        } else {
            return "home";
        }

    }

}
