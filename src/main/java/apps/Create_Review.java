//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Reviews;
import utilities.Security_Code_Generator;

import java.time.LocalDate;
import java.time.LocalTime;

//import java.io.IOException;
//import java.io.PrintWriter;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://www.timothysdigitalsolutions.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/create-review")
public class Create_Review {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "name", defaultValue = "") String name,
		@RequestParam(value = "email", defaultValue = "") String email,
		@RequestParam(value = "rating", defaultValue = "") String rating,
		@RequestParam(value = "subject", defaultValue = "") String subject,
		@RequestParam(value = "description", defaultValue = "") String description,
		@RequestParam(value = "create_review", defaultValue = "") String create_review
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
		
		//Set this default string, so that it can be randomly scrambled in to a specified number of characters.
		Security_Code_Generator.entire_string = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		//Specify the number of characters for the new security code.
		Security_Code_Generator.number_of_characters = 25;
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
		Control_Change_Reviews.use_connection = use_open_connection;
		Control_Change_Reviews.item_id = item_id;
		Control_Change_Reviews.name = name;
		Control_Change_Reviews.email = email;
		Control_Change_Reviews.rating = rating;
		Control_Change_Reviews.subject = subject;
		Control_Change_Reviews.description = description;
		Control_Change_Reviews.new_security_code = String.valueOf(Security_Code_Generator.generate_hash());
		Control_Change_Reviews.date_received = String.valueOf(localDate);
		Control_Change_Reviews.time_received = String.valueOf(time_format.format(localTime));
		Control_Change_Reviews.create_review = create_review;
		
		if (create_review.equals("Submit")) {
			
			return Control_Change_Reviews.control_create_review();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Create_Review.class, args);
    }
}
