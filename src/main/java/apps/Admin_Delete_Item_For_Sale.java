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
@RequestMapping("/admin-delete-item-for-sale")
public class Admin_Delete_Item_For_Sale {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "row_id", defaultValue = "") String row_id,
		@RequestParam(value = "delete_item_for_sale", defaultValue = "") String delete_item_for_sale
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
		
		Control_Change_For_Sale_Items.use_connection = use_open_connection;
		Control_Change_For_Sale_Items.row_id = row_id;
		Control_Change_For_Sale_Items.delete_item_for_sale = delete_item_for_sale;
		
		if (delete_item_for_sale.equals("Delete item")) {
			
			return Control_Change_For_Sale_Items.control_delete_item_for_sale();
		} else {
			
			return "";
		}
    }

    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Delete_Item_For_Sale.class, args);
    }
}
