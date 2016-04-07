package za.co.discovery.assignment.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "edges")
@Table
public class Edge implements Serializable {

    @Id
    @Column
    private int edgeId;
    @Column
    private String origin;
    @Column
    private String destination;
    @Column
    private float distance;
    @Column
    private float totalTravelTime;

    public Edge() {

    }

    public Edge(int edgeId, String origin, String destination, float distance) {
        this.edgeId = edgeId;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public Edge(int edgeId, String origin, String destination, float distance, float totalTravelTime) {
        this.edgeId = edgeId;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.totalTravelTime = totalTravelTime;
    }

    public int getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(int edgeId) {
        this.edgeId = edgeId;
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

    public float getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(float totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

}
