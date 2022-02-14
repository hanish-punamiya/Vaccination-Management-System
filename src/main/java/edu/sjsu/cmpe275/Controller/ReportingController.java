package edu.sjsu.cmpe275.Controller;

import edu.sjsu.cmpe275.Helper.Model.PatientRecord;
import edu.sjsu.cmpe275.Helper.Model.SystemRecord;
import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.User;
import edu.sjsu.cmpe275.Service.ReportingService;
import edu.sjsu.cmpe275.Service.ReportingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/report")
public class ReportingController {
    @Autowired
    ReportingServiceImpl reportingService;

    @PostMapping("/patientreports")
    public ResponseEntity<PatientRecord> getPatientReport(@RequestBody Map<String, Object> requestBody) {
        String startDate = (String) requestBody.get("startDate");
        String endDate = (String) requestBody.get("endDate");
        String currDate = (String) requestBody.get("currDate");
        Long userId = Long.parseLong(String.valueOf(requestBody.get("userId"))) ;
        System.out.println("request object: start date = " + startDate + " end date = " + endDate + " currDate = "+ currDate);
        try {
            List<Appointment> appointments = new ArrayList<>();
            PatientRecord patientRecord =  reportingService.getPatientReport(userId, startDate, endDate, currDate);
            return new ResponseEntity<>(patientRecord, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/systemreports")
    public ResponseEntity<SystemRecord> getSystemReport(@RequestBody Map<String, Object> requestBody) {
        System.out.println("inside system report controller");
        String startDate = (String) requestBody.get("startDate");
        String endDate = (String) requestBody.get("endDate");
        String currDate = (String) requestBody.get("currDate");
        Long clinicId = Long.parseLong(String.valueOf(requestBody.get("clinicId"))) ;
        try {
            List<Appointment> appointments = new ArrayList<>();
            SystemRecord systemRecord = reportingService.getSystemReport(clinicId, startDate, endDate, currDate);
            return new ResponseEntity<>(systemRecord , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
