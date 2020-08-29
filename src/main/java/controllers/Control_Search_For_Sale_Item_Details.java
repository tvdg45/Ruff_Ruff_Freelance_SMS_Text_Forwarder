//Author: Timothy van der Graaff
package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import utilities.Form_Validation;
import views.Show_For_Sale_Item_Details;

public class Control_Search_For_Sale_Item_Details extends models.Search_For_Sale_Item_Details {
    
    //global variables
    public static Connection use_connection;
    public static String item_id;
    public static String results_per_page;
    public static String page_number;
    public static String sort_by;
    
    public static String control_search_for_sale_item_details() {
        
        String output;
        
        connection = use_connection;
        
        set_item_id(item_id);
        
        Show_For_Sale_Item_Details.for_sale_item_details = search_sale_item_details();
        
        output = Show_For_Sale_Item_Details.show_for_sale_item_details();
        
        return output;
    }
    
    public static String control_search_for_sale_item_additional_pictures() {
       
        String output;
        
        connection = use_connection;
        
        set_item_id(item_id);
        
        Show_For_Sale_Item_Details.for_sale_item_additional_pictures = search_sale_item_additional_pictures();
        
        output = Show_For_Sale_Item_Details.show_for_sale_item_additional_pictures();
        
        return output;
    }
    
    public static String control_search_for_sale_item_reviews() {
        
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
        
        set_item_id(item_id);
        set_results_per_page(results_per_page);
        set_page_number(page_number);
        
        switch (sort_by) {
            case "Oldest":
                Show_For_Sale_Item_Details.for_sale_item_reviews = search_sale_item_reviews_oldest_first();
                break;
            case "Highest rating":
                Show_For_Sale_Item_Details.for_sale_item_reviews = search_sale_item_reviews_highest_rating_first();
                break;
            case "Lowest rating":
                Show_For_Sale_Item_Details.for_sale_item_reviews = search_sale_item_reviews_lowest_rating_first();
                break;
            default:
                Show_For_Sale_Item_Details.for_sale_item_reviews = search_sale_item_reviews_newest_first();
                break;
        }
        
        output = Show_For_Sale_Item_Details.show_for_sale_item_reviews();
        
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
        
        set_item_id(item_id);
        set_results_per_page(results_per_page);
        
        Show_For_Sale_Item_Details.page_number_count = calculate_page_number_count(search_for_sale_item_reviews_count());
        
        output = Show_For_Sale_Item_Details.show_page_numbers();
        
        try {
            
            use_connection.close();
        } catch (SQLException e) {
        }
        
        return output;
    }
}