package edu.sjsu.cmpe275.Controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.sjsu.cmpe275.Config.EmailConfig;
import edu.sjsu.cmpe275.Helper.Constant.Url;
import edu.sjsu.cmpe275.Helper.Error.Response;
import edu.sjsu.cmpe275.Model.Address;
import edu.sjsu.cmpe275.Model.AppUserRole;
import edu.sjsu.cmpe275.Model.Clinic;
import edu.sjsu.cmpe275.Model.User;
import edu.sjsu.cmpe275.Repository.AddressRepository;
import edu.sjsu.cmpe275.Repository.UserRepository;
import edu.sjsu.cmpe275.Service.UserServiceImpl;
import edu.sjsu.cmpe275.Util.NotificationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    EmailConfig emailConfig;


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = new ArrayList<User>();
            userRepository.findAll().forEach(users::add);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<Object> signUp(@RequestBody Map<String, Object> requestBody)
    {
        try{
            String email = (String) requestBody.get("email");
            String password = (String) requestBody.get("password");
            String firstName = (String) requestBody.get("firstName");
            String lastName = (String) requestBody.get("lastName");
            String middleName = (String) requestBody.get("middleName");
            String dateOfBirth = (String) requestBody.get("dateOfBirth");
            String addressLine1 = (String) requestBody.get("addressLine1");
            String addressLine2 = (String) requestBody.get("addressLine2");
            String city = (String) requestBody.get("city");
            String gender = (String) requestBody.get("gender");
            String state = (String) requestBody.get("state");
            Integer zipcode = (Integer) requestBody.get("zipcode");

            Optional<User> userData =	userRepository.findByEmail(email);
            if(userData.isPresent())
                return new ResponseEntity<Object>(new Response("403", "emailid " + email + " already exists"), HttpStatus.FORBIDDEN);

            User user = new User();
            if(!firstName.isEmpty() && firstName!=null){
                user.setFirstName(firstName);
            }
            if(!lastName.isEmpty() && lastName!=null){
                user.setLastName(lastName);
            }
            if(!middleName.isEmpty() && middleName!=null){
                user.setMiddleName(middleName);
            }
            if(!dateOfBirth.isEmpty() && dateOfBirth!=null){
                user.setDateOfBirth(new java.sql.Date(new Date(dateOfBirth).getTime()));
            }
            if(!email.isEmpty() && email!=null){
                if(email.contains("@sjsu.edu"))
                    user.setRole("ADMIN");
                else
                    user.setRole("USER");
                user.setEmail(email);
            }
            if(!password.isEmpty() && password!=null){
                user.setPassword(password);
            }
            if(!gender.isEmpty() && gender!=null){
                user.setGender(gender);
            }
            user.setEnabled(true);
            user.setVerified(false);

            Address address = new Address();
            if(!addressLine1.isEmpty() && addressLine1!=null){
                address.setLine1(addressLine1);
            }
            if(!addressLine2.isEmpty() && addressLine2!=null){
                address.setLine2(addressLine2);
            }
            if(!city.isEmpty() && city!=null){
                address.setCity(city);
            }
            if(!state.isEmpty() && state!=null){
                address.setState(state);
            }
            if(zipcode!=null){
                address.setZipCode(zipcode);
            }

            Address savedAdress = addressRepository.save(address);
            user.setAddress(address);
            User savedUser = userRepository.save(user);

            new NotificationHelper().sendEmail(emailConfig, "shilpi9soni@gmail.com", email, "please click on this link to get verified: " + Url.baseUrl + "/user/auth/verification", "Get Verified");

            return new ResponseEntity<Object>("Successfully signed up", HttpStatus.OK);
        }
        catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<Object> login(@RequestBody Map<String, Object> requestBody)
    {
        try{
            String email = (String) requestBody.get("email");
            String requestPassword = (String) requestBody.get("password");

            Optional<User> userData =	userRepository.findByEmail(email);
             if(!userData.isPresent())
                 return new ResponseEntity<Object>(new Response("404", "email id " + email + " not found"), HttpStatus.NOT_FOUND);

            User user = userData.get();
            Boolean isEnabled= user.getEnabled();
            String password = user.getPassword();

            if(!isEnabled)
                return new ResponseEntity<Object>(new Response("401", "User has yet to confirm email"), HttpStatus.UNAUTHORIZED);
            else if(!password.equals(requestPassword))
                return new ResponseEntity<Object>(new Response("403", "password does not match"), HttpStatus.FORBIDDEN);
            else
                return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        catch (Exception exception){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    	
    }	

    @PostMapping("/user")
    @Transactional
    public ResponseEntity<Object> getUser(@RequestBody Map<String, Object> requestBody) {
        try{

            Long userId = ((Number) requestBody.get("userId")).longValue();
            Optional<User> userData = userService.getUser(userId);
            if(!userData.isPresent())
                return new ResponseEntity<Object>(new Response("404","Disease id "+ userId+" does not exists"), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        }
        catch (Exception exception){
            System.out.println(exception.getStackTrace());
        }
        return null;
    }

    @GetMapping("/auth/verification")
    public String verification(){
        return "You are now verified";
    }


}
