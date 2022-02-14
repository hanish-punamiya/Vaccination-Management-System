package edu.sjsu.cmpe275.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "vaccination_record")
@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class VaccinationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "shot_number")
    private Integer shotNumber;

    @Column(name = "shot_date")
    private Timestamp shotDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vaccination_id")
    private Vaccination vaccination;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "clinic_id")
    @JsonIgnoreProperties({"appointments"})
    private Clinic clinic;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
        @JsonIgnoreProperties({"appointments","vaccinationHistory","address"})
    private User user;

    @Column(name = "taken")
    private Boolean taken;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    @JsonIgnoreProperties({"user","clinic","vaccinations"})
    private Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Vaccination getVaccination() {
        return vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public Timestamp getShotDate() {
        return shotDate;
    }

    public void setShotDate(Timestamp shotDate) {
        this.shotDate = shotDate;
    }

    public Integer getShotNumber() {
        return shotNumber;
    }

    public void setShotNumber(Integer shotNumber) {
        this.shotNumber = shotNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}