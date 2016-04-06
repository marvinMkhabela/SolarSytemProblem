package za.co.discovery.assignment.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "routes")
@Table
public class Route implements Serializable {

    @Id
    @Column
    private int routeId;
    @Column
    private String origin;
    @Column
    private String destination;
    @Column
    private float distance;

    public Route() {

    }

    public Route(int routeId, String origin, String destination, float distance) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

}
