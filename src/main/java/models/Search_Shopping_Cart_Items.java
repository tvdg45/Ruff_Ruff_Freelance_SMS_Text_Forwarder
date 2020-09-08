//Author: Timothy van der Graaff
package models;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Search_Shopping_Cart_Items {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String guest_session;
    private static String results_per_page;
    private static String page_number;
    
    //mutators
    protected static void set_guest_session(String this_guest_session) {
       
        guest_session = this_guest_session;
    }
    
    protected static void set_results_per_page(String this_results_per_page) {
        
        results_per_page = this_results_per_page;
    }
    
    protected static void set_page_number(String this_page_number) {
        
        page_number = this_page_number;
    }
    
    //accessors
    private static String get_guest_session() {
        
        return guest_session;
    }
    
    private static String get_results_per_page() {
        
        return results_per_page;
    }
    
    private static String get_page_number() {
        
        return page_number;
    }
    
    
    
    
    
    
    private static void create_new_shopping_cart_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_shopping_cart_items " +
                    "(row_id INT NOT NULL, item TEXT NOT NULL, " +
                    "thumbnail TEXT NOT NULL, category TEXT NOT NULL, " +
                    "description TEXT NOT NULL, price DECIMAL(10,2) NOT NULL, " +
                    "quantity INT NOT NULL, raw_time_received TEXT NOT NULL, session TEXT NOT NULL, " +
                    "item_id INT NOT NULL, date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    //Guest must have an active session, so he or she can hold items in his or her cart.
    protected static ArrayList<ArrayList<String>> search_guest_shopping_cart() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> item = new ArrayList<>();
        ArrayList<String> thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> quantity = new ArrayList<>();
        ArrayList<String> item_id = new ArrayList<>();
        
        int shopping_cart_item_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item, thumbnail, category, " +
                    "description, price, quantity, item_id FROM company_shopping_cart_items WHERE session = ? " +
                    "ORDER BY row_id DESC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                item.add(select_results.getString(2));
                thumbnail.add(select_results.getString(3));
                item_category.add(select_results.getString(4));
                description.add(select_results.getString(5));
                price.add(String.format("%.2f", select_results.getDouble(6)));
                quantity.add(String.valueOf(select_results.getInt(7)));
                item_id.add(String.valueOf(select_results.getInt(8)));
                
                shopping_cart_item_count++;
            }
            
            if (shopping_cart_item_count == 0) {
                
                row_id.add("0");
                item.add("no item");
                thumbnail.add("no item");
                item_category.add("no item");
                description.add("no item");
                price.add("no item");
                quantity.add("0");
                item_id.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_shopping_cart_table();
            
            row_id.add("0");
            item.add("fail 1");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            quantity.add("0");
            item_id.add("0");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            item.add("fail 2");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            quantity.add("0");
            item_id.add("0");
        }
        
        output.add(row_id);
        output.add(item);
        output.add(thumbnail);
        output.add(item_category);
        output.add(description);
        output.add(price);
        output.add(quantity);
        output.add(item_id);
        
        return output;
    }
    
    //This method searches all items for the customer's receipt.
    protected static ArrayList<ArrayList<String>> search_for_all_guest_shopping_cart_items() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> item = new ArrayList<>();
        ArrayList<String> thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> quantity = new ArrayList<>();
        ArrayList<String> item_id = new ArrayList<>();
        
        int shopping_cart_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id, item, thumbnail, category, " +
                    "description, price, quantity, item_id FROM company_shopping_cart_items WHERE session = ? " +
                    "ORDER BY row_id ASC");
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                item.add(select_results.getString(2));
                thumbnail.add(select_results.getString(3));
                item_category.add(select_results.getString(4));
                description.add(select_results.getString(5));
                price.add(String.format("%.2f", select_results.getDouble(6)));
                quantity.add(String.valueOf(select_results.getInt(7)));
                item_id.add(String.valueOf(select_results.getInt(8)));
                
                shopping_cart_item_count++;
            }
            
            if (shopping_cart_item_count == 0) {
                
                row_id.add("0");
                item.add("no item");
                thumbnail.add("no item");
                item_category.add("no item");
                description.add("no item");
                price.add("no item");
                quantity.add("0");
                item_id.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_shopping_cart_table();
            
            row_id.add("0");
            item.add("fail");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            quantity.add("0");
            item_id.add("0");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            item.add("fail");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            quantity.add("0");
            item_id.add("0");
        }
        
        output.add(row_id);
        output.add(item);
        output.add(thumbnail);
        output.add(item_category);
        output.add(description);
        output.add(price);
        output.add(quantity);
        output.add(item_id);
        
        return output;
    }
    
    protected static double search_for_guest_shopping_cart_total_price() {

        double output = 0;
        int shopping_cart_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT price, quantity FROM company_shopping_cart_items " +
                    "WHERE session = ? ORDER BY row_id DESC");
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output += (select_results.getDouble(1) * select_results.getInt(2));
                
                shopping_cart_item_count++;
            }
            
            if (shopping_cart_item_count == 0) {
                
                output = shopping_cart_item_count;                
            }
        } catch (SQLException e) {
            
            output = 0;
        }
        
        return output;
    }
    
    protected static int search_for_guest_shopping_cart_total_quantity() {

        int output = 0;
        int shopping_cart_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT quantity FROM company_shopping_cart_items " +
                    "WHERE session = ? ORDER BY row_id DESC");
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output += select_results.getInt(1);
                
                shopping_cart_item_count++;
            }
            
            if (shopping_cart_item_count == 0) {
                
                output = shopping_cart_item_count;                
            }
        } catch (SQLException e) {
            
            output = 0;
        }
        
        return output;
    }
    
    protected static int search_for_guest_shopping_cart_items_count() {
        
        int output;
        int shopping_cart_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id FROM company_shopping_cart_items " +
                    "WHERE session = ? ORDER BY row_id DESC");
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                shopping_cart_item_count++;
            }
            
            output = shopping_cart_item_count;
        } catch (SQLException e) {
            
            output = 0;
        }
        
        return output;
    }
    
    protected static int calculate_page_number_count(int result_count) {
        
        int output;
        int remainder;
        int get_results_per_page;
        
        try {
            
            get_results_per_page = Integer.parseInt(get_results_per_page());
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            get_results_per_page = 10;
        }
        
        remainder = result_count % get_results_per_page;
        output = (result_count - remainder) / get_results_per_page;
        
        //Add an extra page for overflowing records.
        if (remainder > 0) {
            
            output += 1;
        }
        
        return output;
    }
}
