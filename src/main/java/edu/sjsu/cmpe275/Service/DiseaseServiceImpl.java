package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Disease;
import edu.sjsu.cmpe275.Repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiseaseServiceImpl implements DiseaseService {
    @Autowired
    DiseaseRepository diseaseRepository;

    @Override
    public Optional<Disease> createDisease(String diseaseName, String description){
        System.out.println("inside disease service impl, create disease = " + diseaseName );
        try{

            Disease disease = new Disease();
            disease.setName(diseaseName);
            disease.setDescription(description);

            Optional<Disease> savedDisease =  Optional.of(diseaseRepository.save(disease)) ;
            System.out.println("disease name and id = "+  savedDisease.get().getName() + " " + savedDisease.get().getId());
            return savedDisease;
        }catch (Exception e){
            System.out.println("exception thrown create disease error  = " +e);
            return null;
        }
    }

    @Override
    public Optional<Disease> getDiseaseByName(String diseaseName){
        return diseaseRepository.findByDiseaseName(diseaseName);
    }

    @Override
    public boolean deleteDiseaseById(long id){
        try{
            diseaseRepository.deleteById(id);
            return true;
        }catch (Exception e){
            System.out.println("exception in deleting disease" + e);
            return false;
        }
    }

    @Override
    public List<Disease> getAllDiseases() {
        try{
            List<Disease> diseases = new ArrayList<>();
            diseaseRepository.findAll().forEach(diseases::add);
            return diseases;
        }catch (Exception e){
            System.out.println("exception in getAllDiseases" + e);
        }
        return null;
    }

    @Override
    public Optional<Disease> getDiseaseById(Long diseaseId) {
        return diseaseRepository.findById(diseaseId);
    }
}
