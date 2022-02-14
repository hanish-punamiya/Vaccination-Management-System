package edu.sjsu.cmpe275.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.Model.ConfirmationToken;
import edu.sjsu.cmpe275.Repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

	@Autowired
ConfirmationTokenRepository confirmationTokenRepository;
	
	public void saveConfirmationToken(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}
	
	public ConfirmationToken getToken(String token) {
		ConfirmationToken conf=confirmationTokenRepository.findByToken(token).orElseThrow(()-> new IllegalStateException("Token not found !"));
		return conf; 
	}
	
	public ConfirmationToken setConfirmedAt(String token) {
		ConfirmationToken conf=confirmationTokenRepository.findByToken(token).orElseThrow(()-> new IllegalStateException("Token not found !"));
		conf.setConfirmedAt(LocalDateTime.now());
		confirmationTokenRepository.save(conf);
		return conf; 
	}
}
