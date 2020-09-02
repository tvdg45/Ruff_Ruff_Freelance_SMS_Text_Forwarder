//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_For_Sale_Items;
import java.io.IOException;
//import java.io.PrintWriter;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-for-sale-items-interface")
public class Admin_For_Sale_Items_Interface {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "category", defaultValue = "") String category,
		@RequestParam(value = "keywords", defaultValue = "") String keywords,
		@RequestParam(value = "results_per_page", defaultValue = "") String results_per_page,
		@RequestParam(value = "page_number", defaultValue = "") String page_number,
		@RequestParam(value = "sort_by", defaultValue = "") String sort_by,
		@RequestParam(value = "search_items", defaultValue = "") String search_items
			   ) {
		
		Connection use_open_connection;
		
		try {
		use_open_connection = Config.openConnection();
		
		Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
		Control_Change_Shopping_Cart_Items.category = category;
		Control_Change_Shopping_Cart_Items.keywords = keywords;
		Control_Change_Shopping_Cart_Items.category = category;
		Control_Change_Shopping_Cart_Items.results_per_page = results_per_page;
		Control_Change_Shopping_Cart_Items.page_number = page_number;
		Control_Change_Shopping_Cart_Items.sort_by = sort_by;
		Control_Change_Shopping_Cart_Items.search_items = search_items;
		
		return "{\"sale_categories\": " +
			Control_Search_For_Sale_Items.control_search_for_sale_categories() + "," +
			" \"sale_items\": " +
			Control_Search_For_Sale_Items.control_search_for_sale_items() + "," +
			" \"pages\": " +
			Control_Search_For_Sale_Items.control_calculate_page_number_count() + "}";
		} catch (IOException e) {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_For_Sale_Items_Interface.class, args);
    }
}
