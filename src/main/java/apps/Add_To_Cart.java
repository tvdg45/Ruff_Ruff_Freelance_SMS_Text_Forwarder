//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;

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
@RequestMapping("/add-to-cart")
public class Add_To_Cart {

	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "item", defaultValue = "") String item,
		@RequestParam(value = "thumbnail", defaultValue = "") String thumbnail,
		@RequestParam(value = "category", defaultValue = "") String category,
		@RequestParam(value = "description", defaultValue = "") String description,
		@RequestParam(value = "price", defaultValue = "") String price,
		@RequestParam(value = "quantity", defaultValue = "") String quantity,
		@RequestParam(value = "raw_time_received", defaultValue = "") String raw_time_received,
		@RequestParam(value = "guest_session", defaultValue = "") String guest_session,
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "add_to_cart", defaultValue = "") String add_to_cart
			   ) {
		
		Connection use_open_connection;
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
		try {
			
			use_open_connection = Config.openConnection();
		
			Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
			Control_Change_Shopping_Cart_Items.item = item;
			Control_Change_Shopping_Cart_Items.thumbnail = thumbnail;
			Control_Change_Shopping_Cart_Items.category = category;
			Control_Change_Shopping_Cart_Items.description = description;
			Control_Change_Shopping_Cart_Items.price = price;
			Control_Change_Shopping_Cart_Items.quantity = quantity.split(",");
			Control_Change_Shopping_Cart_Items.raw_time_received = raw_time_received;
			Control_Change_Shopping_Cart_Items.guest_session = guest_session;
			Control_Change_Shopping_Cart_Items.item_id = item_id.split(",");
			Control_Change_Shopping_Cart_Items.date_received = String.valueOf(localDate);
			Control_Change_Shopping_Cart_Items.time_received = String.valueOf(time_format.format(localTime));
			Control_Change_Shopping_Cart_Items.add_to_cart = add_to_cart;
		
			if (add_to_cart.equals("Add to cart")) {
			
				return Control_Change_Shopping_Cart_Items.control_add_to_cart();
			} else {
			
				return "";
			}
		} catch (IOException e) {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Add_To_Cart.class, args);
    }
}
