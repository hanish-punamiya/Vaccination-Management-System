package edu.sjsu.cmpe275.Helper.Model;

import edu.sjsu.cmpe275.Model.Appointment;

import java.util.List;

public class SystemRecord {
    public List<Appointment> totalAppointments;
    public List<Appointment> noShowAppointments;
    public double noShowRate = 0;
}
