package edu.sjsu.cmpe275.Service;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.ConfirmationToken;
import edu.sjsu.cmpe275.Model.User;
import edu.sjsu.cmpe275.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * this class implements all the functions of passenger service.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	

	private final static String USER_NOT_FOUND_MESSAGE = "user with this email %s not found !";

    @Autowired
    UserRepository userRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	 ConfirmationTokenService confirmationService;

	
	public String singUpUser(User user) {
		
		boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
		if (userExists)
			if(user.getEnabled())
			throw new IllegalStateException("User exist and email already confirmed");
		// we can add here some code to verify if the user exists we and it try to change his information 
		// we can delete all Tokens with the UserId and save the new informations of this user and send a verication Token again
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		//  save the confirmation toekn in the DB and send it  :
		String token = UUID.randomUUID().toString();
		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(15), user);
		confirmationService.saveConfirmationToken(confirmationToken);
		
		// send Email :
		 
		return token;
	}
	
	public void enableAppUser(String email) {
	User user=	userRepository.findByEmail(email).orElseThrow(()->new IllegalStateException("User not found !"));
	user.setEnabled(true);
	userRepository.save(user);
	}
	
	
    @Override
    public List<Appointment> getAllUserAppointments(Long id) {
        try{
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return user.get().getAppointments();
            }
        }
        catch (Exception exception){
            System.out.println(exception.getStackTrace());
        }
        return null;
    }


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));	}

	@Override
	public User loginUser(String email, String loginPassword) {
		
		User user=	userRepository.findByEmail(email).orElseThrow(()->new IllegalStateException("User not found !"));
		Boolean isEnabled= user.getEnabled();

		String password = user.getPassword();
		
		if(loginPassword.equals(password))
		{
			return user;
		}
		else
		{
			return null;
		}

	}




    @Override
    public Optional<User> getUser(Long id) {
        try{
            return userRepository.findById(id);
        }
        catch (Exception exception){
            System.out.println(exception.getStackTrace());
        }
        return null;
    }
}
