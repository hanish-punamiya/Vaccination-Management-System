package edu.sjsu.cmpe275.Controller;

import edu.sjsu.cmpe275.Helper.Error.Response;
import edu.sjsu.cmpe275.Model.Address;
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
public class UserLoginController {
    @Autowired
    UserRepository userRepository;

/*    @PostMapping("/login")
    public ResponseEntity<Object> userSignIn(@RequestBody Map<String, Object> requestBody) {

        String email = String.valueOf(requestBody.get("email"));
        String password = String.valueOf(requestBody.get("password"));


        Optional<User> userData = userRepository.findByEmail(email);
        if(userData.isPresent()){
            return new ResponseEntity<Object>(new Response("400","User email "+ email +" does not exists"), HttpStatus.BAD_REQUEST);
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
    }*/
}
