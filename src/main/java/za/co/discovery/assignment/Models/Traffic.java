package za.co.discovery.assignment.Models;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "traffic")
@Table
public class Traffic implements Serializable{

    @Id
    @Column
    private int routeId;
    @Column
    private String origin;
    @Column
    private String destination;
    @Column
    private float trafficDelay;

    public Traffic() {

    }

    public Traffic(int routeId, String origin, String destination, float trafficDelay) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.trafficDelay = trafficDelay;
    }

    public int getRoute() {
        return routeId;
    }

    public void setRoute(int routeId) {
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

    public float getTrafficDelay() {
        return trafficDelay;
    }

    public void setTrafficDelay(float trafficDelay) {
        this.trafficDelay = trafficDelay;
    }


}
