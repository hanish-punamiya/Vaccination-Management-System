package edu.sjsu.cmpe275.Controller;

import edu.sjsu.cmpe275.Config.EmailConfig;
import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Repository.AppointmentRepository;
import edu.sjsu.cmpe275.Service.AppointmentServiceImpl;
import edu.sjsu.cmpe275.Util.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentServiceImpl appointmentService;

    @Autowired
    EmailConfig emailConfig;

    @PostMapping("/user")
    @Transactional
    public ResponseEntity<Object> getAllAppointmentsForUser(@RequestBody Long userId) {
        try {
            List<Appointment> userAppointments = appointmentService.getAllAppointmentsForUser(userId);
            if (userAppointments.isEmpty())
                return new ResponseEntity<Object>(userAppointments, HttpStatus.NOT_FOUND);
            else if (userAppointments == null)
                return new ResponseEntity<Object>(userAppointments, HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity<Object>(userAppointments, HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/book")
    @Transactional
    public ResponseEntity<Object> bookAppointment(@RequestBody Map<String, Object> requestBody) {
        try {

            Long userId = ((Number) requestBody.get("userId")).longValue();
            System.out.println("userId" + userId);

            Long clinicId = ((Number) requestBody.get("clinicId")).longValue();
            System.out.println(clinicId);


            String appointmentDate = (String) requestBody.get("appointmentDate");
            System.out.println(appointmentDate);

            String appointmentBookedDate = (String) requestBody.get("appointmentBookedDate");
            System.out.println(appointmentBookedDate);

            List<Integer> oldVaccinationIds = (List<Integer>) requestBody.get("vaccinationIds");
            List<Integer> shotNumber = (List<Integer>) requestBody.get("shotNumber");
            System.out.println(shotNumber);


            List<Long> vaccinationIds = new ArrayList<Long>();

            for (Integer id :
                    oldVaccinationIds) {
                Long newId = new Long(id);
                vaccinationIds.add(newId);
            }
            System.out.println(vaccinationIds);


            Appointment bookedAppointment = appointmentService.bookAppointment(userId, appointmentDate, appointmentBookedDate, clinicId, vaccinationIds, shotNumber);
            if (bookedAppointment != null) {
                String email = bookedAppointment.getUser().getEmail();
                if (email != null && !email.isEmpty())
                    new NotificationHelper().sendEmail(emailConfig, "shilpi9soni@gmail.com", email, "Appointment Cancelled", "Hi, your appointment with id: " + bookedAppointment.getId() + "is cancelled");
                return new ResponseEntity<Object>(bookedAppointment, HttpStatus.OK);
            } else
                return new ResponseEntity<Object>(bookedAppointment, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/shot_number")
    @Transactional
    public ResponseEntity<Object> getShotNumber(@RequestBody Map<String, Object> requestBody) {
        try {

            Long vaccinationId = ((Number) requestBody.get("vaccinationId")).longValue();
            Long userId = ((Number) requestBody.get("userId")).longValue();
            String date = (String) requestBody.get("date");


            Integer shotNumber = appointmentService.getShotNumber(vaccinationId, userId, date);
            if (shotNumber != -1)
                return new ResponseEntity<Object>(shotNumber, HttpStatus.OK);
            else
                return new ResponseEntity<Object>(shotNumber, HttpStatus.FORBIDDEN);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel")
    @Transactional
    public ResponseEntity<Object> cancelAppointment(@RequestBody Map<String, Object> requestBody) {
        try {
            Long appointmentId = ((Number) requestBody.get("appointmentId")).longValue();

            Appointment cancelledAppointment = appointmentService.cancelAppointment(appointmentId);
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (cancelledAppointment != null) {
                if (appointment.isPresent()) {
                    String email = appointment.get().getUser().getEmail();
                    if (email != null && !email.isEmpty())
                        new NotificationHelper().sendEmail(emailConfig, "shilpi9soni@gmail.com", email, "Appointment Cancelled", "Hi, your appointment with id: " + appointment.get().getId() + "is cancelled");
                }
                return new ResponseEntity<Object>(cancelledAppointment, HttpStatus.OK);
            } else
                return new ResponseEntity<Object>("Some error occurred", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get_one")
    @Transactional
    public ResponseEntity<Object> getAppointment(@RequestBody Map<String, Object> requestBody) {
        try {
            Long appointmentId = ((Number) requestBody.get("appointmentId")).longValue();
            Appointment appointment = appointmentService.cancelAppointment(appointmentId);
            if (appointment != null)
                return new ResponseEntity<Object>(appointment, HttpStatus.OK);
            else
                return new ResponseEntity<Object>("Appointment not found", HttpStatus.NOT_FOUND);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/change")
    @Transactional
    public ResponseEntity<Object> changeAppointment(@RequestBody Map<String, Object> requestBody) {
        try {
            Long appointmentId = ((Number) requestBody.get("appointmentId")).longValue();
            String newAppointmentDate = (String) requestBody.get("newAppointmentDate");
            String currentDate = (String) requestBody.get("currentDate");

            Appointment appointment = appointmentService.changeAppointment(appointmentId, newAppointmentDate, currentDate);
            Optional<Appointment> newAppointment = appointmentRepository.findById(appointmentId);
            if (newAppointment != null) {
                String email = newAppointment.get().getUser().getEmail();
                if (email != null && !email.isEmpty())
                    new NotificationHelper().sendEmail(emailConfig, "shilpi9soni@gmail.com", email, "Appointment Cancelled", "Hi, your appointment with id: " + newAppointment.get().getId() + "has successfully changed");
                return new ResponseEntity<Object>(appointment, HttpStatus.OK);
            } else
                return new ResponseEntity<Object>("Appointment not found", HttpStatus.NOT_FOUND);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/checkIn")
    @Transactional
    public ResponseEntity<Boolean> checkInAppointment(@RequestBody Map<String, Object> requestBody) {
        try {

            Long appointmentId = ((Number) requestBody.get("appointmentId")).longValue();
            boolean isCheckedIn = false;
            isCheckedIn = appointmentService.checkInAppointment(appointmentId);
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
            if (isCheckedIn == true) {
                if (appointment.isPresent()) {
                    String email = appointment.get().getUser().getEmail();
                    if (email != null && !email.isEmpty())
                        new NotificationHelper().sendEmail(emailConfig, "shilpi9soni@gmail.com", email, "Appointment Cancelled", "Hi, your appointment with id: " + appointment.get().getId() + "is Checked-in");
                }
                return new ResponseEntity<Boolean>(isCheckedIn, HttpStatus.OK);
            } else
                return new ResponseEntity<Boolean>(isCheckedIn, HttpStatus.BAD_REQUEST);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/past")
    @Transactional
    public ResponseEntity<Object> getSortedPastAppointmentsForUSer(@RequestBody Map<String, Object> requestBody) {
        try {

            Long userId = ((Number) requestBody.get("userId")).longValue();
            String currentDate = (String) requestBody.get("currentDate");

            return new ResponseEntity<Object>(appointmentService.getSortedPastAppointmentsForUSer(userId, currentDate), HttpStatus.OK);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/future")
    @Transactional
    public ResponseEntity<Object> getSortedFutureAppointmentsForUSer(@RequestBody Map<String, Object> requestBody) {
        try {

            Long userId = ((Number) requestBody.get("userId")).longValue();
            String currentDate = (String) requestBody.get("currentDate");

            return new ResponseEntity<Object>(appointmentService.getSortedFutureAppointmentsForUSer(userId, currentDate), HttpStatus.OK);

        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
