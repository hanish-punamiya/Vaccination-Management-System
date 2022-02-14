package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Helper.Model.PatientRecord;
import edu.sjsu.cmpe275.Helper.Model.SystemRecord;
import edu.sjsu.cmpe275.Model.Appointment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportingService {
    public PatientRecord getPatientReport(Long userId, String startDate, String endDate, String currDate);
    public SystemRecord getSystemReport(Long clinicId, String startDate, String endDate, String currDate);
}
