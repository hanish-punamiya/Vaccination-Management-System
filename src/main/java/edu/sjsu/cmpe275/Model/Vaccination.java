package edu.sjsu.cmpe275.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Entity
@Table(name = "vaccination")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Vaccination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "vaccination_diseases",
            joinColumns = @JoinColumn(name = "vaccination_id"),
            inverseJoinColumns = @JoinColumn(name = "diseases_id"))
    private List<Disease> diseases;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "number_of_shots")
    private Integer numberOfShots;

    @Column(name = "shot_interval")
    private Integer shotInterval;

    @Column(name = "duration")
    private Integer duration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getShotInterval() {
        return shotInterval;
    }

    public void setShotInterval(Integer shotInterval) {
        this.shotInterval = shotInterval;
    }

    public Integer getNumberOfShots() {
        return numberOfShots;
    }

    public void setNumberOfShots(Integer numberOfShots) {
        this.numberOfShots = numberOfShots;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}