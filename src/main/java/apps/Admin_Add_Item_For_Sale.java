//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_For_Sale_Items;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;

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
@RequestMapping("/admin-add-item-for-sale")
public class Admin_Add_Item_For_Sale {
    
    @RequestMapping(method = RequestMethod.POST)
    String home(
            @RequestParam(value = "item", defaultValue = "") String item,
            @RequestParam(value = "thumbnail", defaultValue = "") String thumbnail,
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "price", defaultValue = "") String price,
            @RequestParam(value = "inventory", defaultValue = "") String inventory,
            @RequestParam(value = "add_item_for_sale", defaultValue = "") String add_item_for_sale
    ) {
        
        Connection use_open_connection;
        
        try {
            
            use_open_connection = Config.openConnection();
            
            DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
            
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
            
            Control_Change_For_Sale_Items.use_connection = use_open_connection;
            Control_Change_For_Sale_Items.item = item;
            Control_Change_For_Sale_Items.thumbnail = thumbnail;
            Control_Change_For_Sale_Items.category = category.split(",");
            Control_Change_For_Sale_Items.description = description;
            Control_Change_For_Sale_Items.price = price;
            Control_Change_For_Sale_Items.inventory = inventory;
            Control_Change_For_Sale_Items.date_received = String.valueOf(localDate);
            Control_Change_For_Sale_Items.time_received = String.valueOf(time_format.format(localTime));
            Control_Change_For_Sale_Items.add_item_for_sale = add_item_for_sale;
            
            if (add_item_for_sale.equals("Add item")) {
                
                return Control_Change_For_Sale_Items.control_add_item_for_sale();
            } else {
                
                return "";
            }
        } catch (IOException e) {
            
            return "";
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Add_Item_For_Sale.class, args);
    }
}
