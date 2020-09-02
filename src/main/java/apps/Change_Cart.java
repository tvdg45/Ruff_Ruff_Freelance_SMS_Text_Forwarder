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
@RequestMapping("/change-cart")
public class Change_Cart {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "row_id", defaultValue = "") String row_id,
		@RequestParam(value = "quantity", defaultValue = "") String quantity,
		@RequestParam(value = "guest_session", defaultValue = "") String guest_session,
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "change_cart", defaultValue = "") String change_cart
			   ) {
		
		Connection use_open_connection;
		
		try {
			
		use_open_connection = Config.openConnection();
		
		Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
		Control_Change_Shopping_Cart_Items.row_id = row_id.split(",");
		Control_Change_Shopping_Cart_Items.quantity = quantity.split(",");
		Control_Change_Shopping_Cart_Items.guest_session = guest_session;
		Control_Change_Shopping_Cart_Items.item_id = item_id.split(",");
		Control_Change_Shopping_Cart_Items.change_cart = change_cart;
		
		if (change_cart.equals("Update cart")) {
			
			return Control_Change_Shopping_Cart_Items.control_change_cart();
		} else {
			
			return "";
		}
		} catch (IOException e) {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Change_Cart.class, args);
    }
}
