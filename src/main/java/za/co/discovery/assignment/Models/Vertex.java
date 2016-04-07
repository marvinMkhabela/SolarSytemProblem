package za.co.discovery.assignment.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "vertices")
@Table
public class Vertex implements Serializable {

    @Id
    @Column
    private String node;
    @Column
    private String name;

    public Vertex() {

    }

    public Vertex(String node, String name) {
        this.node = node;
        this.name = name;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
