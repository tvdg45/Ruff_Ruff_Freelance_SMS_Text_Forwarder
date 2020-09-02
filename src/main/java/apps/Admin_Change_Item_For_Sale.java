//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_For_Sale_Items;

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
@RequestMapping("/admin-change-item-for-sale")
public class Admin_Change_Item_For_Sale {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "item", defaultValue = "") String item,
		@RequestParam(value = "thumbnail", defaultValue = "") String thumbnail,
		@RequestParam(value = "category", defaultValue = "") String category,
		@RequestParam(value = "description", defaultValue = "") String description,
		@RequestParam(value = "price", defaultValue = "") String price,
		@RequestParam(value = "inventory", defaultValue = "") String inventory,
		@RequestParam(value = "row_id", defaultValue = "") String row_id,
		@RequestParam(value = "change_item_for_sale", defaultValue = "") String change_item_for_sale
			   ) {
		
        Connection use_open_connection;
        
        if (change_item_for_sale.equals("Change item")) {
			
			try {
				
			use_open_connection = Config.openConnection();
				
            Control_Change_For_Sale_Items.use_connection = use_open_connection;
            Control_Change_For_Sale_Items.item = item;
            Control_Change_For_Sale_Items.thumbnail = thumbnail;
            Control_Change_For_Sale_Items.category = category.split(",");
            Control_Change_For_Sale_Items.description = description;
            Control_Change_For_Sale_Items.price = price;
            Control_Change_For_Sale_Items.inventory = inventory;
            Control_Change_For_Sale_Items.row_id = row_id;
            Control_Change_For_Sale_Items.change_item_for_sale = change_item_for_sale;
            
            return Control_Change_For_Sale_Items.control_change_item_for_sale();
			} catch (IOException e) {
				
				return "";
			}
        } else {
			
			return "";
		}
	}
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Change_Item_For_Sale.class, args);
    }
}
