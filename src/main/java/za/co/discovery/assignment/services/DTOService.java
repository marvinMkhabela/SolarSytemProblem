package za.co.discovery.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.dao.PlanetDAO;
import za.co.discovery.assignment.dao.RouteDAO;
import za.co.discovery.assignment.dao.TrafficDAO;
import za.co.discovery.assignment.dto.PlanetDTO;
import za.co.discovery.assignment.dto.RouteDTO;
import za.co.discovery.assignment.dto.TrafficDTO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.Traffic;

import java.util.List;

@Service
public class DTOService {

    private PlanetDAO planetDAO;
    private RouteDAO routeDAO;
    private TrafficDAO trafficDAO;

    @Autowired
    public DTOService(PlanetDAO planetDAO, RouteDAO routeDAO, TrafficDAO trafficDAO) {
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
        this.trafficDAO = trafficDAO;
    }

    public Planet transcribePlanet(PlanetDTO planetDTO) {

        if (planetDTO.getNode() == null || planetDTO.getName() == null) {
            return null;
        } else {
            List<Planet> planets = planetDAO.retrieveAll();
            String dtoNode = planetDTO.getNode();
            int idx = planets.size();
            boolean contained = false;

            for (Planet planet : planets) {
                if (dtoNode.equals(planet.getNode())) {
                    contained = true;
                }
            }

            if (contained) {
                return null;
            } else {
                return new Planet(dtoNode, planetDTO.getName(), idx);
            }
        }
    }

    public Route transcribeRoute(RouteDTO routeDTO) {

        if (routeDTO.getOrigin() == null || routeDTO.getDestination() == null) {
            return null;
        } else {
            List<Planet> planets = planetDAO.retrieveAll();
            Planet origin = null;
            String dtoOrigin = routeDTO.getOrigin();
            Planet destination = null;
            String dtoDestination = routeDTO.getDestination();

            for (Planet planet : planets) {
                if (dtoOrigin.equals(planet.getName())) {
                    origin = planet;
                } else if (dtoDestination.equals(planet.getName())) {
                    destination = planet;
                }
            }

            if (origin == null || destination == null) {
                return null;
            } else {
                return new Route(routeDTO.getRouteId(), origin, destination, routeDTO.getDistance());
            }
        }
    }

    public Traffic transcribeTraffic(TrafficDTO trafficDTO) {

        if (trafficDTO.getOrigin() == null || trafficDTO.getDestination() == null) {
            return null;
        } else {
            List<Planet> planets = planetDAO.retrieveAll();
            Planet origin = null;
            String dtoOrigin = trafficDTO.getOrigin();
            Planet destination = null;
            String dtoDestination = trafficDTO.getDestination();

            for (Planet planet : planets) {
                if (dtoOrigin.equals(planet.getName())) {
                    origin = planet;
                } else if (dtoDestination.equals(planet.getName())) {
                    destination = planet;
                }
            }

            if (origin == null || destination == null) {
                return null;
            } else {
                return new Traffic(trafficDTO.getTrafficId(), origin, destination, trafficDTO.getDelay());
            }
        }
    }
}
