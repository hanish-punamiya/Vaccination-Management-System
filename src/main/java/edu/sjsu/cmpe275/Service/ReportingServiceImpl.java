package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Helper.Model.PatientRecord;
import edu.sjsu.cmpe275.Helper.Model.SystemRecord;
import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportingServiceImpl {
    @Autowired
    AppointmentRepository appointmentRepository;
    public PatientRecord getPatientReport(Long userId, String startDate, String endDate, String currDate){

        System.out.println("inside get patient report " + userId);
        Long id = userId;
//        Date sDate = new Date(startDate);
//        Date eDate = new Date(endDate);
        Date sDate = new java.sql.Date(new Date(startDate).getTime());
        Date eDate = new java.sql.Date(new Date(endDate).getTime());
        System.out.println("inside get patient report 2");
        Timestamp t = new Timestamp( new Date(currDate).getTime());
        Date cDate = new java.sql.Date(new Date(currDate).getTime());

//        Date cDate = new Date(currDate);
        System.out.println("in patient report serviveImpls = id = " + id + " sDate = " + sDate + " eDate = " + eDate + " c date  = " + cDate);
        List<Appointment> allAppointments = appointmentRepository.findAllByUserIdAndDateBetween(id, sDate, eDate);
        System.out.println("all appointments patietn report = " + allAppointments);
        List<Appointment> totalAppointments =new ArrayList<>();
        List<Appointment> noShowAppointments = new ArrayList<>();
        PatientRecord patientRecord = new PatientRecord();

        if(allAppointments.size() == 0){
            patientRecord.noShowAppointments = new ArrayList<>();
            patientRecord.totalAppointments = new ArrayList<>();
            patientRecord.noShowRate = 0;
            return patientRecord;
        }
        int noShow =0;
        for(int i = 0; i < allAppointments.size(); i++){
            Appointment a = allAppointments.get(i);
            if(a.getCheckIn() == true && a.getDate().before(cDate)){
                totalAppointments.add(a);
            }else{
                noShowAppointments.add(a);
                noShow++;
            }
        }
        double noShowRate = noShow/allAppointments.size();
        System.out.println("all appointments  = " + allAppointments);
        patientRecord.totalAppointments = allAppointments;
        patientRecord.noShowAppointments = noShowAppointments;
        patientRecord.noShowRate = noShowRate;
        return patientRecord;
    }

    public SystemRecord getSystemReport(Long clinicId, String startDate, String endDate, String currDate){

        System.out.println("inside get system report ");
        Long id = clinicId;
        Date sDate = new java.sql.Date(new Date(startDate).getTime());
        Date eDate = new java.sql.Date(new Date(endDate).getTime());
        Timestamp t = new Timestamp( new Date(currDate).getTime());
        Date cDate = new java.sql.Date(new Date(currDate).getTime());

        System.out.println("in patient report serviveImpls = id = " + id + " sDate = " + sDate + " eDate = " + eDate);
        List<Appointment> allAppointments = appointmentRepository.findAllByClinicIdAndDateBetween(id, sDate, eDate);


        System.out.println("all appointments  = " + allAppointments);
        SystemRecord systemRecord = new SystemRecord();
        List<Appointment> totalAppointments =new ArrayList<>();
        List<Appointment> noShowAppointments = new ArrayList<>();
        if(allAppointments.size() == 0){
            systemRecord.noShowAppointments = new ArrayList<>();
            systemRecord.totalAppointments = new ArrayList<>();
            systemRecord.noShowRate = 0;
            return systemRecord;
        }
        int noShow =0;
        for(int i = 0; i < allAppointments.size(); i++){
            Appointment a = allAppointments.get(i);
            if(a.getCheckIn() == true && a.getDate().before(cDate)){
                totalAppointments.add(a);
            }else{
                noShowAppointments.add(a);
                noShow++;
            }
        }
        double noShowRate = noShow/allAppointments.size();
        System.out.println("all appointments  = " + allAppointments);
        systemRecord.totalAppointments = allAppointments;
        systemRecord.noShowAppointments = noShowAppointments;
        systemRecord.noShowRate = noShowRate;
        return systemRecord;

    }
}
