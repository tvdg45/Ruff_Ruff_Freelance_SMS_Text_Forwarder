//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_For_Sale_Items;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-delete-sale-item-pictures")
public class Admin_Delete_Sale_Item_Pictures {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "row_id", defaultValue = "") String row_id,
		@RequestParam(value = "delete_sale_item_pictures", defaultValue = "") String delete_sale_item_pictures
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
        
		Control_Change_For_Sale_Items.use_connection = use_open_connection;
		Control_Change_For_Sale_Items.row_id = row_id;
		Control_Change_For_Sale_Items.delete_sale_item_pictures = delete_sale_item_pictures;
		
		if (delete_sale_item_pictures.equals("Delete picture")) {
			
			return Control_Change_For_Sale_Items.control_delete_sale_item_pictures();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Admin_Delete_Sale_Item_Pictures.class, args);
    }
}
