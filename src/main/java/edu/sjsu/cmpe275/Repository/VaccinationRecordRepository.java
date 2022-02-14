package edu.sjsu.cmpe275.Repository;

import edu.sjsu.cmpe275.Model.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
    List<VaccinationRecord> findAllByUserId(Long userId);
    List<VaccinationRecord> findAllByVaccinationIdAndUserId(Long vaccinationId, Long userId);
    List<VaccinationRecord> findAllByAppointmentIdAndUserId(Long appointmentId,  Long userId);
    List<VaccinationRecord> findAllByAppointmentId(Long appointmentId);

}