package edu.sjsu.cmpe275.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
	
	@GetMapping("/")
	public String vms()
	{
		return "Inside vaccination management system";
	}
	@GetMapping("/restricted")
	public String restricted()
	{
		return " to see this you need to be logged in";
		
	}

}
