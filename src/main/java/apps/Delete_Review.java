//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Reviews;

import java.io.IOException;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://www.timothysdigitalsolutions.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/delete-review")
public class Delete_Review {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "row_id", defaultValue = "") String row_id,
            @RequestParam(value = "item_id", defaultValue = "") String item_id,
            @RequestParam(value = "delete_review", defaultValue = "") String delete_review
    ) {
		
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            Control_Change_Reviews.use_connection = use_open_connection;
            Control_Change_Reviews.row_id = row_id;
            Control_Change_Reviews.item_id = item_id;
            Control_Change_Reviews.delete_review = delete_review;
            
            if (delete_review.equals("Delete")) {
                
                return Control_Change_Reviews.control_delete_review();
            } else {
                
                return "";
            }
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Delete_Review.class, args);
    }
}
