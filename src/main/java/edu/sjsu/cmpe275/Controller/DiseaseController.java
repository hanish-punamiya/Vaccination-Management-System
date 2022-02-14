package edu.sjsu.cmpe275.Controller;

import edu.sjsu.cmpe275.Helper.Error.Response;
import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.Disease;
import edu.sjsu.cmpe275.Repository.DiseaseRepository;
import edu.sjsu.cmpe275.Service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/disease")
public class DiseaseController {


    @Autowired
    DiseaseRepository diseaseRepository;
    @Autowired
    DiseaseService diseaseService;

    @PostMapping("/createDisease")
    @Transactional
    public ResponseEntity<Object> createDisease(@RequestBody Map<String, Object> requestBody) {
        try{
            String diseaseName = (String)requestBody.get("diseaseName");
            String diseaseDescription = (String)requestBody.get("diseaseDescription");
            System.out.println("create disease controller called req = " + diseaseName + " " + diseaseDescription);
            Optional<Disease> diseaseData = diseaseService.getDiseaseByName(diseaseName);
            if(diseaseData.isPresent()){
                return new ResponseEntity<Object>(new Response("400","Disease name "+ diseaseName+" already exists"), HttpStatus.BAD_REQUEST);
            }

            diseaseData =  diseaseService.createDisease(diseaseName, diseaseDescription);
            if(diseaseData.isPresent()){
                Disease _disease =  diseaseData.get();
                return new ResponseEntity<>(_disease, HttpStatus.OK);
            }else{
                return new ResponseEntity<Object>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/deleteDisease/{diseaseId}")
    @Transactional
    public ResponseEntity<Object> deleteDisease(@PathVariable("diseaseId") long diseaseId) {
        try{
            System.out.println("delete disease controller called");
            Optional<Disease> diseaseData = diseaseRepository.findById(diseaseId);
            if(diseaseData.isPresent()){
                return new ResponseEntity<Object>(new Response("400","Disease id "+ diseaseId+" doesn not exists"), HttpStatus.BAD_REQUEST);
            }
            boolean isDeleted = diseaseService.deleteDiseaseById(diseaseId);
            if(isDeleted){
                return new ResponseEntity<>("disease id: "+ diseaseId + " deleted successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<Object>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    @Transactional
    public ResponseEntity<Object> getAllDiseases() {
        try {
            return new ResponseEntity<>(diseaseService.getAllDiseases(), HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/disease")
    @Transactional
    public ResponseEntity<Object> getDiseaseById(Map<String, Object> requestBody) {
        try {
            Long diseaseId = ((Number) requestBody.get("diseaseId")).longValue();
            Optional<Disease> diseaseData = diseaseService.getDiseaseById(diseaseId);
            if(!diseaseData.isPresent()){
                return new ResponseEntity<Object>(new Response("404","Disease id "+ diseaseId+" does not exists"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(diseaseData.get(), HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
