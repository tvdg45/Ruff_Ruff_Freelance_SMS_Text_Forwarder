//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Reviews;
import utilities.Security_Code_Generator;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://www.timothysdigitalsolutions.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/send-review-change-email-request")
public class Send_Review_Change_Email_Request {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "row_id", defaultValue = "") String row_id,
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "send_review_change_email_request", defaultValue = "") String send_review_change_email_request
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
		
		//Set this default string, so that it can be randomly scrambled in to a specified number of characters.
		Security_Code_Generator.entire_string = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		//Specify the number of characters for the new security code.
		Security_Code_Generator.number_of_characters = 25;
        
		Control_Change_Reviews.use_connection = use_open_connection;
		Control_Change_Reviews.new_security_code = String.valueOf(Security_Code_Generator.generate_hash());
		Control_Change_Reviews.row_id = row_id;
		Control_Change_Reviews.item_id = item_id;
		Control_Change_Reviews.send_review_change_email_request = send_review_change_email_request;
		
		if (send_review_change_email_request.equals("Edit")) {
			
			return Control_Change_Reviews.control_send_review_change_email_request();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Send_Review_Change_Email_Request.class, args);
    }
}
