package za.co.discovery.assignment.dto;

public class TrafficDTO {

    private int trafficId;
    private String origin;
    private String destination;
    private double delay;

    public int getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(int trafficId) {
        this.trafficId = trafficId;
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

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}
