//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;
import java.io.PrintWriter;

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
@RequestMapping("/create-receipt")
public class Create_Receipt {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "guest_session", defaultValue = "") String guest_session,
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "create_receipt", defaultValue = "") String create_receipt
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
		Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
		Control_Change_Shopping_Cart_Items.guest_session = guest_session;
		Control_Change_Shopping_Cart_Items.item_id = item_id.split(",");
		Control_Change_Shopping_Cart_Items.date_received = String.valueOf(localDate);
		Control_Change_Shopping_Cart_Items.time_received = String.valueOf(time_format.format(localTime));
		Control_Change_Shopping_Cart_Items.create_receipt = create_receipt;
		
		if (create_receipt.equals("Create receipt")) {
			
			return Control_Change_Shopping_Cart_Items.control_create_receipt();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Create_Receipt.class, args);
    }
}
