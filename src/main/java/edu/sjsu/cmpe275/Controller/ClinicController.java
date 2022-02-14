package edu.sjsu.cmpe275.Controller;

import edu.sjsu.cmpe275.Helper.Error.Response;
import edu.sjsu.cmpe275.Model.Address;
import edu.sjsu.cmpe275.Model.Clinic;
import edu.sjsu.cmpe275.Model.Disease;
import edu.sjsu.cmpe275.Model.User;
import edu.sjsu.cmpe275.Repository.ClinicRepository;
import edu.sjsu.cmpe275.Repository.UserRepository;
import edu.sjsu.cmpe275.Service.ClinicService;
import edu.sjsu.cmpe275.Service.ClinicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
    @Autowired
    ClinicServiceImpl clinicService;
    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/createClinic")
    @Transactional
    public ResponseEntity<Object> createClinic(@RequestBody Map<String, Object> requestBody) {

//        System.out.println("create clinic controller called = " + clinicName +clinicAddress+city+state+zipCode+numPhys);
        try {


            String clinicName = (String) requestBody.get("name");
            String clinicAddress = (String) requestBody.get("address");
            int numPhys = Integer.parseInt(String.valueOf(requestBody.get("physicians")));
            int opening = Integer.parseInt(String.valueOf(requestBody.get("opening")));
            int closing = Integer.parseInt(String.valueOf(requestBody.get("closing")));
            String city = (String) requestBody.get("city");
            String state = (String) requestBody.get("state");
            int zipCode = Integer.parseInt(String.valueOf(requestBody.get("zipCode")));
            System.out.println("create clinic controller called = " + clinicName + clinicAddress + city + state + zipCode + numPhys);

            Optional<Clinic> clinicData = clinicService.getClinicByName(clinicName);
            if (clinicData.isPresent()) {
                return new ResponseEntity<Object>(new Response("400", "Clinic name " + clinicName + " already exists"), HttpStatus.BAD_REQUEST);
            }

            Address address = new Address();
            address.setCity(city);
            address.setLine1(clinicAddress);
            address.setState(state);
            address.setZipCode(zipCode);


            clinicData = clinicService.createClinic(clinicName, address, numPhys, opening, closing);

            if (clinicData.isPresent()) {
                Clinic _clinic = clinicData.get();
                return new ResponseEntity<>(_clinic, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(new Response("500", "\"something went wrong\""), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteClinic/{clinicId}")
    @Transactional
    public ResponseEntity<Object> deleteClinic(@PathVariable("clinicId") long clinicId) {
        try{
            System.out.println("delete clinic controller called");
            Optional<Clinic> clinicData = clinicRepository.findById(clinicId);
            if(!clinicData.isPresent()){
                return new ResponseEntity<Object>(new Response("400","Clinic id "+ clinicId+" doesn not exists"), HttpStatus.BAD_REQUEST);
            }
            boolean isDeleted = clinicService.deleteClinicById(clinicId);
            if(isDeleted){
                return new ResponseEntity<>("clinic id: "+ clinicId + " deleted successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<Object>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getClinic/{clinicId}")
    @Transactional
    public ResponseEntity<Object> getClinic(@PathVariable("clinicId") long clinicId) {
        try{
            System.out.println("get clinic controller called");
            Optional<Clinic> clinicData = clinicRepository.findById(clinicId);
            if(!clinicData.isPresent()) {
                return new ResponseEntity<Object>(new Response("400", "Clinic id " + clinicId + " doesn not exists"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(clinicData, HttpStatus.OK);
        }catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getAvailableClinics")
    public ResponseEntity<List<Clinic>> getAvailableClinics(@RequestBody Map<String, Object> requestBody) throws ParseException {

        String appointmentTime = (String)requestBody.get("appointmentTime");
        System.out.println("get available clinic controller called appointment time = " +appointmentTime );
        List<Clinic> clinics = new ArrayList<>();
        clinics = clinicService.getAvailableClinics(appointmentTime);
        return new ResponseEntity<>(clinics, HttpStatus.OK);
    }

    @GetMapping("/getAllClinics")
    public ResponseEntity<List<Clinic>> getAllClinics() {

        //Long clinicId = Long.parseLong(String.valueOf(requestBody.get("clinicId"))) ;
        //System.out.println("get available clinic controller called clinic Id = " + clinicId);
        List<Clinic> clinics = new ArrayList<>();
        clinics = clinicRepository.findAll();
        System.out.println("all clinics = " + clinics);
        return new ResponseEntity<>(clinics, HttpStatus.OK);
    }
}
