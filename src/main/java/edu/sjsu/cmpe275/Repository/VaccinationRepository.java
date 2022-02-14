package edu.sjsu.cmpe275.Repository;

import edu.sjsu.cmpe275.Model.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
//    List<Vaccination> findAllById(List<Long> vaccinationIds);
}