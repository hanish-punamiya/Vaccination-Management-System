package edu.sjsu.cmpe275.Controller;

import edu.sjsu.cmpe275.Helper.Error.Response;
import edu.sjsu.cmpe275.Model.Address;
import edu.sjsu.cmpe275.Model.Clinic;
import edu.sjsu.cmpe275.Model.User;
import edu.sjsu.cmpe275.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserSignUpController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/signUp")
    public ResponseEntity<Object> userSignUp(@RequestBody Map<String, Object> requestBody) {

        String firstName = String.valueOf(requestBody.get("firstName"));
        String middleName = String.valueOf(requestBody.get("middleName"));
        String lastName = String.valueOf(requestBody.get("lastName"));
        String email  = String.valueOf(requestBody.get("email"));
        String gender = String.valueOf(requestBody.get("gender"));
        String addressLine = (String) requestBody.get("addressLine");
        String city = (String) requestBody.get("city");
        String state = (String) requestBody.get("state");
        int zipcode = Integer.parseInt(String.valueOf(requestBody.get("zipCode")));



        Optional<User> userData = userRepository.findByEmail(email);
        if(userData.isPresent()){
            return new ResponseEntity<Object>(new Response("400","User email "+ email +" already exists"), HttpStatus.BAD_REQUEST);
        }

        Address address = new Address();
        address.setCity(city);
        address.setLine1(addressLine);
        address.setState(state);
        address.setZipCode(zipcode);

        User user = new User();
        user.setAddress(address);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmail(email);

        userData =  Optional.of(userRepository.save(user));

        if(userData.isPresent()){
            User _user =  userData.get();
            return new ResponseEntity<>(_user, HttpStatus.OK);
        }else{
            return new ResponseEntity<Object>( new Response("500","\"something went wrong\""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
