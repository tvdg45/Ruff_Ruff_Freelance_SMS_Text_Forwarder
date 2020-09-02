//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

//import java.io.IOException;
//import java.io.PrintWriter;

import java.sql.Connection;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-extract-receipts")
public class Admin_Extract_Receipts {

	@RequestMapping(method = RequestMethod.POST)
    String home() {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
		
		Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
		
		return "{\"receipts\": " +
			Control_Change_Shopping_Cart_Items.control_search_receipts() + "," +
			" \"items_sold\": " +
			Control_Change_Shopping_Cart_Items.control_search_items_sold() + "}";
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Admin_Extract_Receipts.class, args);
    }
}
