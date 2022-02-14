package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Address;
import edu.sjsu.cmpe275.Model.Clinic;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public interface ClinicService {
    public Optional<Clinic> createClinic(String clinicName, Address clinicAddress, int numPhys, int opening , int closing);
    public Optional<Clinic> getClinicByName(String clinicName);
    public boolean deleteClinicById(long id);
//    public List<Clinic> getAllAvailableClinics(Object object);
    public List<Clinic> getAvailableClinics(String appointmentTime) throws ParseException;
}
