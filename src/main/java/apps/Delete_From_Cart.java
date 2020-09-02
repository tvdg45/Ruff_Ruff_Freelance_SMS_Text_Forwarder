//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

import java.io.IOException;
//import java.io.PrintWriter;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://www.timothysdigitalsolutions.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/delete-from-cart")
public class Delete_From_Cart {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "row_id", defaultValue = "") String row_id,
		@RequestParam(value = "guest_session", defaultValue = "") String guest_session,
		@RequestParam(value = "delete_from_cart", defaultValue = "") String delete_from_cart
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
        
		Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
		Control_Change_Shopping_Cart_Items.row_id = row_id.split(",");
		Control_Change_Shopping_Cart_Items.guest_session = guest_session;
		Control_Change_Shopping_Cart_Items.delete_from_cart = delete_from_cart;
		
		if (delete_from_cart.equals("Delete item(s)")) {
			
			return Control_Change_Shopping_Cart_Items.control_delete_from_cart();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Delete_From_Cart.class, args);
    }
}
