package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.Disease;
import edu.sjsu.cmpe275.Model.Vaccination;
import edu.sjsu.cmpe275.Model.VaccinationRecord;
import edu.sjsu.cmpe275.Repository.DiseaseRepository;
import edu.sjsu.cmpe275.Repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VaccinationServiceImpl implements VaccinationService {

    @Autowired
    VaccinationRepository vaccinationRepository;

    @Autowired
    DiseaseRepository diseaseRepository;

    @Override
    public List<Vaccination> getAllVaccinations() {
        try {
            List<Vaccination> vaccinations = new ArrayList<>();
            vaccinationRepository.findAll().forEach(vaccinations::add);
            if (!vaccinations.isEmpty())
                return vaccinations;
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public Vaccination createVaccination(String name, List<Long> diseaseIds, String manufacturer, Integer numberOfShots, Integer shotInterval, Integer duration) {
        try {
            Vaccination vaccination = new Vaccination();
            vaccination.setName(name);
            vaccination.setManufacturer(manufacturer);
            vaccination.setDuration(duration);
            vaccination.setNumberOfShots(numberOfShots);
            vaccination.setShotInterval(shotInterval);
            List<Disease> diseases = new ArrayList<>();
            for (Long diseaseId :
                    diseaseIds) {
                Optional<Disease> diseaseData = diseaseRepository.findById(diseaseId);
                if (diseaseData.isPresent()) {
                    diseases.add(diseaseData.get());
                }
            }
            vaccination.setDiseases(diseases);
            return vaccinationRepository.save(vaccination);
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @Override
    public Optional<Vaccination> getVaccinationById(Long vaccinationId) {
        return vaccinationRepository.findById(vaccinationId);
    }

    @Override
    public Optional<Vaccination> updateVaccinationById(Long vaccinationId) {
//        try {
//            Vaccination vaccination = new Vaccination();
//            vaccination.setName(name);
//            vaccination.setManufacturer(manufacturer);
//            vaccination.setDuration(duration);
//            vaccination.setNumberOfShots(numberOfShots);
//            vaccination.setShotInterval(shotInterval);
//            List<Disease> diseases = new ArrayList<>();
//            for (Long diseaseId :
//                    diseaseIds) {
//                Optional<Disease> diseaseData = diseaseRepository.findById(diseaseId);
//                if (diseaseData.isPresent()) {
//                    diseases.add(diseaseData.get());
//                }
//            }
//            vaccination.setDiseases(diseases);
//            return vaccinationRepository.save(vaccination);
//        } catch (Exception exception) {
//            System.out.println(exception.getStackTrace());
//        }
        return null;
    }
}
