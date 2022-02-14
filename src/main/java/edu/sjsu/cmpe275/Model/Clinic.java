package edu.sjsu.cmpe275.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Entity
@Table(name = "clinic")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "clinic_name", unique = true)
    private String clinicName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "open")
    private Integer open;

    @Column(name = "close")
    private Integer close;

    @Column(name = "business_hours")
    private Integer businessHours;

    @Column(name = "num_physicians")
    private Integer numPhysicians;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"user","clinic","vaccinations"})
    private List<Appointment> appointments;

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Integer getNumPhysicians() {
        return numPhysicians;
    }

    public void setNumPhysicians(Integer numPhysicians) {
        this.numPhysicians = numPhysicians;
    }

    public Integer getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(Integer businessHours) {
        this.businessHours = businessHours;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}