//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_For_Sale_Item_Details;
import java.io.IOException;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://www.timothysdigitalsolutions.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/product-reviews-interface")
public class Product_Reviews_Interface {

	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "results_per_page", defaultValue = "") String results_per_page,
		@RequestParam(value = "page_number", defaultValue = "") String page_number,
		@RequestParam(value = "sort_by", defaultValue = "") String sort_by
			   ) {
		
		Connection use_open_connection;
		
		try {
		
		use_open_connection = Config.openConnection();
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
		Control_Search_For_Sale_Item_Details.use_connection = use_open_connection;
		Control_Search_For_Sale_Item_Details.item_id = item_id;
		Control_Search_For_Sale_Item_Details.results_per_page = results_per_page;
		Control_Search_For_Sale_Item_Details.page_number = page_number;
		Control_Search_For_Sale_Item_Details.sort_by = sort_by;
		
        return "{\"sale_product_details\": " +
			Control_Search_For_Sale_Item_Details.control_search_for_sale_item_details() + "," +
			" \"sale_product_reviews\": " +
			Control_Search_For_Sale_Item_Details.control_search_for_sale_item_reviews() + "," +
			" \"pages\": " +
			Control_Search_For_Sale_Item_Details.control_calculate_page_number_count() + "}";
		} catch (IOException e) {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Product_Reviews_Interface.class, args);
    }
}
