package edu.sjsu.cmpe275.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@XmlRootElement
@Entity
@Table(name = "appointment")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id" )
    @JsonIgnoreProperties({"vaccinationHistory","appointments"})
    private User user;

    @Column(name = "date" )
    private Date date;

    @Column(name = "time" )
    private Time time;

    @Column(name = "check_in" )
    private Boolean checkIn = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clinic_id")
    @JsonIgnoreProperties({"appointments"})
    private Clinic clinic;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "appointment_vaccinations",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "vaccinations_id"))
    private List<Vaccination> vaccinations;

    @Column(name = "booked_on")
    private Timestamp bookedOn;

    @Column(name = "appointment_date_time")
    private Timestamp appointmentDateTime;

    public Timestamp getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(Timestamp appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Timestamp getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(Timestamp bookedOn) {
        this.bookedOn = bookedOn;
    }

    public List<Vaccination> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<Vaccination> vaccinations) {
        this.vaccinations = vaccinations;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Boolean getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Boolean checkIn) {
        this.checkIn = checkIn;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}