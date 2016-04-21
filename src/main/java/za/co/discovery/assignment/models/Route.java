package za.co.discovery.assignment.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "routes")
public class Route implements Serializable{

    @Id
    @Column
    private int routeId;
    @OneToOne(fetch = FetchType.EAGER)
    private Planet origin;
    @OneToOne(fetch = FetchType.EAGER)
    private Planet destination;
    @Column
    private double distance;

    public Route(int routeId, Planet origin, Planet destination, double distance){

        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public int getRouteId(){
        return routeId;
    }

    public Planet getOrigin(){
        return origin;
    }

    public Planet getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }
}
