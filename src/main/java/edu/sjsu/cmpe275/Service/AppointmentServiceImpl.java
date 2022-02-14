package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Config.EmailConfig;
import edu.sjsu.cmpe275.Model.*;
import edu.sjsu.cmpe275.Repository.*;
import edu.sjsu.cmpe275.Util.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    VaccinationRepository vaccinationRepository;

    @Autowired
    VaccinationRecordRepository vaccinationRecordRepository;

    @Autowired
    VaccinationRecordServiceImpl vaccinationRecordService;

    @Autowired
    EmailConfig emailConfig;

    @Override
    public List<Appointment> getAllAppointmentsForUser(Long userId) {
        try {
            List<Appointment> appointments = new ArrayList<>();
            appointmentRepository.findAllByUserId(userId).forEach(appointments::add);
            if (!appointments.isEmpty())
                return appointments;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public List<Appointment> getAllAppointmentsBetween(String dateStart, String dateEnd) {
        try {
            List<Appointment> appointments = new ArrayList<>();
            appointmentRepository.findAllByDateBetween(new Date(dateStart), new Date(dateEnd)).forEach(appointments::add);
            if (!appointments.isEmpty())
                return appointments;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public Appointment bookAppointment(Long userId, String appointmentDate, String appointmentBookedDate, Long clinicId, List<Long> vaccinationIds, List<Integer> shotNumber) {
        try {

            Appointment appointment = new Appointment();
            appointment.setBookedOn(new Timestamp(new Date(appointmentBookedDate).getTime()));
            appointment.setDate(new java.sql.Date(new Date(appointmentDate).getTime()));
            appointment.setCheckIn(false);
            appointment.setAppointmentDateTime(new Timestamp(new Date(appointmentDate).getTime()));
            appointment.setTime(new Time(new Date(appointmentDate).getTime()));
            Optional<User> userData = userRepository.findById(userId);
            if (userData.isPresent())
                appointment.setUser(userData.get());
            Optional<Clinic> clinicData = clinicRepository.findById(clinicId);
            if (clinicData.isPresent())
                appointment.setClinic(clinicData.get());

            List<Vaccination> vaccinations = new ArrayList<Vaccination>();

            for (Long vaccinationId :
                    vaccinationIds) {
                Optional<Vaccination> vaccinationData = vaccinationRepository.findById(vaccinationId);
                if (vaccinationData.isPresent()) {
                    vaccinations.add(vaccinationData.get());
                }
            }
            appointment.setVaccinations(vaccinations);
            Appointment savedAppointment = appointmentRepository.save(appointment);

            for (int i = 0; i < vaccinationIds.size(); i++) {
                Optional<Vaccination> vaccinationData = vaccinationRepository.findById(vaccinationIds.get(i));
                if (vaccinationData.isPresent()) {
                    VaccinationRecord vaccinationRecord = new VaccinationRecord();
                    vaccinationRecord.setVaccination(vaccinationData.get());
                    vaccinationRecord.setTaken(false);
                    if(shotNumber.get(i)==1)
                        vaccinationRecord.setShotDate(new Timestamp(new Date(appointmentDate).getTime()));
                    else
                        vaccinationRecord.setShotDate(new Timestamp(0));
                    vaccinationRecord.setShotNumber(shotNumber.get(i));
                    vaccinationRecord.setAppointment(savedAppointment);
                    if (userData.isPresent())
                        vaccinationRecord.setUser(userData.get());
                    if (clinicData.isPresent())
                        vaccinationRecord.setClinic(clinicData.get());
                    vaccinationRecordRepository.save(vaccinationRecord);
                }
            }
            return appointment;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public Integer getShotNumber(Long vaccinationId, Long userId, String date) {
        try {
            Date appointmentDate = new Date(date);
            List<VaccinationRecord> vaccinationRecords = vaccinationRecordService.getVaccinationRecordsByVaccine(vaccinationId, userId);
            if (!vaccinationRecords.isEmpty()) {
                int vaccinationNumberOfShots = vaccinationRecords.get(0).getVaccination().getNumberOfShots();
                int vaccinationInterval = vaccinationRecords.get(0).getVaccination().getShotInterval();
                int duration = vaccinationRecords.get(0).getVaccination().getDuration();
                VaccinationRecord latestVaccinationRecord = null;
                Date latestShotDate = vaccinationRecords.get(0).getShotDate();;

                int latestShotNumber = 0;
                int index = 0;
                for (VaccinationRecord vaccinationRecord :
                        vaccinationRecords) {
                    if (vaccinationRecord.getShotNumber() > latestShotNumber) {
                        latestShotNumber = vaccinationRecord.getShotNumber();
                        latestVaccinationRecord = vaccinationRecord;
                        if(index>0){
                            latestShotDate = vaccinationRecordService.getNextShotDate(vaccinationRecords.get(index-1).getAppointment().getDate(),vaccinationInterval);
                        }
                    }
                    index++;
                }
                if (vaccinationRecords.size() % vaccinationNumberOfShots != 0) {
                    if (latestVaccinationRecord.getTaken()) {
                        if (appointmentDate.before(vaccinationRecordService.getNextShotDate(latestShotDate, vaccinationInterval))) {
                            return -1;
                        } else {
                            return latestVaccinationRecord.getShotNumber() + 1;
                        }
                    } else {
                        return -1;
                    }
                } else {
                    //duration
                    if (latestVaccinationRecord.getTaken() && duration != 0) {
                        if (appointmentDate.before(vaccinationRecordService.getNextShotDate(latestShotDate, duration))) {
                            return -1;
                        } else {
                            return latestVaccinationRecord.getShotNumber() + 1;
                        }
                    } else if (duration != 0) {
                        return -1;
                    }
                }
            } else {
                return 1;
            }
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return -1;// check once
    }

    @Override
    public Appointment changeAppointment(Long appointmentId, String appointmentDate, String appointmentBookedDate) {
        try {
            Optional<Appointment> appointmentData = appointmentRepository.findById(appointmentId);
            if (appointmentData.isPresent()) {
                appointmentData.get().setBookedOn(new Timestamp(new Date(appointmentBookedDate).getTime()));
                appointmentData.get().setDate(new java.sql.Date(new Date(appointmentDate).getTime()));
                return appointmentRepository.save(appointmentData.get());
            }
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public Appointment cancelAppointment(Long appointmentId) {
        try {
            Optional<Appointment> appointmentData = appointmentRepository.findById(appointmentId);
            if (appointmentData.isPresent()) {

                List<VaccinationRecord> vaccinationRecords = new ArrayList<>();
                vaccinationRecordRepository.findAllByAppointmentId(appointmentId).forEach(vaccinationRecords::add);
                for (VaccinationRecord vaccinationRecord :
                        vaccinationRecords) {
                    vaccinationRecordRepository.deleteById(vaccinationRecord.getId());
                }
                appointmentRepository.deleteById(appointmentId);
                String userEmail = appointmentData.get().getUser().getEmail();
                if(userEmail!=null && !userEmail.isEmpty())
                    new NotificationHelper().sendEmail(emailConfig, "shilpi9soni@gmail.com", userEmail, "", "lololol");
                return appointmentData.get();
            }
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public Appointment getAppointment(Long appointmentId) {
        try {
            Optional<Appointment> appointmentData = appointmentRepository.findById(appointmentId);
            if (appointmentData.isPresent())
                return appointmentData.get();
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public boolean checkInAppointment(Long appointmentId) {
        try {
            List<VaccinationRecord> vaccinationRecordData = vaccinationRecordRepository.findAllByAppointmentId(appointmentId);
            Appointment apt = appointmentRepository.getById(appointmentId);
            apt.setCheckIn(true);
            if (vaccinationRecordData.size() == 0) {
                return false;
            }
            for (VaccinationRecord v : vaccinationRecordData) {
                if (v.getTaken() == true) {
                    return false;
                }
                v.setTaken(true);
                v.setShotDate(apt.getAppointmentDateTime());
                vaccinationRecordRepository.save(v);
            }
            appointmentRepository.save(apt);
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
            return false;
        }
    }

    @Override
    public List<Appointment> getSortedFutureAppointmentsForUSer(Long userId, String currentDate) {
        try {
            List<Appointment> appointments = new ArrayList<>();

            appointmentRepository.findAllByUserIdAndAppointmentDateTimeAfter(userId, new Timestamp(new Date(currentDate).getTime())).forEach(appointments::add);
            Comparator<Appointment> compareByAppointmentDate = (Appointment a1, Appointment a2) ->
                    a1.getAppointmentDateTime().compareTo(a2.getAppointmentDateTime());

                if (!appointments.isEmpty())
                    Collections.sort(appointments, compareByAppointmentDate);

            return appointments;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public List<Appointment> getSortedPastAppointmentsForUSer(Long userId, String currentDate) {
        try {
            List<Appointment> appointments = new ArrayList<>();

            appointmentRepository.findAllByUserIdAndAppointmentDateTimeBefore(userId, new Timestamp(new Date(currentDate).getTime())).forEach(appointments::add);
            Comparator<Appointment> compareByAppointmentDate = (Appointment a1, Appointment a2) ->
                    a1.getAppointmentDateTime().compareTo(a2.getAppointmentDateTime());

            if (!appointments.isEmpty())
                Collections.sort(appointments, compareByAppointmentDate);

            return appointments;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }
}
