//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

import java.io.IOException;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/clear-all-carts")
public class Clear_All_Carts {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "raw_time_received", defaultValue = "") String raw_time_received,
            @RequestParam(value = "clear_all_carts", defaultValue = "") String clear_all_carts
    ) {
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
            Control_Change_Shopping_Cart_Items.raw_time_received = raw_time_received;
            Control_Change_Shopping_Cart_Items.clear_all_carts = clear_all_carts;
            
            if (clear_all_carts.equals("Clear all carts")) {
                
                return Control_Change_Shopping_Cart_Items.control_clear_all_carts();
            } else {
                
                return "";
            }
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Clear_All_Carts.class, args);
    }
}
