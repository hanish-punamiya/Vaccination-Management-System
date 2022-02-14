package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Disease;
import edu.sjsu.cmpe275.Model.Vaccination;

import java.util.List;
import java.util.Optional;

public interface VaccinationService {
    public List<Vaccination> getAllVaccinations();
    public Vaccination createVaccination(String name, List<Long> diseaseIds, String manufacturer, Integer numberOfShots, Integer shotInterval, Integer duration);
    public Optional<Vaccination> getVaccinationById(Long vaccinationId);
    public Optional<Vaccination> updateVaccinationById(Long vaccinationId);
}
