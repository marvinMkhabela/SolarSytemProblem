package za.co.discovery.assignment.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    public float getTrafficDelay() {
        return trafficDelay;
    }

    public void setTrafficDelay(float trafficDelay) {
        this.trafficDelay = trafficDelay;
    }


}
