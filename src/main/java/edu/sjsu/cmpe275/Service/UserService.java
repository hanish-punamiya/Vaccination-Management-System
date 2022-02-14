package edu.sjsu.cmpe275.Service;

import edu.sjsu.cmpe275.Model.Appointment;
import edu.sjsu.cmpe275.Model.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * passenger service to create, delete and get passengers
 */
@Service
public interface  UserService {


    public List<Appointment> getAllUserAppointments(Long id);
    public String singUpUser(User user);
    public User loginUser(String email, String encodedpassword); //not called
    public Optional<User> getUser(Long id);


}
