//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_Shopping_Cart_Items;
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
@RequestMapping("/search-shopping-cart")
public class Search_Shopping_Cart {
    
	@RequestMapping(method = RequestMethod.GET)
    String home(
		@RequestParam(value = "guest_session", defaultValue = "") String guest_session,
		@RequestParam(value = "raw_time_received", defaultValue = "") String raw_time_received,
		@RequestParam(value = "results_per_page", defaultValue = "") String results_per_page,
		@RequestParam(value = "page_number", defaultValue = "") String page_number
			   ) {
		
		Connection use_open_connection;
		
		String search_guest_session = "";
		String return_string = "";
		
		try {
		
		use_open_connection = Config.openConnection();
		
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
        Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
        Control_Change_Shopping_Cart_Items.guest_session = "b577ca16f3ba02c697cfb22b8baa0ef6e441fcaec4154d294b98c71e8f9fdd2d";
        Control_Change_Shopping_Cart_Items.raw_time_received = raw_time_received;
        Control_Change_Shopping_Cart_Items.date_received = String.valueOf(localDate);
        Control_Change_Shopping_Cart_Items.time_received = String.valueOf(time_format.format(localTime));
		
        search_guest_session = Control_Change_Shopping_Cart_Items.control_search_guest_session();
        
        if (search_guest_session.equals("success")) {
            
            Control_Search_Shopping_Cart_Items.results_per_page = results_per_page;
            Control_Search_Shopping_Cart_Items.page_number = page_number;
        } else {

            Control_Search_Shopping_Cart_Items.results_per_page = "10";
            Control_Search_Shopping_Cart_Items.page_number = "1";
        }
        
        /*return_string = "{\"shopping_cart_items_count\": ";
        return_string += "\"" + Control_Search_Shopping_Cart_Items.control_search_for_guest_shopping_cart_total_quantity() + "\",";
        return_string += " \"shopping_cart_price_total\": ";
        return_string += Control_Search_Shopping_Cart_Items.control_search_for_guest_shopping_cart_total_price() + ",";
        return_string += " \"all_shopping_cart_items\": ";
        return_string += Control_Search_Shopping_Cart_Items.control_search_for_all_guest_shopping_cart_items() + ",";
        return_string += " \"shopping_cart_items\": ";
        return_string += Control_Search_Shopping_Cart_Items.control_search_for_guest_shopping_cart_items() + ",";
        return_string += " \"pages\": ";
        return_string += Control_Search_Shopping_Cart_Items.control_calculate_page_number_count() + "}";*/

        try {
            
            use_open_connection.close();
        } catch (Exception e) {
        }
		} catch (IOException e) {
			
			return_string += "";
		}
		
		return return_string;
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Search_Shopping_Cart.class, args);
    }   
}
