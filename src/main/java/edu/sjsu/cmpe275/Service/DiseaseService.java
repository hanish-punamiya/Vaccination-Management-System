package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Disease;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DiseaseService {
    public Optional<Disease> createDisease(String diseaseName, String description);
    public Optional<Disease> getDiseaseByName(String diseaseName);
    public Optional<Disease> getDiseaseById(Long diseaseId);
    public boolean deleteDiseaseById(long id);
    public List<Disease> getAllDiseases();

}
