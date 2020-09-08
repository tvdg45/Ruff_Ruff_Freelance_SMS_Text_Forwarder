//Author: Timothy van der Graaff
package models;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Change_Shopping_Cart_Items {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String[] row_id;
    private static String item;
    private static String thumbnail;
    private static String category;
    private static String description;
    private static String price;
    private static String[] quantity;
    private static String raw_time_received;
    private static String guest_session;
    private static String[] item_id;
    private static double total_price;
    private static String date_received;
    private static String time_received;
    protected static ArrayList<Integer> inventory_values;
    private static ArrayList<String> guest_sessions;
    private static int generate_receipt_id;
    
    //mutators
    protected static void set_row_id(String[] this_row_id) {
        
        row_id = this_row_id;
    }
    
    protected static void set_item(String this_item) {
        
        item = this_item;
    }
    
    protected static void set_thumbnail(String this_thumbnail) {
        
        thumbnail = this_thumbnail;
    }
    
    protected static void set_category(String this_category) {
        
        category = this_category;
    }
    
    protected static void set_description(String this_description) {
        
        description = this_description;
    }
    
    protected static void set_price(String this_price) {
        
        price = this_price;
    }
    
    protected static void set_quantity(String[] this_quantity) {
        
        quantity = this_quantity;
    }
    
    protected static void set_raw_time_received(String this_raw_time_received) {
        
        raw_time_received = this_raw_time_received;
    }
    
    protected static void set_guest_session(String this_guest_session) {
        
        guest_session = this_guest_session;
    }
    
    protected static void set_guest_sessions(ArrayList<String> this_guest_sessions) {
        
        guest_sessions = this_guest_sessions;
    }
    
    protected static void set_item_id(String[] this_item_id) {
        
        item_id = this_item_id;
    }
    
    protected static void set_total_price(double this_total_price) {
        
        total_price = this_total_price;
    }
    
    protected static void set_date_received(String this_date_received) {
        
        date_received = this_date_received;
    }
    
    protected static void set_time_received(String this_time_received) {
        
        time_received = this_time_received;
    }
    
    //accessors
    private static String[] get_row_id() {
        
        return row_id;
    }
    
    private static String get_item() {
        
        return item;
    }
    
    private static String get_thumbnail() {
        
        return thumbnail;
    }
    
    private static String get_category() {
        
        return category;
    }
    
    private static String get_description() {
        
        return description;
    }
    
    private static String get_price() {
        
        return price;
    }
    
    private static String[] get_quantity() {
        
        return quantity;
    }
    
    private static String get_raw_time_received() {
        
        return raw_time_received;
    }
    
    private static String get_guest_session() {
        
        return guest_session;
    }
    
    private static ArrayList<String> get_guest_sessions() {
        
        return guest_sessions;
    }
    
    private static String[] get_item_id() {
        
        return item_id;
    }
    
    private static double get_total_price() {
        
        return total_price;
    }
    
    private static String get_date_received() {
        
        return date_received;
    }
    
    private static String get_time_received() {
        
        return time_received;
    }
    
    
    
    
    
    
    protected static int generate_row_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_shopping_cart_items ORDER BY row_id DESC LIMIT 1");
            
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
    
    protected static int generate_receipt_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_receipts ORDER BY row_id DESC LIMIT 1");
            
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
    
    protected static int generate_guest_session_id() {
        
        int output;
        
        try {
            
            PreparedStatement prepared_statement = connection.prepareStatement("SELECT row_id FROM " +
                    "company_guest_sessions ORDER BY row_id DESC LIMIT 1");
            
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
    
    private static void create_new_table() {
        
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
    
    private static void create_new_receipt_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_receipts " +
                    "(row_id INT NOT NULL, total_price DECIMAL(10,2) NOT NULL, " +
                    "date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_receipts' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
    
     private static void create_new_items_sold_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_items_sold " +
                    "(row_id INT NOT NULL, item TEXT NOT NULL, " +
                    "thumbnail TEXT NOT NULL, category TEXT NOT NULL, " +
                    "description TEXT NOT NULL, price DECIMAL(10,2) NOT NULL, " +
                    "quantity INT NOT NULL, receipt_id INT NOT NULL, " +
                    "date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_sold' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
    }
     
    private static void create_new_guest_sessions_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement(
                    
                    "CREATE TABLE company_guest_sessions " +
                    "(row_id INT NOT NULL, session TEXT NOT NULL, " +
                    "raw_time_received TEXT NOT NULL, " +
                    "date_received TEXT NOT NULL, time_received TEXT NOT NULL, " +
                    "PRIMARY KEY (row_id)) ENGINE = MYISAM;");
            
            create_statement.execute();
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_sold' " +
                    "table was not created because it already exists.  " +
                    "This is not necessarily an error.");
        }
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
    
    //Guests may not buy more items than what is already in stock for each given item.
    protected static ArrayList<Integer> search_shopping_cart_quantity_per_item() {
        
        ArrayList<Integer> output = new ArrayList<>();
        ArrayList<Integer> all_items_by_quantity = new ArrayList<>();
        ArrayList<Integer> all_items_by_item_id = new ArrayList<>();
        int total_quantity_per_item;
        String query_string = "";
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            query_string += "SELECT quantity, item_id FROM company_shopping_cart_items ";
            query_string += "WHERE ";
            query_string += "session = ? AND (";
            
            for (int i = 2; i < (get_item_id().length + 2); i++) {
                
                if (i == 2) {
                    
                    query_string += "item_id = ?";
                } else {
                    
                    query_string += " OR item_id = ?";
                }
            }
            

            query_string += ") ORDER BY item_id ASC";
            
            select_statement = connection.prepareStatement(query_string);
            
            select_statement.setString(1, get_guest_session());
            
            for (int i = 2; i < (get_item_id().length + 2); i++) {
                
                select_statement.setInt(i, Integer.valueOf(get_item_id()[i - 2]));
            }
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                all_items_by_quantity.add(select_results.getInt(1));
                all_items_by_item_id.add(select_results.getInt(2));
            }
            
            for (int i = 0; i < get_item_id().length; i++) {
                
                total_quantity_per_item = 0;
                
                for (int j = 0; j < all_items_by_item_id.size(); j++) {
                    
                    if (Integer.valueOf(get_item_id()[i]).equals(all_items_by_item_id.get(j))) {
                        
                        total_quantity_per_item += all_items_by_quantity.get(j);
                    }
                }
                
                output.add(total_quantity_per_item);
            }
        } catch (SQLException e) {
            
            output.add(0);
        } catch (Exception e) {
            
            output.add(0);
        }
        
        return output;
    }
    
    //Guests may not buy more items than what is already in stock for each given item.
    protected static ArrayList<Integer> search_for_sales_items_inventory() {
        
        ArrayList<Integer> output = new ArrayList<>();
        int items_in_stock_count = 0;
        String query_string = "";
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            query_string += "SELECT inventory FROM company_items_for_sale ";
            query_string += "WHERE ";
            
            for (int i = 1; i < (get_row_id().length + 1); i++) {
                
                if (i == 1) {
                    
                    query_string += "row_id = ? ";
                } else {
                   
                    query_string += "OR row_id = ? ";
                }
            }
            
            query_string += "ORDER BY row_id ASC";
            
            select_statement = connection.prepareStatement(query_string);
            
            for (int i = 1; i < (get_item_id().length + 1); i++) {
                
                select_statement.setInt(i, Integer.valueOf(get_row_id()[i - 1]));
            }
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output.add(select_results.getInt(1));
                
                items_in_stock_count++;
            }
            
            if (items_in_stock_count == 0) {
                
                output.add(0);
                
                LOGGER.log(Level.INFO, "none");
            }
        } catch (SQLException e) {
            
            output.add(0);
        } catch (Exception e) {
            
            output.add(0);
        }
        
        return output;
    }
    
    protected static String add_to_cart() {
        
        String output;
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_shopping_cart_items (row_id, item, thumbnail, category, description, " +
                    "price, quantity, raw_time_received, session, item_id, date_received, time_received) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            insert_statement.setInt(1, generate_row_id());
            insert_statement.setString(2, get_item());
            insert_statement.setString(3, get_thumbnail());
            insert_statement.setString(4, get_category());
            insert_statement.setString(5, get_description());
            insert_statement.setDouble(6, Double.valueOf(get_price()));
            insert_statement.setInt(7, Integer.valueOf(get_quantity()[0]));
            insert_statement.setString(8, get_raw_time_received());
            insert_statement.setString(9, get_guest_session());
            insert_statement.setInt(10, Integer.valueOf(get_item_id()[0]));
            insert_statement.setString(11, get_date_received());
            insert_statement.setString(12, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String change_cart_quantity() {
        
        String output;
        int records_to_change = 0;
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_shopping_cart_items " +
                    "SET quantity = ? WHERE row_id = ?");
            
            for (int i = 0; i < get_row_id().length; i++) {
                
                update_statement.setInt(1, Integer.parseInt(get_quantity()[i]));
                update_statement.setInt(2, Integer.parseInt(get_row_id()[i]));
                
                update_statement.addBatch();
                
                records_to_change++;
            }
            
            if (records_to_change > 0) {
                
                update_statement.executeBatch();
            }
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String delete_from_cart() {
        
        String output;
        int records_to_delete = 0;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_shopping_cart_items WHERE row_id = ?");
            
            for (int i = 0; i < get_row_id().length; i++) {
                
                delete_statement.setInt(1, Integer.parseInt(get_row_id()[i]));
                
                delete_statement.addBatch();
                
                records_to_delete++;
            }
            
            if (records_to_delete > 0) {
                
                delete_statement.executeBatch();
            }
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String change_item_inventories() {
        
        String output;
        int records_to_change = 0;
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_items_for_sale " +
                    "SET inventory = ? WHERE row_id = ?");
            
            for (int i = 0; i < inventory_values.size(); i++) {
                
                update_statement.setInt(1, inventory_values.get(i));
                update_statement.setInt(2, Integer.parseInt(get_row_id()[i]));
                
                update_statement.addBatch();
                
                records_to_change++;
            }
            
            if (records_to_change > 0) {
                
                update_statement.executeBatch();
            }
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_for_sale' " +
                    "table is corrupt or does not exist");
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
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
    
    protected static String create_receipt() {
        
        String output;
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_receipts (row_id, total_price, date_received, time_received) " +
                    "VALUES(?, ?, ?, ?)");
            
            generate_receipt_id = generate_receipt_id();
            
            insert_statement.setInt(1, generate_receipt_id);
            insert_statement.setDouble(2, get_total_price());
            insert_statement.setString(3, get_date_received());
            insert_statement.setString(4, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_receipts' " +
                    "table is corrupt or does not exist");
            
            create_new_receipt_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String add_to_receipt(ArrayList<String> use_row_id, ArrayList<String> use_item,
            ArrayList<String> use_thumbnail, ArrayList<String> item_category,
            ArrayList<String> use_description, ArrayList<String> use_price,
            ArrayList<String> use_quantity) {
        
        String output;
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_items_sold (row_id, item, thumbnail, category, description, price, " +
                    "quantity, receipt_id, date_received, time_received) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            for (int i = 0; i < use_row_id.size(); i++) {
                
                insert_statement.setInt(1, Integer.valueOf(String.valueOf(generate_receipt_id) + use_row_id.get(i)));
                insert_statement.setString(2, use_item.get(i));
                insert_statement.setString(3, use_thumbnail.get(i));
                insert_statement.setString(4, item_category.get(i));
                insert_statement.setString(5, use_description.get(i));
                insert_statement.setDouble(6, Double.parseDouble(use_price.get(i)));
                insert_statement.setInt(7, Integer.valueOf(use_quantity.get(i)));
                insert_statement.setInt(8, generate_receipt_id);
                insert_statement.setString(9, get_date_received());
                insert_statement.setString(10, get_time_received());
                
                insert_statement.addBatch();
            }
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_sold' " +
                    "table is corrupt or does not exist");
            
            create_new_items_sold_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;       
    }
    
    //This method searches all items for the customer's receipt.
    protected static ArrayList<ArrayList<String>> search_shopping_cart_items() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> use_row_id = new ArrayList<>();
        ArrayList<String> use_item = new ArrayList<>();
        ArrayList<String> use_thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> use_description = new ArrayList<>();
        ArrayList<String> use_price = new ArrayList<>();
        ArrayList<String> use_quantity = new ArrayList<>();
        
        int shopping_cart_item_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id, item, thumbnail, category, " +
                    "description, price, quantity FROM company_shopping_cart_items WHERE session = ? " +
                    "ORDER BY row_id DESC");
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                use_row_id.add(String.valueOf(select_results.getInt(1)));
                use_item.add(select_results.getString(2));
                use_thumbnail.add(select_results.getString(3));
                item_category.add(select_results.getString(4));
                use_description.add(select_results.getString(5));
                use_price.add(String.format("%.2f", select_results.getDouble(6)));
                use_quantity.add(String.valueOf(select_results.getInt(7)));
                
                shopping_cart_item_count++;
            }
            
            if (shopping_cart_item_count == 0) {
                
                use_row_id.add("0");
                use_item.add("no item");
                use_thumbnail.add("no item");
                item_category.add("no item");
                use_description.add("no item");
                use_price.add("no item");
                use_quantity.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            use_row_id.add("0");
            use_item.add("fail");
            use_thumbnail.add("fail");
            item_category.add("fail");
            use_description.add("fail");
            use_price.add("fail");
            use_quantity.add("0");
        }
        
        output.add(use_row_id);
        output.add(use_item);
        output.add(use_thumbnail);
        output.add(item_category);
        output.add(use_description);
        output.add(use_price);
        output.add(use_quantity);
        
        return output;
    }
    
    //Remove items from shopping cart that have been in there for a day.
    protected static String clear_carts() {
        
        String output;
        String query_string = "";
        
        PreparedStatement delete_statement;
        
        try {
            
            //Delete shopping cart items without a browsing session.
            query_string += "DELETE FROM company_shopping_cart_items";
            
            for (int i = 1; i < (get_guest_sessions().size() + 1); i++) {
                
                if (i == 1) {
                    
                    query_string += " WHERE NOT session = ?";
                } else {
                    
                    query_string += " OR NOT session = ?";
                }
            }
            
            delete_statement = connection.prepareStatement(query_string);
            
            for (int i = 1; i < (get_guest_sessions().size() + 1); i++) {
                
                delete_statement.setString(i, get_guest_sessions().get(i - 1));
            }
            
            if (get_guest_sessions().size() > 0) {
                
                delete_statement.addBatch();
                delete_statement.executeBatch();
            }
            
            query_string = "";
            delete_statement.clearBatch();
            
            //Delete shopping cart items with a browsing session.
            query_string += "DELETE FROM company_shopping_cart_items";
            
            for (int i = 1; i < (get_guest_sessions().size() + 1); i++) {
                
                if (i == 1) {
                    
                    query_string += " WHERE session = ?";
                } else {
                    
                    query_string += " OR session = ?";
                }
            }
            
            delete_statement = connection.prepareStatement(query_string);
            
            for (int i = 1; i < (get_guest_sessions().size() + 1); i++) {
                
                delete_statement.setString(i, get_guest_sessions().get(i - 1));
            }
            
            if (get_guest_sessions().size() > 0) {
                
                delete_statement.addBatch();
                delete_statement.executeBatch();
            }
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String search_guest_session() {
        
        String output = "";
        
        int guest_session_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT session " +
                    "FROM company_guest_sessions WHERE session = ? " +
                    "ORDER BY row_id DESC");
            
            select_statement.setString(1, get_guest_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output = select_results.getString(1);
                
                guest_session_count++;
            }
            
            if (guest_session_count != 1) {
                
                output = "";
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_guest_sessions' " +
                    "table is corrupt or does not exist");
            
            create_new_guest_sessions_table();
            
            output = "";
        }
        
        return output;
    }
    
    protected static String add_guest_session() {
        
        String output;
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_guest_sessions (row_id, session, raw_time_received, date_received, time_received) " +
                    "VALUES(?, ?, ?, ?, ?)");
            
            insert_statement.setInt(1, generate_guest_session_id());
            insert_statement.setString(2, get_guest_session());
            insert_statement.setString(3, get_raw_time_received());
            insert_statement.setString(4, get_date_received());
            insert_statement.setString(5, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_guest_sessions' " +
                    "table is corrupt or does not exist");
            
            create_new_guest_sessions_table();
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_guest_sessions() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> use_guest_session = new ArrayList<>();
        ArrayList<String> use_raw_time_received = new ArrayList<>();
        
        int guest_session_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT session, raw_time_received " +
                    "FROM company_guest_sessions " +
                    "ORDER BY row_id DESC");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                use_guest_session.add(select_results.getString(1));
                use_raw_time_received.add(select_results.getString(2));
                
                guest_session_count++;
            }
            
            if (guest_session_count == 0) {
                
                use_guest_session.add("");
                use_raw_time_received.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_guest_sessions' " +
                    "table is corrupt or does not exist");
            
            create_new_guest_sessions_table();
            
            use_guest_session.add("");
            use_raw_time_received.add("0");
        }
        
        output.add(use_guest_session);
        output.add(use_raw_time_received);
        
        return output;        
    }
    
    //Remove guest sessions that have been active for a day.
    protected static String clear_guest_sessions() {
        
        String output;
        String query_string = "";
        
        PreparedStatement delete_statement;
        
        try {
            
            query_string += "DELETE FROM company_guest_sessions WHERE";
            
            for (int i = 1; i < (get_guest_sessions().size() + 1); i++) {
                
                if (i == 1) {
                    
                    query_string += " session = ?";
                } else {
                    
                    query_string += " OR session = ?";
                }
            }
            
            delete_statement = connection.prepareStatement(query_string);
            
            for (int i = 1; i < (get_guest_sessions().size() + 1); i++) {
                
                delete_statement.setString(i, get_guest_sessions().get(i - 1));
            }
            
            if (get_guest_sessions().size() > 0) {
                
                delete_statement.addBatch();
                delete_statement.executeBatch();
            }
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_shopping_cart_items' " +
                    "table is corrupt or does not exist");
            
            create_new_guest_sessions_table();
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String clear_cart() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_shopping_cart_items WHERE session = ?");
            
            delete_statement.setString(1, get_guest_session());
            
            delete_statement.addBatch();
            
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            create_new_table();
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static ArrayList<String> search_receipts() {
        
        ArrayList<String> output = new ArrayList<>();
        
        int receipt_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT row_id FROM company_receipts " +
                    "ORDER BY row_id DESC");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                output.add(String.valueOf(select_results.getInt(1)));
                
                receipt_count++;
            }
            
            if (receipt_count == 0) {
                
                output.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_receipts' " +
                    "table is corrupt or does not exist");
            
            create_new_receipt_table();
            
            output.add("0");
        }
        
        return output;
    }
    
    protected static ArrayList<ArrayList<String>> search_items_sold() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> use_item = new ArrayList<>();
        ArrayList<String> use_thumbnail = new ArrayList<>();
        ArrayList<String> use_category = new ArrayList<>();
        ArrayList<String> use_description = new ArrayList<>();
        ArrayList<String> use_price = new ArrayList<>();
        ArrayList<String> use_receipt_id = new ArrayList<>();
        
        int items_sold_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT item, thumbnail, category, " +
                    "description, price, quantity, receipt_id FROM company_items_sold " +
                    "ORDER BY row_id DESC");
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                use_item.add(select_results.getString(1));
                use_thumbnail.add(select_results.getString(2));
                use_category.add(select_results.getString(3));
                use_description.add(select_results.getString(4));
                use_price.add(String.valueOf(select_results.getDouble(5) * select_results.getInt(6)));
                use_receipt_id.add(String.valueOf(select_results.getInt(7)));
                
                items_sold_count++;
            }
            
            if (items_sold_count == 0) {
                
                use_item.add("no item");
                use_thumbnail.add("no item");
                use_category.add("no item");
                use_description.add("no item");
                use_price.add("0");
                use_receipt_id.add("0");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_sold' " +
                    "table is corrupt or does not exist");
            
            create_new_items_sold_table();
            
            use_item.add("fail");
            use_thumbnail.add("fail");
            use_category.add("fail");
            use_description.add("fail");
            use_price.add("0");
            use_receipt_id.add("0");
        }
        
        output.add(use_item);
        output.add(use_thumbnail);
        output.add(use_category);
        output.add(use_description);
        output.add(use_price);
        output.add(use_receipt_id);
        
        return output;
    }
    
    protected static String delete_receipts() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_receipts");
            
            delete_statement.addBatch();
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_receipts' " +
                    "table is corrupt or does not exist");
            
            create_new_receipt_table();
            
            output = "fail";
        }        
        
        return output;
    }
    
    protected static String delete_items_sold() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_items_sold");
            
            delete_statement.addBatch();
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_items_sold' " +
                    "table is corrupt or does not exist");
            
            create_new_items_sold_table();
            
            output = "fail";
        }        
        
        return output;
    }
}
