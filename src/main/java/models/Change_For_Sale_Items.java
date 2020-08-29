//Author: Timothy van der Graaff
package models;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utilities.Find_and_replace;

public abstract class Change_For_Sale_Items {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String[] external_id;
    private static String[] category;
    private static String row_id;
    private static String item_id;
    private static String item;
    private static String thumbnail;
    private static String description;
    private static String price;
    private static String inventory;
    private static String date_received;
    private static String time_received;
    
    //mutators
    protected static void set_external_id(String[] this_external_id) {
        
        external_id = this_external_id;
    }
    
    protected static void set_category(String[] this_category) {
        
        category = this_category;
    }
    
    protected static void set_row_id(String this_row_id) {
        
        row_id = this_row_id;
    }
    
    protected static void set_item_id(String this_item_id) {
        
        item_id = this_item_id;
    }
    
    protected static void set_item(String this_item) {
        
        item = this_item;
    }
    
    protected static void set_thumbnail(String this_thumbnail) {
        
        thumbnail = this_thumbnail;
    }
    
    protected static void set_description(String this_description) {
        
        description = this_description;
    }
    
    protected static void set_price(String this_price) {
        
        price = this_price;
    }
    
    protected static void set_inventory(String this_inventory) {
        
        inventory = this_inventory;
    }
    
    protected static void set_date_received(String this_date_received) {
        
        date_received = this_date_received;
    }
    
    protected static void set_time_received(String this_time_received) {
        
        time_received = this_time_received;
    }
    
    //accessors
    private static String[] get_external_id() {
        
        return external_id;
    }
    
    private static String[] get_category() {
        
        return category;
    }
    
    private static String get_row_id() {
        
        return row_id;
    }
    
    private static String get_item_id() {
        
        return item_id;
    }
    
    private static String get_item() {
        
        return item;
    }
    
    private static String get_thumbnail() {
        
        return thumbnail;
    }
    
    private static String get_description() {
        
        return description;
    }
    
    private static String get_price() {
        
        return price;
    }
    
    private static String get_inventory() {
        
        return inventory;
    }
    
    private static String get_date_received() {
        
        return date_received;
    }
    
    private static String get_time_received() {
        
        return time_received;
    }
    
    
    
    
    private static void create_new_table() {
        
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
    
    private static int generate_row_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_items_for_sale ORDER BY row_id DESC LIMIT 1");
            
            ResultSet select_results = prepared_statement.executeQuery();
            
            select_results.last();
            
            if (select_results.getRow() > 0) {
               
                output = select_results.getInt(1);
            } else {
                
                output = 0;
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "{0}", e);
            
            output = 0;
        }
        
