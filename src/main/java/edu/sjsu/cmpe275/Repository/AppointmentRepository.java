package edu.sjsu.cmpe275.Repository;

import edu.sjsu.cmpe275.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByUserId(Long UserId);
    List<Appointment> findAllByDateBefore(Date date);
    List<Appointment> findAllByUserIdAndAppointmentDateTimeBefore(Long userId, Timestamp timestamp);
    List<Appointment> findAllByDateAfter(Date date);
    List<Appointment> findAllByUserIdAndAppointmentDateTimeAfter(Long userId, Timestamp timestamp);
    List<Appointment> findAllByDateBetween(Date dateStart, Date dateEnd);
//    List<Appointment> findAllByUserAndDateBetween(Date dateStart, Date dateEnd)

//    @Query(value = "select a from Appointment a where c.clinicName=:clinicName")
    @Query(value = "select a from Appointment a where a.date between :dateStart and :dateEnd and a.user.id =:id")
    List<Appointment> findAllByUserIdAndDateBetween(Long id, Date dateStart, Date dateEnd);

    @Query(value = "select a from Appointment a where a.date between :dateStart and :dateEnd and a.clinic.id=:id")
    List<Appointment> findAllByClinicIdAndDateBetween(Long id, Date dateStart, Date dateEnd);

    @Query(value = "select a from Appointment a where a.appointmentDateTime=:t")
    List<Appointment> findAllAppointmentByDateTime(Timestamp t);
}