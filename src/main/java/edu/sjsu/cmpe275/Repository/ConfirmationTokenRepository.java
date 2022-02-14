package edu.sjsu.cmpe275.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.Model.ConfirmationToken;


@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
	
	Optional<ConfirmationToken> findByToken(String token);


}
