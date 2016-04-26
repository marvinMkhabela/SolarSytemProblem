package za.co.discovery.assignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import za.co.discovery.assignment.PathResponseDto;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.dto.PlanetDTO;
import za.co.discovery.assignment.dto.RouteDTO;
import za.co.discovery.assignment.dto.TrafficDTO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;
import za.co.discovery.assignment.services.DTOService;
import za.co.discovery.assignment.services.PathService;

import java.util.List;
import java.util.Optional;

@Controller
public class RootController {

    private PathService pathService;
    private PlanetDAO planetDAO;
    private RouteDAO routeDAO;
    private TrafficDAO trafficDAO;
    private DTOService dtoService;

    @Autowired
    public RootController(PathService pathService, PlanetDAO planetDAO, RouteDAO routeDAO, TrafficDAO trafficDAO,
                          DTOService dtoService) {
        this.pathService = pathService;
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
        this.trafficDAO = trafficDAO;
        this.dtoService = dtoService;
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
        List<Planet> planets = pathService.retrievePlanets();

        model.addAttribute("planet", new PlanetDTO());
        model.addAttribute("planets", planets);

        return "planetManipulation";
    }

    @RequestMapping(value = "/createPlanet", method = RequestMethod.POST)
    public String planetCreate(@ModelAttribute(value = "planet") PlanetDTO planetDTO,
                               RedirectAttributes redirectAttributes) {

        String message;
        if (planetDTO.getName().isEmpty() || planetDTO.getNode().isEmpty()) {
            message = "Failed! Please check that all fields are completed";
        } else {
            Planet planet = dtoService.transcribePlanet(planetDTO);
            if (planet == null) {
                message = "Failed! Planet already exists";
            } else {
                planetDAO.savePlanet(planet);
                message = "Success!";
            }
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/planetManipulation";
    }

    @RequestMapping(value = "/editPlanet", method = RequestMethod.POST)
    public String planetEdit(@ModelAttribute(value = "planet") PlanetDTO planetDTO,
                             RedirectAttributes redirectAttributes) {

        List<Planet> planets = pathService.retrievePlanets();
        String message;
        if (planetDTO.getName().isEmpty()) {
            message = "Failed! Please check that all fields are completed";
        } else {
            Optional<Planet> optionalPlanet = planets.stream()
                    .filter(p -> p.getNode().equals(planetDTO.getNode()))
                    .findAny();
            if (optionalPlanet.isPresent()) {
                Planet targetPlanet = optionalPlanet.get();
                Planet planet = new Planet(targetPlanet.getNode(), planetDTO.getName(), targetPlanet.getNumericIdx());
                planetDAO.updatePlanet(planet);
                message = "Success!";
            } else {
                message = "Failed! Unknown planet";
            }
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/planetManipulation";
    }

    @RequestMapping(value = "/deletePlanet", method = RequestMethod.POST)
    public String planetDelete(@ModelAttribute(value = "planet") PlanetDTO planetDTO,
                               RedirectAttributes redirectAttributes) {

        String message;
        List<Planet> planets = pathService.retrievePlanets();
        if (planetDTO.getName().isEmpty()) {
            message = "Failed! Invalid input combination. Please enter correct name.";
        } else {
            Optional<Planet> planetOptional = planets.stream()
                    .filter(p -> p.getNode().equals(planetDTO.getNode()))
                    .findFirst();

            if (planetOptional.isPresent()) {
                try {
                    planetDAO.deletePlanet(planetOptional.get());
                    message = "Success!";
                } catch (Exception e) {
                    message = "Failed! Cannot delete planet, there are routes/traffic dependant on it.";
                }
            } else {
                message = "Failed! Unknown planet";
            }
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/planetManipulation";
    }

    @RequestMapping("/routeManipulation")
    public String routeManipulation(Model model) {

        List<Planet> planets = pathService.retrievePlanets();
        List<Route> routes = pathService.retrieveRoutes();
        model.addAttribute("route", new RouteDTO());
        model.addAttribute("routes", routes);
        model.addAttribute("planets", planets);

        return "routeManipulation";
    }

    @RequestMapping(value = "/editRoute", method = RequestMethod.POST)
    public String routeEdit(@ModelAttribute(value = "route") RouteDTO routeDTO,
                            RedirectAttributes redirectAttributes) {

        String message;
        if (routeDTO.getDistance() == 0) {
            message = "Failed! Please ensure that all fields are completed, zero distances are not valid.";
        } else {
            if (routeDTO.getOrigin().equals(routeDTO.getDestination())) {
                message = "Failed! Please ensure source and destination are not the same";
            } else {
                Route route = dtoService.transcribeRoute(routeDTO);
                if (route == null) {
                    message = "Failed!";
                } else {
                    routeDAO.updateRoute(route);
                    message = "Success!";
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/routeManipulation";
    }

    @RequestMapping(value = "/createRoute", method = RequestMethod.POST)
    public String routeCreate(@ModelAttribute(value = "route") RouteDTO routeDTO,
                              RedirectAttributes redirectAttributes) {

        String message;

        if (routeDTO.getDistance() == 0) {
            message = "Failed! Please ensure that all fields are completed, zero distances are not valid.";
        } else {
            if (routeDTO.getOrigin().equals(routeDTO.getDestination())) {
                message = "Failed! Please ensure source and destination are not the same";
            } else {
                List<Route> routes = pathService.retrieveRoutes();
                routeDTO.setRouteId(routes.size() + 1);
                Route route = dtoService.transcribeRoute(routeDTO);
                if (route == null) {
                    message = "Failed! Route already exists.";
                } else {
                    routeDAO.saveRoute(route);
                    message = "Success!";
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/routeManipulation";
    }

    @RequestMapping(value = "/deleteRoute", method = RequestMethod.POST)
    public String routeDelete(@ModelAttribute(value = "route") RouteDTO routeDTO,
                              RedirectAttributes redirectAttributes) {

        String message;
        if (routeDTO.getRouteId() == 0) {
            message = "Failed! Route not found.";
        } else {
            if (routeDTO.getOrigin().isEmpty() || routeDTO.getDestination().isEmpty()) {
                message = "Failed! Please confirm delete by entering source and destination.";
            } else if (routeDTO.getOrigin().equals(routeDTO.getDestination())) {
                message = "Failed! Please ensure source and destination are not the same.";
            } else {

                routeDAO.deleteRoute(dtoService.transcribeRoute(routeDTO));
                message = "Success!";
            }
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/routeManipulation";
    }

    @RequestMapping("/trafficManipulation")
    public String trafficManipulation(Model model) {

        List<Planet> planets = pathService.retrievePlanets();
        List<Traffic> traffic = pathService.retrieveTraffic();
        model.addAttribute("trafficDTO", new TrafficDTO());
        model.addAttribute("traffic", traffic);
        model.addAttribute("planets", planets);

        return "trafficManipulation";
    }

    @RequestMapping(value = "/editTraffic", method = RequestMethod.POST)
    public String trafficEdit(@ModelAttribute(value = "trafficDTO") TrafficDTO trafficDTO,
                              RedirectAttributes redirectAttributes) {

        String message;
        if (trafficDTO.getDelay() == 0) {
            message = "Failed! Please ensure that all fields are completed, zero delays are not valid.";
        } else {
            if (trafficDTO.getOrigin().equals(trafficDTO.getDestination())) {
                message = "Failed! Please ensure source and destination are not the same";
            } else {
                Traffic traffic = dtoService.transcribeTraffic(trafficDTO);
                if (traffic == null) {
                    message = "Failed!";
                } else {
                    trafficDAO.updateTraffic(traffic);
                    message = "Success!";
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/trafficManipulation";
    }

    @RequestMapping(value = "/createTraffic", method = RequestMethod.POST)
    public String trafficCreate(@ModelAttribute(value = "trafficDTO") TrafficDTO trafficDTO,
                                RedirectAttributes redirectAttributes) {

        String message;

        if (trafficDTO.getDelay() == 0) {
            message = "Failed! Please ensure that all fields are completed, zero delays are not valid.";
        } else {
            if (trafficDTO.getOrigin().equals(trafficDTO.getDestination())) {
                message = "Failed! Please ensure source and destination are not the same";
            } else {
                List<Traffic> trafficList = pathService.retrieveTraffic();
                trafficDTO.setTrafficId(trafficList.size() + 1);
                Traffic traffic = dtoService.transcribeTraffic(trafficDTO);
                if (traffic == null) {
                    message = "Failed! Route already exists.";
                } else {
                    trafficDAO.saveTraffic(traffic);
                    message = "Success!";
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/trafficManipulation";
    }

    @RequestMapping(value = "/deleteTraffic", method = RequestMethod.POST)
    public String trafficDelete(@ModelAttribute(value = "trafficDTO") TrafficDTO trafficDTO,
                                RedirectAttributes redirectAttributes) {

        String message;
        if (trafficDTO.getTrafficId() == 0) {
            message = "Failed! Traffic not found.";
        } else {
            if (trafficDTO.getOrigin().isEmpty() || trafficDTO.getDestination().isEmpty()) {
                message = "Failed! Please confirm delete by entering source and destination.";
            } else if (trafficDTO.getOrigin().equals(trafficDTO.getDestination())) {
                message = "Failed! Please ensure source and destination are not the same.";
            } else {

                trafficDAO.deleteTraffic(dtoService.transcribeTraffic(trafficDTO));
                message = "Success!";
            }
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/trafficManipulation";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
