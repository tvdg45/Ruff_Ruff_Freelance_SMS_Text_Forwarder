//Author: Timothy van der Graaff
package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import utilities.Form_Validation;
import views.Show_For_Sale_Items;

public class Control_Search_For_Sale_Items extends models.Search_For_Sale_Items {
    
    //global variables
    public static Connection use_connection;
    public static String category;
    public static String keywords;
    public static String results_per_page;
    public static String page_number;
    public static String sort_by;
    public static String search_items;
    
    public static String control_search_for_sale_categories() {
        
        String output = "";
        
        connection = use_connection;
        
        Show_For_Sale_Items.for_sale_categories = search_sale_categories();
        
        output += Show_For_Sale_Items.show_for_sale_categories();
        
        return output;
    }
    
    public static String control_search_for_sale_items() {
        
        String output;
        
        connection = use_connection;
        
        if (Form_Validation.is_string_null_or_white_space(category) || category.equals("null")) {
            
            category = "";
        }
        
        if (Form_Validation.is_string_null_or_white_space(keywords) || keywords.equals("null")) {
            
            keywords = "";
        }
        
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
        
        set_category(category);
        set_keywords(keywords);
        set_results_per_page(results_per_page);
        set_page_number(page_number);
        
        if (sort_by.equals("High to low") && search_items.equals("Search")) {
            
            Show_For_Sale_Items.for_sale_items = search_for_sale_items_price_high_to_low();
        } else {
            
            Show_For_Sale_Items.for_sale_items = search_for_sale_items_price_low_to_high();
        }
        
        output = Show_For_Sale_Items.show_for_sale_items();
        
        return output;
    }
    
    public static String control_calculate_page_number_count() {
        
        String output;
        
        connection = use_connection;
        
        if (Form_Validation.is_string_null_or_white_space(category) || category.equals("null")) {
            
            category = "";
        }
        
        if (Form_Validation.is_string_null_or_white_space(keywords) || keywords.equals("null")) {
            
            keywords = "";
        }
        
        if ((Form_Validation.is_string_null_or_white_space(results_per_page) || results_per_page.equals("null"))) {
            
            results_per_page = "10";
        }
        
        if (!(results_per_page.equals("10")) && !(results_per_page.equals("20"))
            && !(results_per_page.equals("30")) && !(results_per_page.equals("40"))
            && !(results_per_page.equals("50"))) {
            
            results_per_page = "10";
        }
        
        set_category(category);
        set_keywords(keywords);
        set_results_per_page(results_per_page);
        
        Show_For_Sale_Items.page_number_count = calculate_page_number_count(search_for_sale_items_count());
        
        output = Show_For_Sale_Items.show_page_numbers();
        
        try {
            
            use_connection.close();
        } catch (SQLException e) {
        }
        
        return output;
    }
}