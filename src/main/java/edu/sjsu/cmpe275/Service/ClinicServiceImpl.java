package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Address;
import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.Clinic;
import edu.sjsu.cmpe275.Model.Disease;
import edu.sjsu.cmpe275.Repository.AppointmentRepository;
import edu.sjsu.cmpe275.Repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ClinicServiceImpl implements ClinicService {
    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Optional<Clinic> createClinic(String clinicName, Address clinicAddress, int numPhys, int opening , int closing){
        System.out.println("inside clinic service impl, create clinic = "  );

        Clinic clinicObj = new Clinic();
        clinicObj.setClinicName(clinicName);

        clinicObj.setAddress(clinicAddress);
        clinicObj.setNumPhysicians(numPhys);
        clinicObj.setOpen(opening);
        clinicObj.setClose(closing);
        clinicObj.setBusinessHours(closing-opening);
        try{
            Optional<Clinic> clinic =  Optional.of(clinicRepository.save(clinicObj));
            System.out.println("clinic name and id = "+  clinic.get().getClinicName() + " " + clinic.get().getId());
            return clinic;
        }catch (Exception e){
            System.out.println("exception thrown create disease error  = " +e);
            return null;
        }
    }

    @Override
    public Optional<Clinic> getClinicByName(String clinicName) {
        return clinicRepository.findByClinicName(clinicName);
    }

    @Override
    public boolean deleteClinicById(long id) {
        try{
            clinicRepository.deleteById(id);
            return true;
        }catch (Exception e){
            System.out.println("exception in deleting clinic" + e);
            return false;
        }
    }

//    @Override
//    public List<Clinic> getAllAvailableClinics(Object object) {
//        Date searchDate = new Date();
//        String searchTime = "";
//        List<Clinic> availableClinics = new ArrayList<>();
//        List<Clinic> clinics =  clinicRepository.findAll();
//        for(Clinic c : clinics) {
//            List<Appointment> appointments = c.getAppointments();
//            int phys_num = c.getNumPhysicians();
//            int count = 0;
//            for (Appointment a : appointments) {
//                Date d = a.getDate();
//                Time t = a.getTime();
//                if (d.equals(searchDate) && t.equals(searchTime)) {
//                    count++;
//                }
//            }
//            if (count < phys_num) {
//                availableClinics.add(c);
//            }
//        }
//        return availableClinics;
//    }

    @Override
    public List<Clinic> getAvailableClinics(String appointmentTime) throws ParseException {

        try{
            System.out.println("apoointment time stamp = " +  new Timestamp(new Date(appointmentTime).getTime()));
            System.out.println("apoointment Date = " +  new java.sql.Date(new Date(appointmentTime).getTime()));


            Timestamp t = new Timestamp( new Date(appointmentTime).getTime());
            Date d = new java.sql.Date(new Date(appointmentTime).getTime());
            int hr = t.getHours();
            System.out.println( " time = " + t + " hours = " + hr);

            List<Clinic> availableClinics = new ArrayList<>();
            List<Appointment> appointments = new ArrayList<>();
            appointments =  appointmentRepository.findAllAppointmentByDateTime(t);
            System.out.println("apointments in available clinics = " + appointments);
            Map<Clinic, Integer> map = new HashMap<>();
            for (Appointment a: appointments){
                Clinic c  = a.getClinic();
                if(map.containsKey(c)){
                    map.put(c, map.get(c) + 1);
                }else{
                    map.put(c, 1);
                }
            }
            List<Clinic> notAvailableClinics = new ArrayList<>();
            for(Map.Entry<Clinic, Integer> entry : map.entrySet()){
                Clinic value = entry.getKey();
                int key = entry.getValue();
                int numPhys = value.getNumPhysicians();
                if(key >= numPhys){
                    notAvailableClinics.add(value);
                }
            }
            List<Clinic> allClinics = clinicRepository.findAll();
            for(Clinic c : allClinics){
                if(!notAvailableClinics.contains(c)){
                    availableClinics.add(c);
                }
            }
            return availableClinics;
        }catch (Exception e){
            System.out.println("exception e  = " +e);
            return new ArrayList<>();
        }

    }

}
