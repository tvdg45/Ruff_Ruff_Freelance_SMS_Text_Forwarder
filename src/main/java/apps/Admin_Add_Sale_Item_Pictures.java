//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_For_Sale_Items;

import java.time.LocalDate;
import java.time.LocalTime;

//import java.io.IOException;
//import java.io.PrintWriter;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-add-sale-item-pictures")
public class Admin_Add_Sale_Item_Pictures {
    
	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "thumbnail", defaultValue = "") String thumbnail,
		@RequestParam(value = "add_sale_item_pictures", defaultValue = "") String add_sale_item_pictures
			   ) {
		
		Connection use_open_connection;
		
		use_open_connection = Config.openConnection();
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
		Control_Change_For_Sale_Items.use_connection = use_open_connection;
		Control_Change_For_Sale_Items.item_id = item_id;
		Control_Change_For_Sale_Items.thumbnail = thumbnail;
		Control_Change_For_Sale_Items.date_received = String.valueOf(localDate);
		Control_Change_For_Sale_Items.time_received = String.valueOf(time_format.format(localTime));
		Control_Change_For_Sale_Items.add_sale_item_pictures = add_sale_item_pictures;
		
		if (add_sale_item_pictures.equals("Add picture")) {
			
			return Control_Change_For_Sale_Items.control_add_sale_item_pictures();
		} else {
			
			return "";
		}
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Admin_Add_Sale_Item_Pictures.class, args);
    }
}