        return output + 1;
    }
    
    private static int generate_sale_categories_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_sale_categories ORDER BY row_id DESC LIMIT 1");
            
            ResultSet select_results = prepared_statement.executeQuery();
            
            select_results.last();
            
            if (select_results.getRow() > 0) {
               
                output = select_results.getInt(1);
            } else {
                
                output = 0;
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "{0}", e);
            
            output = 0;
        }
        
        return output + 1;
    }
    
    private static int generate_sale_item_pictures_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_item_pictures ORDER BY row_id DESC LIMIT 1");
            
            ResultSet select_results = prepared_statement.executeQuery();
            
            select_results.last();
            
            if (select_results.getRow() > 0) {
               
                output = select_results.getInt(1);
            } else {
                
                output = 0;
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "{0}", e);
            
            output = 0;
        }
        
        return output + 1;
    }
    
    protected static String add_item_for_sale() {
        
        String output;
        String category_concatination = "";
        
        for (int i = 0; i < get_category().length; i++) {
            
            if (i != (get_category().length - 1)) {
                
                category_concatination += get_category()[i] + ",";
            } else {
                
                category_concatination += get_category()[i];
            }
        }
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_items_for_sale (row_id, item, thumbnail, category, description, " +
                    "price, inventory, date_received, time_received) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            insert_statement.setInt(1, generate_row_id());
            insert_statement.setString(2, get_item());
            insert_statement.setString(3, get_thumbnail());
            insert_statement.setString(4, category_concatination);
            insert_statement.setString(5, get_description());
            insert_statement.setDouble(6, Double.valueOf(get_price()));
            insert_statement.setInt(7, Integer.valueOf(get_inventory()));
            insert_statement.setString(8, get_date_received());
            insert_statement.setString(9, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static ArrayList<String> search_sale_categories() {
        
        ArrayList<String> output = new ArrayList<>();
        
        int user_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT external_id FROM company_sale_categories " +
                    "ORDER BY row_id DESC");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
               
                output.add(select_results.getString(1));
                
                user_count++;
            }
            
            if (user_count == 0) {
                
                output.add("no categories");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_sale_categories' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_categories_table();
            
            output.add("fail");
        }
        
        return output;
    }
    
    protected static void add_sale_categories() {
        
        ArrayList<String> search_sale_categories = search_sale_categories();
        int records_to_insert = 0;
        int use_row_id;
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("&#44;");
        replace.add(",");
        
        PreparedStatement insert_statement;
        
        try {
            
            use_row_id = generate_sale_categories_id();
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_sale_categories (row_id, category, external_id, " +
                    "date_received, time_received) VALUES(?, ?, ?, ?, ?)");
            
            for (int i = 0; i < get_external_id().length; i++) {
                
                if (!(search_sale_categories.contains(get_external_id()[i]))) {
                
                    insert_statement.setInt(1, use_row_id);
                    insert_statement.setString(2, Find_and_replace.find_and_replace(find, replace, get_category()[i]));
                    insert_statement.setString(3, get_external_id()[i]);
                    insert_statement.setString(4, get_date_received());
                    insert_statement.setString(5, get_time_received());
                    
                    insert_statement.addBatch();
                    
                    records_to_insert++;
                    use_row_id++;
                }
            }
            
            if (records_to_insert > 0) {
                
                insert_statement.executeBatch();
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_sale_categories' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_categories_table();
        }
    }
    
    protected static String add_sale_item_pictures() {
        
        String output;
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_item_pictures (row_id, item_id, thumbnail, date_received, time_received) " +
                    "VALUES(?, ?, ?, ?, ?)");
            
            insert_statement.setInt(1, generate_sale_item_pictures_id());
            insert_statement.setInt(2, Integer.valueOf(get_item_id()));
            insert_statement.setString(3, get_thumbnail());
            insert_statement.setString(4, get_date_received());
            insert_statement.setString(5, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_pictures' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_pictures_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String change_item_for_sale() {
        
        String output;
        String category_concatination = "";
        
        for (int i = 0; i < get_category().length; i++) {
            
            if (i != (get_category().length - 1)) {
                
                category_concatination += get_category()[i] + ",";
            } else {
                
                category_concatination += get_category()[i];
            }
        }
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_items_for_sale SET item = ?, " +
                    "thumbnail = ?, category = ?, description = ?, price = ?, inventory = ? " +
                    "WHERE row_id = ?");
            
            update_statement.setString(1, get_item());
            update_statement.setString(2, get_thumbnail());
            update_statement.setString(3, category_concatination);
            update_statement.setString(4, get_description());
            update_statement.setDouble(5, Double.parseDouble(get_price()));
            update_statement.setInt(6, Integer.valueOf(get_inventory()));
            update_statement.setInt(7, Integer.valueOf(get_row_id()));
            
            update_statement.addBatch();
            
            update_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static void change_sale_categories() {
        
        int records_to_update = 0;
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("&#44;");
        replace.add(",");
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_sale_categories SET category = ? " +
                    "WHERE external_id = ?");
            
            for (int i = 0; i < get_external_id().length; i++) {
                
                update_statement.setString(1, Find_and_replace.find_and_replace(find, replace, get_category()[i]));
                update_statement.setString(2, get_external_id()[i]);
                    
                update_statement.addBatch();
                    
                records_to_update++;
            }
            
            if (records_to_update > 0) {
                
                update_statement.executeBatch();
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_sale_categories' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_categories_table();
        }
    }
    
    protected static String delete_item_for_sale() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_items_for_sale WHERE row_id = ?");
            
            delete_statement.setInt(1, Integer.parseInt(get_row_id()));
            
            delete_statement.addBatch();
            
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String delete_sale_item_pictures() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_item_pictures WHERE row_id = ?");
            
            delete_statement.setInt(1, Integer.parseInt(get_row_id()));
            
            delete_statement.addBatch();
            
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_pictures' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_pictures_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String delete_sale_item_pictures_by_item() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_item_pictures WHERE item_id = ?");
            
            delete_statement.setInt(1, Integer.parseInt(get_item_id()));
            
            delete_statement.addBatch();
            
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_pictures' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_item_pictures_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static void delete_sale_categories() {
        
        int records_to_delete = 0;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_sale_categories WHERE external_id = ?");
            
            for (int i = 0; i < get_external_id().length; i++) {
                
                delete_statement.setString(1, get_external_id()[i]);
                    
                delete_statement.addBatch();
                    
                records_to_delete++;
            }
            
            if (records_to_delete > 0) {
                
                delete_statement.executeBatch();
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_sale_categories' " +
                    "table is corrupt or does not exist");
            
            create_new_sale_categories_table();
        }
    }
}
