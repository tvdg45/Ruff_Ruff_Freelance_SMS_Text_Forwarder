//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Search_For_Sale_Item_Details;
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
@RequestMapping("/admin-product-details-interface")
public class Admin_Product_Details_Interface {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "item_id", defaultValue = "") String item_id
    ) {
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            Control_Search_For_Sale_Item_Details.use_connection = use_open_connection;
            Control_Search_For_Sale_Item_Details.item_id = item_id;
            Control_Search_For_Sale_Item_Details.results_per_page = "10";
            Control_Search_For_Sale_Item_Details.page_number = "1";
            Control_Search_For_Sale_Item_Details.sort_by = "Newest";
            
            return "{\"sale_product_details\": " +
                    Control_Search_For_Sale_Item_Details.control_search_for_sale_item_details() + "," +
                    " \"sale_product_additional_pictures\": " +
                    Control_Search_For_Sale_Item_Details.control_search_for_sale_item_additional_pictures() + "," +
                    " \"sale_product_reviews\": " +
                    Control_Search_For_Sale_Item_Details.control_search_for_sale_item_reviews() + "}";
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Product_Details_Interface.class, args);
    }
}
