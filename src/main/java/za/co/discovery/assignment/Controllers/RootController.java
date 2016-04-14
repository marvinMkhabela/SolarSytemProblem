package za.co.discovery.assignment.Controllers;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import za.co.discovery.assignment.DAO.EdgeDAO;
import za.co.discovery.assignment.DAO.TrafficDAO;
import za.co.discovery.assignment.DAO.VertexDAO;
import za.co.discovery.assignment.DTO.PathDTO;
import za.co.discovery.assignment.Models.Edge;
import za.co.discovery.assignment.Models.Traffic;
import za.co.discovery.assignment.Models.Vertex;
import za.co.discovery.assignment.Services.Graph;
import za.co.discovery.assignment.Services.ShortestPathCalculator;
import za.co.discovery.assignment.Services.StartUpDataMigrationService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RootController {

    private SessionFactory sessionFactory;
    List<PathDTO> paths = new ArrayList<PathDTO>();
    List<Vertex> vertices;
    List<Edge> edges;
    List<Traffic> traffic;
    private VertexDAO vertexDAO;
    private EdgeDAO edgeDAO;
    private TrafficDAO trafficDAO;
    private StartUpDataMigrationService startUpDataMigrationService;

    @Autowired
    public RootController(SessionFactory sessionFactory, VertexDAO vertexDAO, EdgeDAO edgeDAO, TrafficDAO trafficDAO) {

        this.sessionFactory = sessionFactory;
        this.vertexDAO = vertexDAO;
        this.edgeDAO = edgeDAO;
        this.trafficDAO = trafficDAO;
        startUpDataMigrationService = new StartUpDataMigrationService(sessionFactory, vertexDAO, edgeDAO, trafficDAO);
    }

    @RequestMapping("/")
    public String home() {

        Graph graph = startUpDataMigrationService.createGraph();
        vertices = startUpDataMigrationService.retrieveAllVertices();
        traffic = startUpDataMigrationService.retrieveAllTraffic();
        edges = startUpDataMigrationService.retrieveAllEdges();
        graph.appendTrafficData(traffic);

        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator(graph);
        String[] rawPaths = shortestPathCalculator.calculatePathsFromEarth();

        for (String s : rawPaths) {
            paths.add(new PathDTO(s, vertices));
        }

        return "redirect:menu";
    }

    @RequestMapping("/menu")
    public String mainMenu() {

        return "menu";
    }

    @RequestMapping("/query")
    public String query(Model model) {
        model.addAttribute("vertices", vertices);
        return "queryPath";
    }

    @RequestMapping(value = "/selectedDestination", method = RequestMethod.GET)
    public ModelAndView selectionEvaluation(@RequestParam(value = "selectedDestination", required = true) String selectedDestination,
                                            Model model) {

        Vertex v;
        int vertexMarker = -1;

        for (int i = 0; i < vertices.size(); i++) {
            v = vertices.get(i);
            if (v.getName().equals(selectedDestination)) {
                vertexMarker = i;
            }
        }

        PathDTO targetPath = paths.get(vertexMarker);

        if (!targetPath.getName().equals(selectedDestination)) {
            targetPath = new PathDTO();
            targetPath.setName(selectedDestination);
            targetPath.setPath("Disjoint node, no path found!");
        }

        model.addAttribute("choiceMade", "true");
        model.addAttribute("targetPath", targetPath);

        ModelAndView mav = new ModelAndView("queryPath");
        mav.addObject("choiceMade", "true");
        mav.addObject("vertices", vertices);
        mav.addObject("targetPath", targetPath);

        return mav;
    }

    @RequestMapping("/update")
    public String updateNode(Model model) {

        model.addAttribute("vertices", vertices);
        return "updatePlanet";
    }

    @RequestMapping(value = "/updateNode", method = RequestMethod.GET)
    public ResponseEntity updateNode(@RequestParam(value = "updateNode", required = true) String updateNode) {

        String responseString = new String();
        for (Vertex v : vertices) {
            if (v.getNode().equals(updateNode)) {
                responseString = v.getName();
            }
        }

        return new ResponseEntity(responseString, HttpStatus.OK);

    }

    @RequestMapping(value = "/updateNewName", method = RequestMethod.GET)
    public String persistUpdate(@RequestParam(value = "updatedName", required = true) String updatedName,
                                @RequestParam(value = "updateNode", required = true) String updateNode) {

        Vertex targetVertex = null;
        for (Vertex v : vertices) {
            if (v.getNode().equals(updateNode)) {
                targetVertex = v;
            }
        }

        targetVertex.setName(updatedName);
        vertexDAO.update(targetVertex);

        return "redirect:/";
    }

    @RequestMapping("/create")
    public String createVertex() {

        return "createPlanet";
    }

    @RequestMapping(value = "/createPlanet", method = RequestMethod.GET)
    public ResponseEntity EvaluateEntry(@RequestParam(value = "creationNode", required = true) String creationNode,
                                        @RequestParam(value = "creationName", required = true) String creationName) {

        String clashes = "No";
        Vertex comparisonVertex = new Vertex(creationNode, creationName);
        for (Vertex v : vertices) {
            if ((v.equals(comparisonVertex)) || (v.getName().equals(creationName)) || (v.getNode().equals(creationNode))) {
                clashes = "Yes";
            }
        }

        return new ResponseEntity(clashes, HttpStatus.OK);
    }

    @RequestMapping(value = "/persistEntry")
    public String storeEntry(@RequestParam(value = "creationNode", required = true) String creationNode,
                             @RequestParam(value = "creationName", required = true) String creationName) {

        vertexDAO.save(new Vertex(creationNode, creationName));

        return "redirect:/";
    }

    @RequestMapping("/delete")
    public String deletePlanet(Model model) {

        model.addAttribute("vertices", vertices);
        return "deletePlanet";
    }

    @RequestMapping(value = "/deletePlanet", method = RequestMethod.GET)
    public String confirmedDelete(@RequestParam(value = "planetName", required = true) String planetName) {

        Vertex vertex = null;
        for (Vertex v : vertices) {
            if (v.getName().equals(planetName)) {
                vertex = v;
            }
        }

        vertexDAO.delete(vertex.getNode());

        return "redirect:/";
    }

    @RequestMapping("/deleteRoute")
    public String deleteRoute(Model model) {

        model.addAttribute("edges", edges);

        return "deleteRoute";
    }

    @RequestMapping(value = "/confirmRouteDeletion", method = RequestMethod.GET)
    public String routeDeletion(@RequestParam(value = "deletionId", required = true) int id) {

        edgeDAO.delete(id);
        return "redirect:/";
    }

    @RequestMapping("updateRoute")
    public String routeUpdate(Model model) {

        model.addAttribute("edges", edges);
        return "updateRoute";
    }

    @RequestMapping(value = "/confirmRouteUpdate", method = RequestMethod.GET)
    public String confirmRouteUpdate(@RequestParam(value = "selectedId", required = true) int selectedId,
                                     @RequestParam(value = "origin", required = true) String origin,
                                     @RequestParam(value = "destination", required = true) String destination,
                                     @RequestParam(value = "distance", required = true) String distance) {

        float distanceFloat = Float.parseFloat(distance);
        Edge edge = new Edge(selectedId, origin, destination, distanceFloat);
        edgeDAO.update(edge);

        return "redirect:/";
    }

    @RequestMapping("/createRoute")
    public String createRoute(Model model) {

        model.addAttribute("vertices", vertices);
        return "createRoute";
    }

    @RequestMapping(value = "/proposeRouteCreation", method = RequestMethod.GET)
    public ResponseEntity proposeRouteCreation(@RequestParam(value = "origin", required = true) String origin,
                                               @RequestParam(value = "destination", required = true) String destination) {

        String exists = "No";
        for (Edge e : edges) {
            if (e.getOrigin().equals(origin) && e.getDestination().equals(destination)) {
                exists = "Yes";
            }
        }
        return new ResponseEntity(exists, HttpStatus.OK);
    }

    @RequestMapping(value = "/createConfirmedRoute", method = RequestMethod.GET)
    public String createConfirmedRoute(@RequestParam(value = "origin", required = true) String origin,
                                       @RequestParam(value = "destination", required = true) String destination,
                                       @RequestParam(value = "distance", required = true) String distance) {

        float distanceFloat = Float.parseFloat(distance);
        int newID = edges.size() + 1;
        Edge edge = new Edge(newID, origin, destination, distanceFloat);
        edgeDAO.save(edge);

        return "redirect:/";
    }

    @RequestMapping("/deleteTraffic")
    public String deleteTraffic(Model model) {

        model.addAttribute("traffic", traffic);
        return "deleteTraffic";
    }

    @RequestMapping(value = "/deleteConfirmedTraffic", method = RequestMethod.GET)
    public String deleteConfirmedTraffic(@RequestParam(value = "trafficId", required = true) int trafficId) {

        trafficDAO.delete(trafficId);
        return "redirect:/";
    }

    @RequestMapping("/updateTraffic")
    public String updateTraffic(Model model) {

        model.addAttribute("traffic", traffic);
        return "updateTraffic";
    }

    @RequestMapping(value = "/confirmedTrafficUpdate", method = RequestMethod.GET)
    public String confirmedTrafficUpdate(@RequestParam(value = "trafficId", required = true) int trafficId,
                                         @RequestParam(value = "origin", required = true) String origin,
                                         @RequestParam(value = "destination", required = true) String destination,
                                         @RequestParam(value = "delay", required = true) String delay) {

        float delayFloat = Float.parseFloat(delay);
        Traffic updateTraffic = new Traffic(trafficId, origin, destination, delayFloat);
        trafficDAO.update(updateTraffic);
        return "redirect:/";
    }

    @RequestMapping("/createTraffic")
    public String createTraffic(Model model) {

        model.addAttribute("vertices", vertices);
        return "createTraffic";
    }

    @RequestMapping(value = "/proposeTrafficCreation", method = RequestMethod.GET)
    public ResponseEntity proposeTrafficCreation(@RequestParam(value = "origin", required = true) String origin,
                                                 @RequestParam(value = "destination", required = true) String destination) {

        String exists = "No";
        for (Traffic t : traffic) {
            if ((t.getOrigin().equals(origin)) && (t.getDestination().equals(destination))) {
                exists = "Yes";
            }
        }
        return new ResponseEntity(exists, HttpStatus.OK);
    }

    @RequestMapping(value = "/createConfirmedTraffic", method = RequestMethod.GET)
    public String createConfirmedTraffic(@RequestParam(value = "origin", required = true) String origin,
                                         @RequestParam(value = "destination", required = true) String destination,
                                         @RequestParam(value = "delay", required = true) String delay) {

        float delayFloat = Float.parseFloat(delay);
        int newID = traffic.size() + 1;
        Traffic createdTraffic = new Traffic(newID, origin, destination, delayFloat);
        trafficDAO.save(createdTraffic);

        return "redirect:/";
    }

}
