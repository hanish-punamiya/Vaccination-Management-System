package edu.sjsu.cmpe275.Repository;

import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
@Transactional (readOnly = true)
public interface UserRepository extends JpaRepository<User,Long> {
//    @Override
    List<User> findAll();
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);



   

}
