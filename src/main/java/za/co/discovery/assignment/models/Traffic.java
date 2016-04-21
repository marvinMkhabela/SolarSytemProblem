package za.co.discovery.assignment.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "traffic")
public class Traffic implements Serializable{

    @Id
    @Column
    private int trafficId;
    @OneToOne(fetch = FetchType.EAGER)
    private Planet origin;
    @OneToOne(fetch = FetchType.EAGER)
    private Planet destination;
    @Column
    private double delay;

    public Traffic(int trafficId, Planet origin, Planet destination, double delay) {

        this.trafficId = trafficId;
        this.origin = origin;
        this.destination = destination;
        this.delay = delay;
    }

    public int getTrafficId(){
        return trafficId;
    }

    public Planet getOrigin(){
        return origin;
    }

    public Planet getDestination() {
        return destination;
    }

    public double getDelay() {
        return delay;
    }

}
