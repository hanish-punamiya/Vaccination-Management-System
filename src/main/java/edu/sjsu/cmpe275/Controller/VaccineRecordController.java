package edu.sjsu.cmpe275.Controller;


import edu.sjsu.cmpe275.Model.User;
import edu.sjsu.cmpe275.Model.VaccinationRecord;
import edu.sjsu.cmpe275.Service.VaccinationRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/uservaccination")
public class VaccineRecordController {

    @Autowired
    VaccinationRecordServiceImpl vaccinationRecordService;

    @PostMapping("/due")
    @Transactional
    public ResponseEntity<Object> getVaccinationsDue(@RequestBody Map<String, Object> requestBody) {
        try {

            Long userId = ((Number) requestBody.get("userId")).longValue();
            String currentDate = (String) requestBody.get("currentDate");

            return new ResponseEntity<Object>(vaccinationRecordService.getVaccinationsDue(userId, currentDate), HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vaccinationHistory")
    @Transactional
    public ResponseEntity<Object> getVaccinationRecords(@RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = ((Number) requestBody.get("userId")).longValue();
            List<VaccinationRecord> userVaccinationHistory = vaccinationRecordService.getVaccinationRecords(userId);
            if (userVaccinationHistory.isEmpty())
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.NOT_FOUND);
            else if (userVaccinationHistory == null)
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/appointment")
    @Transactional
    public ResponseEntity<Object> getVaccinationRecordsByAppointment(@RequestBody Long userId, Long appointmentId) {
        try {
            List<VaccinationRecord> userVaccinationHistory = vaccinationRecordService.getVaccinationRecordsByAppointment(appointmentId, userId);
            if (userVaccinationHistory.isEmpty())
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.NOT_FOUND);
            else if (userVaccinationHistory == null)
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vaccination")
    @Transactional
    public ResponseEntity<Object> getVaccinationRecordsByVaccination(@RequestBody Long userId, Long vaccinationId) {
        try {
            List<VaccinationRecord> userVaccinationHistory = vaccinationRecordService.getVaccinationRecordsByVaccine(vaccinationId, userId);
            if (userVaccinationHistory.isEmpty())
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.NOT_FOUND);
            else if (userVaccinationHistory == null)
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.INTERNAL_SERVER_ERROR);
            else
                return new ResponseEntity<Object>(userVaccinationHistory, HttpStatus.OK);
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
