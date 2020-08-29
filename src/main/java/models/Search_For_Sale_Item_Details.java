//Author: Timothy van der Graaff
package models;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Search_For_Sale_Item_Details {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String item_id;
    private static String results_per_page;
    private static String page_number;
    
    //mutators
    protected static void set_item_id(String this_item_id) {
        
        item_id = this_item_id;
    }
    
    protected static void set_results_per_page(String this_results_per_page) {
        
        results_per_page = this_results_per_page;
    }
    
    protected static void set_page_number(String this_page_number) {
        
        page_number = this_page_number;
    }
    
    //accessors
    private static String get_item_id() {
        
        return item_id;
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
    
    private static void create_new_sale_item_pictures_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_item_pictures " +
                    "(row_id INT NOT NULL, item_id INT NOT NULL, " +
                    "thumbnail TEXT NOT NULL, date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_pictures' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    private static void create_new_sale_item_reviews_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_item_reviews " +
                    "(row_id INT NOT NULL, item_id INT NOT NULL, rating INT NOT NULL, subject TEXT NOT NULL, " +
                    "description TEXT NOT NULL, name TEXT NOT NULL, email TEXT NOT NULL, security_code TEXT NOT NULL, " +
                    "visible TEXT NOT NULL, date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
    protected static ArrayList<ArrayList<String>> search_sale_item_details() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> item = new ArrayList<>();
        ArrayList<String> thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<String> inventory = new ArrayList<>();
        
        int sale_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id, item, thumbnail, category, " +
                    "description, price, inventory FROM company_items_for_sale WHERE row_id = ? " +
                    "ORDER BY row_id ASC");
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            
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
            
            if (sale_item_count != 1) {
                
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
    
    protected static ArrayList<ArrayList<String>> search_sale_item_additional_pictures() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> thumbnail = new ArrayList<>();
        
        int sale_item_picture_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id, thumbnail FROM " +
                    "company_item_pictures WHERE item_id = ? ORDER BY row_id ASC");
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                thumbnail.add(select_results.getString(2));
                
                sale_item_picture_count++;
            }
            
            if (sale_item_picture_count == 0) {
                
                row_id.add("0");
                thumbnail.add("no picture");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_pictures' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_pictures_table();
            
            row_id.add("0");
            thumbnail.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            thumbnail.add("fail");
        }
        
        output.add(row_id);
        output.add(thumbnail);
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_sale_item_reviews_oldest_first() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> use_item_id = new ArrayList<>();
        ArrayList<String> rating = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> date_received = new ArrayList<>();
        
        int sale_item_review_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item_id, rating, subject, description, name, " +
                    "date_received FROM company_item_reviews WHERE item_id = ? AND visible = ? " +
                    "ORDER BY row_id ASC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            select_statement.setString(2, "yes");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                use_item_id.add(String.valueOf(select_results.getInt(2)));
                rating.add(String.valueOf(select_results.getInt(3)));
                subject.add(select_results.getString(4));
                description.add(select_results.getString(5));
                name.add(select_results.getString(6));
                date_received.add(select_results.getString(7));
                
                sale_item_review_count++;
            }
            
            if (sale_item_review_count == 0) {
                
                row_id.add("0");
                use_item_id.add("0");
                rating.add("0");
                subject.add("no review");
                description.add("no review");
                name.add("no review");
                date_received.add("no review");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_reviews_table();
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        }
        
        output.add(row_id);
        output.add(use_item_id);
        output.add(rating);
        output.add(subject);
        output.add(description);
        output.add(name);
        output.add(date_received);
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_sale_item_reviews_newest_first() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> use_item_id = new ArrayList<>();
        ArrayList<String> rating = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> date_received = new ArrayList<>();
        
        int sale_item_review_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item_id, rating, subject, description, name, " +
                    "date_received FROM company_item_reviews WHERE item_id = ? AND visible = ? " +
                    "ORDER BY row_id DESC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            select_statement.setString(2, "yes");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                use_item_id.add(String.valueOf(select_results.getInt(2)));
                rating.add(String.valueOf(select_results.getInt(3)));
                subject.add(select_results.getString(4));
                description.add(select_results.getString(5));
                name.add(select_results.getString(6));
                date_received.add(select_results.getString(7));
                
                sale_item_review_count++;
            }
            
            if (sale_item_review_count == 0) {
                
                row_id.add("0");
                use_item_id.add("0");
                rating.add("0");
                subject.add("no review");
                description.add("no review");
                name.add("no review");
                date_received.add("no review");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_reviews_table();
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        }
        
        output.add(row_id);
        output.add(use_item_id);
        output.add(rating);
        output.add(subject);
        output.add(description);
        output.add(name);
        output.add(date_received);
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_sale_item_reviews_highest_rating_first() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> use_item_id = new ArrayList<>();
        ArrayList<String> rating = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> date_received = new ArrayList<>();
        
        int sale_item_review_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item_id, rating, subject, description, name, " +
                    "date_received FROM company_item_reviews WHERE item_id = ? AND visible = ? " +
                    "ORDER BY rating DESC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            select_statement.setString(2, "yes");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                use_item_id.add(String.valueOf(select_results.getInt(2)));
                rating.add(String.valueOf(select_results.getInt(3)));
                subject.add(select_results.getString(4));
                description.add(select_results.getString(5));
                name.add(select_results.getString(6));
                date_received.add(select_results.getString(7));
                
                sale_item_review_count++;
            }
            
            if (sale_item_review_count == 0) {
                
                row_id.add("0");
                use_item_id.add("0");
                rating.add("0");
                subject.add("no review");
                description.add("no review");
                name.add("no review");
                date_received.add("no review");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_reviews_table();
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        }
        
        output.add(row_id);
        output.add(use_item_id);
        output.add(rating);
        output.add(subject);
        output.add(description);
        output.add(name);
        output.add(date_received);
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_sale_item_reviews_lowest_rating_first() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> row_id = new ArrayList<>();
        ArrayList<String> use_item_id = new ArrayList<>();
        ArrayList<String> rating = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> date_received = new ArrayList<>();
        
        int sale_item_review_count = 0;
        int offset;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            offset = (Integer.parseInt(get_page_number()) - 1) * Integer.parseInt(get_results_per_page());
            
            select_statement = connection.prepareStatement("SELECT row_id, item_id, rating, subject, description, name, " +
                    "date_received FROM company_item_reviews WHERE item_id = ? AND visible = ? " +
                    "ORDER BY rating ASC LIMIT " + get_results_per_page() + " OFFSET " + offset);
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            select_statement.setString(2, "yes");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                row_id.add(String.valueOf(select_results.getInt(1)));
                use_item_id.add(String.valueOf(select_results.getInt(2)));
                rating.add(String.valueOf(select_results.getInt(3)));
                subject.add(select_results.getString(4));
                description.add(select_results.getString(5));
                name.add(select_results.getString(6));
                date_received.add(select_results.getString(7));
                
                sale_item_review_count++;
            }
            
            if (sale_item_review_count == 0) {
                
                row_id.add("0");
                use_item_id.add("0");
                rating.add("0");
                subject.add("no review");
                description.add("no review");
                name.add("no review");
                date_received.add("no review");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_reviews_table();
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            row_id.add("0");
            use_item_id.add("0");
            rating.add("0");
            subject.add("fail");
            description.add("fail");
            name.add("fail");
            date_received.add("fail");
        }
        
        output.add(row_id);
        output.add(use_item_id);
        output.add(rating);
        output.add(subject);
        output.add(description);
        output.add(name);
        output.add(date_received);
        
        return output;
    }
    
    protected static int search_for_sale_item_reviews_count() {
        
        int output;
        int sale_item_review_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id FROM company_item_reviews " +
                    "WHERE item_id = ? AND visible = ? ORDER BY row_id ASC");
            
            select_statement.setInt(1, Integer.parseInt(get_item_id()));
            select_statement.setString(2, "yes");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                sale_item_review_count++;
            }
            
            output = sale_item_review_count;
        } catch (SQLException e) {
            
            output = 0;
        } catch (Exception e) {
            
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