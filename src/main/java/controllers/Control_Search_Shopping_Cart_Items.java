//Author: Timothy van der Graaff
package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import utilities.Form_Validation;
import views.Show_Shopping_Cart_Items;

public class Control_Search_Shopping_Cart_Items extends models.Search_Shopping_Cart_Items {
   
    //global variables
    public static Connection use_connection;
    public static String results_per_page;
    public static String page_number;
    public static String guest_session;
    
    public static String control_search_for_all_guest_shopping_cart_items() {
        
        String output;
        
        connection = use_connection;
        
        set_guest_session(guest_session);
        
        Show_Shopping_Cart_Items.all_guest_shopping_cart_items = search_for_all_guest_shopping_cart_items();
        
        output = Show_Shopping_Cart_Items.show_all_guest_shopping_cart_items();
        
        return output;
    }
    
    public static String control_search_for_guest_shopping_cart_items() {
        
        String output;
        
        connection = use_connection;
        
        if (Form_Validation.is_string_null_or_white_space(results_per_page) || results_per_page.equals("null")) {
            
            results_per_page = "10";
        }
        
        if (!(results_per_page.equals("10")) && !(results_per_page.equals("20"))
            && !(results_per_page.equals("30")) && !(results_per_page.equals("40"))
            && !(results_per_page.equals("50"))) {
            
            results_per_page = "10";
        }
        
        if (Form_Validation.is_string_null_or_white_space(page_number) || page_number.equals("null")) {
            
            page_number = "1";
        }
        
        set_guest_session(guest_session);
        set_results_per_page(results_per_page);
        set_page_number(page_number);
        
        Show_Shopping_Cart_Items.shopping_cart_items = search_guest_shopping_cart();
        
        output = Show_Shopping_Cart_Items.show_shopping_cart_items();
        
        return output;
    }
    
    public static String control_search_for_guest_shopping_cart_total_price() {
        
        String output;
        
        connection = use_connection;
        
        set_guest_session(guest_session);
        
        output = String.format("%.2f", search_for_guest_shopping_cart_total_price());
        
        return output;
    }
    
    public static String control_search_for_guest_shopping_cart_total_quantity() {
        
        String output;
        
        connection = use_connection;
        
        set_guest_session(guest_session);
        
        output = String.valueOf(search_for_guest_shopping_cart_total_quantity());
        
        return output;
    }
    
    public static String control_calculate_page_number_count() {
        
        String output;
        
        connection = use_connection;
        
        if ((Form_Validation.is_string_null_or_white_space(results_per_page) || results_per_page.equals("null"))) {
            
            results_per_page = "10";
        }
        
        if (!(results_per_page.equals("10")) && !(results_per_page.equals("20"))
            && !(results_per_page.equals("30")) && !(results_per_page.equals("40"))
            && !(results_per_page.equals("50"))) {
            
            results_per_page = "10";
        }
        
        set_guest_session(guest_session);
        set_results_per_page(results_per_page);
        
        Show_Shopping_Cart_Items.page_number_count = calculate_page_number_count(search_for_guest_shopping_cart_items_count());
        
        output = Show_Shopping_Cart_Items.show_page_numbers();
        
        try {
            
            use_connection.close();
        } catch (SQLException e) {
        }
        
        return output;
    }
}