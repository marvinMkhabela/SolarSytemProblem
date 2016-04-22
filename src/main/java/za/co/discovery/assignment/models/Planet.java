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
    @Column
    private int numericIdx;

    protected Planet() {

    }

    public Planet(String node, String name, int numericIdx) {
        this.node = node;
        this.name = name;
        this.numericIdx = numericIdx;
    }

    public String getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

    public int getNumericIdx() {
        return numericIdx;
    }

}
