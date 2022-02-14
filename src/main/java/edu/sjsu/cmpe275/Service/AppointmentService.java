package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.Vaccination;

import java.util.Date;
import java.util.List;

public interface AppointmentService {

    public List<Appointment> getAllAppointmentsBetween(String dateStart, String dateEnd);
    public List<Appointment> getAllAppointmentsForUser(Long userId);
    public Appointment bookAppointment(Long userId, String appointmentDate, String appointmentBookedDate, Long clinicId, List<Long> vaccinationIds, List<Integer> shotNumber);
    public Appointment changeAppointment(Long appointmentId, String appointmentDate, String appointmentBookedDate);
    public Integer getShotNumber(Long vaccinationId, Long userId, String appointmentDate);
    public Appointment cancelAppointment(Long appointmentId);
    public Appointment getAppointment(Long appointmentId);
    public boolean checkInAppointment(Long appointmentId);
    public List<Appointment> getSortedFutureAppointmentsForUSer(Long userId, String currentDate);
    public List<Appointment> getSortedPastAppointmentsForUSer(Long userId, String currentDate);

}
