//Author: Timothy van der Graaff
package apps;

import controllers.Control_Change_For_Sale_Items;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import utilities.Find_and_replace;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/admin-change-for-sale-item-categories")
public class Admin_Change_For_Sale_Item_Categories {

	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "category", defaultValue = "") String category,
		@RequestParam(value = "external_id", defaultValue = "") String external_id,
		@RequestParam(value = "add_categories", defaultValue = "") String add_categories,
		@RequestParam(value = "change_categories", defaultValue = "") String change_categories,
		@RequestParam(value = "delete_categories", defaultValue = "") String delete_categories
			   ) {
		
        Connection use_open_connection;
        
        use_open_connection = configuration.Config.openConnection();
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
  
        Control_Change_For_Sale_Items.use_connection = use_open_connection;
        
        String category_exception = "";
        String external_id_exception = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("(");
        find.add(")");
        find.add("; ");
        
        replace.add("");
        replace.add("");
        replace.add(";");
        
        try {
            
            Control_Change_For_Sale_Items.category = Find_and_replace.find_and_replace(find, replace, category).split(";");
        } catch (NullPointerException e) {
            
            category_exception = "yes";
        }
        
        try {
            
            Control_Change_For_Sale_Items.external_id = Find_and_replace.find_and_replace(find, replace, external_id).split(";");
        } catch (NullPointerException e) {
            
            external_id_exception = "yes";
        }
        
        Control_Change_For_Sale_Items.date_received = String.valueOf(localDate);
        Control_Change_For_Sale_Items.time_received = String.valueOf(time_format.format(localTime));
        
        if (add_categories.equals("Add categories") && !(category_exception.equals("yes") || external_id_exception.equals("yes"))) {
            
            Control_Change_For_Sale_Items.control_add_sale_categories();
        }
        
        if (change_categories.equals("Change categories") && !(category_exception.equals("yes") || external_id_exception.equals("yes"))) {
            
            Control_Change_For_Sale_Items.control_change_sale_categories();
        }
        
        if (delete_categories.equals("Delete categories") && !(external_id_exception.equals("yes"))) {
            
            Control_Change_For_Sale_Items.control_delete_sale_categories();
        }
		
		return "";
	}
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Admin_Change_For_Sale_Item_Categories.class, args);
    }
}
