package za.co.discovery.assignment.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "planets")
public class Planet implements Serializable {

    @Id
    @Column
    private String node;
    @Column
    private String name;

    public Planet(){

    }

    public Planet(String node, String name){
        this.node = node;
        this.name = name;
    }

    public String getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

}
