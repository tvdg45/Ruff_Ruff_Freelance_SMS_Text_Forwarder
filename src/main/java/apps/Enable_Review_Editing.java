//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Reviews;
import utilities.Security_Code_Generator;

import java.io.IOException;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://www.timothysdigitalsolutions.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/enable-review-editing")
public class Enable_Review_Editing {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "security_code", defaultValue = "") String security_code,
            @RequestParam(value = "row_id", defaultValue = "") String row_id,
            @RequestParam(value = "item_id", defaultValue = "") String item_id,
            @RequestParam(value = "enable_review_editing", defaultValue = "") String enable_review_editing
    ) {
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();

            //Set this default string, so that it can be randomly scrambled in to a specified number of characters.
            Security_Code_Generator.entire_string = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

            //Specify the number of characters for the new security code.
            Security_Code_Generator.number_of_characters = 25;
            
            Control_Change_Reviews.use_connection = use_open_connection;
            Control_Change_Reviews.new_security_code = String.valueOf(Security_Code_Generator.generate_hash());
            Control_Change_Reviews.security_code = security_code;
            Control_Change_Reviews.row_id = row_id;
            Control_Change_Reviews.item_id = item_id;
            Control_Change_Reviews.enable_review_editing = enable_review_editing;
            
            if (enable_review_editing.equals("Enable")) {
                
                return Control_Change_Reviews.control_enable_review_editing();
            } else {
                
                return "";
            }
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Enable_Review_Editing.class, args);
    }   
}
