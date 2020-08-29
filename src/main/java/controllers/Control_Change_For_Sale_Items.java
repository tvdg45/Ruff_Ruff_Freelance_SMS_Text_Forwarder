//Author: Timothy van der Graaff
package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import utilities.Find_and_replace;

public class Control_Change_For_Sale_Items extends models.Change_For_Sale_Items {

    //global variables
    public static Connection use_connection;
    public static String row_id;
    public static String item_id;
    public static String item;
    public static String thumbnail;
    public static String[] category;
    public static String description;
    public static String price;
    public static String inventory;
    public static String[] external_id;
    public static String date_received;
    public static String time_received;
    public static String add_item_for_sale;
    public static String change_item_for_sale;
    public static String delete_item_for_sale;
    public static String add_sale_item_pictures;
    public static String delete_sale_item_pictures;
    
    public static void control_add_sale_categories() {
        
        connection = use_connection;
        
        set_category(category);
        set_external_id(external_id);
        set_date_received(date_received);
        set_time_received(time_received);
        
        add_sale_categories();
    }
    
    public static void control_change_sale_categories() {
        
        connection = use_connection;
        
        set_category(category);
        set_external_id(external_id);
        
        change_sale_categories();
    }
    
    public static void control_delete_sale_categories() {
        
        connection = use_connection;
        
        set_external_id(external_id);
        
        delete_sale_categories();
    }
    
    public static String control_add_item_for_sale() {
        
        String output = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("&apos;");
        replace.add("\'");
        
        if (add_item_for_sale.equals("Add item")) {
            
            connection = use_connection;
            
            set_item(Find_and_replace.find_and_replace(find, replace, item));
            set_thumbnail(thumbnail);
            set_category(category);
            set_description(Find_and_replace.find_and_replace(find, replace, description));
            set_price(price);
            set_inventory(inventory);
            set_date_received(date_received);
            set_time_received(time_received);
            
            if (add_item_for_sale().equals("success")) {
                
                output = "success";
            } else {
                
                output = "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    public static String control_change_item_for_sale() {
        
        String output = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("&apos;");
        replace.add("\'");
        
        if (change_item_for_sale.equals("Change item")) {
            
            connection = use_connection;
            
            set_row_id(row_id);
            set_item(Find_and_replace.find_and_replace(find, replace, item));
            set_thumbnail(thumbnail);
            set_category(category);
            set_description(Find_and_replace.find_and_replace(find, replace, description));
            set_price(price);
            set_inventory(inventory);
            
            if (change_item_for_sale().equals("success")) {
                
                output = "success";
            } else {
                
                output = "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    public static String control_delete_item_for_sale() {
        
        String output = "";
        
        if (delete_item_for_sale.equals("Delete item")) {
            
            connection = use_connection;
            
            set_row_id(row_id);
            
            if (delete_item_for_sale().equals("success")) {
                
                set_item_id(row_id);
                
                if (delete_sale_item_pictures_by_item().equals("success")) {
                    
                    output = "success";
                } else {
                    
                    output = "fail";
                }
            } else {
                
                output = "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    public static String control_add_sale_item_pictures() {
        
        String output = "";
        
        if (add_sale_item_pictures.equals("Add picture")) {
            
            connection = use_connection;
            
            set_item_id(item_id);
            set_thumbnail(thumbnail);
            set_date_received(date_received);
            set_time_received(time_received);
            
            if (add_sale_item_pictures().equals("success")) {
                
                output = "success";
            } else {
                
                output = "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output; 
    }
    
    public static String control_delete_sale_item_pictures() {
        
        String output = "";
        
        if (delete_sale_item_pictures.equals("Delete picture")) {
            
            connection = use_connection;
            
            set_row_id(row_id);
            
            if (delete_sale_item_pictures().equals("success")) {
                
                output = "success";
            } else {
                
                output = "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output; 
    }
}
