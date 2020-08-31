//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

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
@RequestMapping("/admin-delete-receipts")
public class Admin_Delete_Receipts {

	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "delete_receipts", defaultValue = "") String delete_receipts
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
		
		Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
		Control_Change_Shopping_Cart_Items.delete_receipts = delete_receipts;
		
		if (delete_receipts.equals("Delete receipts")) {
			
			return Control_Change_Shopping_Cart_Items.control_delete_receipts();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Admin_Delete_Receipts.class, args);
    }
}
