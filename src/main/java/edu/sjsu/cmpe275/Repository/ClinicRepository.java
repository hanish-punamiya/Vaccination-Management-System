package edu.sjsu.cmpe275.Repository;

import edu.sjsu.cmpe275.Model.Clinic;
import edu.sjsu.cmpe275.Model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic,Long> {
    @Query(value = "select c from Clinic c where c.clinicName=:clinicName")
    public Optional<Clinic> findByClinicName(String clinicName);
}
