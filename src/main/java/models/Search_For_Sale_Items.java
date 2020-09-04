//Author: Timothy van der Graaff
package models;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Search_For_Sale_Items {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String category;
    private static String keywords;
    private static String results_per_page;
    private static String page_number;
    
    //mutators
    protected static void set_category(String this_category) {
        
        category = this_category;
    }
    
    protected static void set_keywords(String this_keywords) {
        
        keywords = this_keywords;
    }
    
    protected static void set_results_per_page(String this_results_per_page) {
        
        results_per_page = this_results_per_page;
    }
    
    protected static void set_page_number(String this_page_number) {
        
        page_number = this_page_number;
    }
    
    //accessors
    private static String get_category() {
        
        return category;
    }
    
    private static String get_keywords() {
        
        return keywords;
    }
    
    private static String get_results_per_page() {
        
        return results_per_page;
    }
    
    private static String get_page_number() {
        
        return page_number;
    }
    
    
    
    
    
    
    private static void create_new_sale_items_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_items_for_sale " +
                    "(row_id INT NOT NULL, item TEXT NOT NULL, " +
                    "thumbnail TEXT NOT NULL, category TEXT NOT NULL, " +
                    "description TEXT NOT NULL, price DECIMAL(10,2) NOT NULL, " +
                    "inventory INT NOT NULL, date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    private static void create_new_sale_categories_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_sale_categories " +
                    "(row_id INT NOT NULL, category TEXT NOT NULL, external_id TEXT NOT NULL, " +
                    "date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_sale_categories' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    protected static ArrayList<String> search_sale_categories() {
        
        ArrayList<String> output = new ArrayList<>();
        
        int sale_category_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT category FROM " +
                    "company_sale_categories ORDER BY category ASC");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output.add(select_results.getString(1));
                
                sale_category_count++;
            }
            
            if (sale_category_count == 0) {
                
                output.add("no category");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_sale_categories' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_categories_table();
            
            output.add("fail");
        }
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_for_sale_items_price_low_to_high() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> item = new ArrayList<>();
        ArrayList<String> thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> inventory = new ArrayList<>();
        
        int sale_item_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item, thumbnail, category, " +
                    "description, price, inventory FROM company_items_for_sale WHERE category LIKE ? AND item LIKE ? " +
                    "ORDER BY price ASC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setString(1, "%" + get_category() + "%");
            select_statement.setString(2, "%" + get_keywords() + "%");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                item.add(select_results.getString(2));
                thumbnail.add(select_results.getString(3));
                item_category.add(select_results.getString(4));
                description.add(select_results.getString(5));
                price.add(select_results.getString(6));
                inventory.add(String.valueOf(select_results.getInt(7)));
                
                sale_item_count++;
            }
            
            if (sale_item_count == 0) {
                
                row_id.add("0");
                item.add("no item");
                thumbnail.add("no item");
                item_category.add("no item");
                description.add("no item");
                price.add("no item");
                inventory.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_items_table();
            
            row_id.add("0");
            item.add("fail");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            inventory.add("0");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            item.add("fail");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            inventory.add("0");
        }
        
        output.add(row_id);
        output.add(item);
        output.add(thumbnail);
        output.add(item_category);
        output.add(description);
        output.add(price);
        output.add(inventory);
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_for_sale_items_price_high_to_low() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> item = new ArrayList<>();
        ArrayList<String> thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> inventory = new ArrayList<>();
        
        int sale_item_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item, thumbnail, category, " +
                    "description, price, inventory FROM company_items_for_sale WHERE category LIKE ? AND item LIKE ? " +
                    "ORDER BY price DESC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setString(1, "%" + get_category() + "%");
            select_statement.setString(2, "%" + get_keywords() + "%");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                item.add(select_results.getString(2));
                thumbnail.add(select_results.getString(3));
                item_category.add(select_results.getString(4));
                description.add(select_results.getString(5));
                price.add(select_results.getString(6));
                inventory.add(String.valueOf(select_results.getInt(7)));
                
                sale_item_count++;
            }
            
            if (sale_item_count == 0) {
                
                row_id.add("0");
                item.add("no item");
                thumbnail.add("no item");
                item_category.add("no item");
                description.add("no item");
                price.add("no item");
                inventory.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_items_table();
            
            row_id.add("0");
            item.add("fail");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            inventory.add("0");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            item.add("fail");
            thumbnail.add("fail");
            item_category.add("fail");
            description.add("fail");
            price.add("fail");
            inventory.add("0");
        }
        
        output.add(row_id);
        output.add(item);
        output.add(thumbnail);
        output.add(item_category);
        output.add(description);
        output.add(price);
        output.add(inventory);
        
        return output;
    }
    
    protected static int search_for_sale_items_count() {
        
        int output;
        int sale_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id FROM company_items_for_sale " +
                    "WHERE category LIKE ? AND item LIKE ? ORDER BY price DESC");
            
            select_statement.setString(1, "%" + get_category() + "%");
            select_statement.setString(2, "%" + get_keywords() + "%");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                sale_item_count++;
            }
            
            output = sale_item_count;
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
