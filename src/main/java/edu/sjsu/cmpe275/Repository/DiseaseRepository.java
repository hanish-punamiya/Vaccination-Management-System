package edu.sjsu.cmpe275.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import edu.sjsu.cmpe275.Model.Disease;
import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease,Long> {

    @Query(value = "select d from Disease d where d.name=:diseaseName")
    public Optional<Disease> findByDiseaseName(String diseaseName);
}
