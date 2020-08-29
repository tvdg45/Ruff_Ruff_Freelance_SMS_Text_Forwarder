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
@RequestMapping("/add-to-cart")
public class Add_To_Cart {

	@RequestMapping(method = RequestMethod.POST)
    String home(
		@RequestParam(value = "item", defaultValue = "") String item,
		@RequestParam(value = "thumbnail", defaultValue = "") String thumbnail,
		@RequestParam(value = "category", defaultValue = "") String category,
		@RequestParam(value = "description", defaultValue = "") String description,
		@RequestParam(value = "price", defaultValue = "") String price,
		@RequestParam(value = "quantity", defaultValue = "") String quantity,
		@RequestParam(value = "raw_time_received", defaultValue = "") String raw_time_received,
		@RequestParam(value = "guest_session", defaultValue = "") String guest_session,
		@RequestParam(value = "item_id", defaultValue = "") String item_id,
		@RequestParam(value = "add_to_cart", defaultValue = "") String add_to_cart
			   ) {
		
		/*Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
            Control_Change_Shopping_Cart_Items.item = String.valueOf(request.getParameter("item"));
            Control_Change_Shopping_Cart_Items.thumbnail = String.valueOf(request.getParameter("thumbnail"));
            Control_Change_Shopping_Cart_Items.category = String.valueOf(request.getParameter("category"));
            Control_Change_Shopping_Cart_Items.description = String.valueOf(request.getParameter("description"));
            Control_Change_Shopping_Cart_Items.price = String.valueOf(request.getParameter("price"));
            Control_Change_Shopping_Cart_Items.quantity = String.valueOf(request.getParameter("quantity")).split(",");
            Control_Change_Shopping_Cart_Items.raw_time_received = String.valueOf(request.getParameter("raw_time_received"));
            Control_Change_Shopping_Cart_Items.guest_session = String.valueOf(request.getParameter("guest_session"));
            Control_Change_Shopping_Cart_Items.item_id = String.valueOf(request.getParameter("item_id")).split(",");
            Control_Change_Shopping_Cart_Items.date_received = String.valueOf(localDate);
            Control_Change_Shopping_Cart_Items.time_received = String.valueOf(time_format.format(localTime));
            Control_Change_Shopping_Cart_Items.add_to_cart = String.valueOf(request.getParameter("add_to_cart"));*/
        return Control_Change_Shopping_Cart_Items.control_add_to_cart();
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Add_To_Cart.class, args);
    }
}
