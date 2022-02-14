package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Helper.Model.VaccinationDue;
import edu.sjsu.cmpe275.Model.VaccinationRecord;

import java.util.List;
import java.util.Map;

public interface VaccinationRecordService {
    public List<VaccinationDue> getVaccinationsDue(Long userId, String date);
    public List<VaccinationRecord> getVaccinationRecords(Long userId);
    public List<VaccinationRecord> getVaccinationRecordsByVaccine(Long vaccinationId, Long userId);
    public List<VaccinationRecord> getVaccinationRecordsByAppointment(Long appointmentId, Long userId);


}
