//Author: Timothy van der Graaff
package models;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Change_Reviews {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String row_id;
    private static String item_id;
    private static String name;
    private static String email;
    private static String rating;
    private static String subject;
    private static String description;
    private static String security_code;
    private static String date_received;
    private static String time_received;
    
    //mutators
    protected static void set_row_id(String this_row_id) {
       
        row_id = this_row_id;
    }
    
    protected static void set_item_id(String this_item_id) {
        
        item_id = this_item_id;
    }
    
    protected static void set_name(String this_name) {
        
        name = this_name;
    }
    
    protected static void set_email(String this_email) {
        
        email = this_email;
    }
    
    protected static void set_rating(String this_rating) {
        
        rating = this_rating;
    }
    
    protected static void set_subject(String this_subject) {
        
        subject = this_subject;
    }
    
    protected static void set_description(String this_description) {
        
        description = this_description;
    }
    
    protected static void set_security_code(String this_security_code) {
        
        security_code = this_security_code;
    }
    
    protected static void set_date_received(String this_date_received) {
        
        date_received = this_date_received;
    }
    
    protected static void set_time_received(String this_time_received) {
        
        time_received = this_time_received;
    }
    
    //accessors
    private static String get_row_id() {
        
        return row_id;
    }
    
    private static String get_item_id() {
        
        return item_id;
    }
    
    private static String get_name() {
        
        return name;
    }
    
    private static String get_email() {
        
        return email;
    }
    
    private static String get_rating() {
        
        return rating;
    }
    
    private static String get_subject() {
        
        return subject;
    }
    
    private static String get_description() {
        
        return description;
    }
    
    private static String get_security_code() {
        
        return security_code;
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
                    "company_item_reviews ORDER BY row_id DESC LIMIT 1");
            
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
    
    //This method digs up important data, so that an email can be sent to the
    //review owner.
    protected static ArrayList<ArrayList<String>> search_particular_review() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> each_row_id = new ArrayList<>();
        ArrayList<String> each_item_id = new ArrayList<>();
        ArrayList<String> each_rating = new ArrayList<>();
        ArrayList<String> each_subject = new ArrayList<>();
        ArrayList<String> each_description = new ArrayList<>();
        ArrayList<String> each_name = new ArrayList<>();
        ArrayList<String> each_email = new ArrayList<>();
        ArrayList<String> each_security_code = new ArrayList<>();
        
        int review_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {

            select_statement = connection.prepareStatement("SELECT row_id, item_id, rating, subject, description, " +
                    "name, email, security_code FROM company_item_reviews WHERE row_id = ? AND item_id = ? " +
                    "ORDER BY row_id ASC");
            
            select_statement.setInt(1, Integer.parseInt(get_row_id()));
            select_statement.setInt(2, Integer.parseInt(get_item_id()));
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                each_row_id.add(String.valueOf(select_results.getInt(1)));
                each_item_id.add(String.valueOf(select_results.getInt(2)));
                each_rating.add(String.valueOf(select_results.getInt(3)));
                each_subject.add(select_results.getString(4));
                each_description.add(select_results.getString(5));
                each_name.add(select_results.getString(6));
                each_email.add(select_results.getString(7));
                each_security_code.add(select_results.getString(8));
                
                review_count++;
            }
            
            if (review_count == 0) {
                
                each_row_id.add("no review");
                each_item_id.add("no review");
                each_rating.add("no review");
                each_subject.add("no review");
                each_description.add("no review");
                each_name.add("no review");
                each_email.add("no review");
                each_security_code.add("no review");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            each_row_id.add("fail");
            each_item_id.add("fail");
            each_rating.add("fail");
            each_subject.add("fail");
            each_description.add("fail");
            each_name.add("fail");
            each_email.add("fail");
            each_security_code.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            each_row_id.add("fail");
            each_item_id.add("fail");
            each_rating.add("fail");
            each_subject.add("fail");
            each_description.add("fail");
            each_name.add("fail");
            each_email.add("fail");
            each_security_code.add("fail");
        }
        
        output.add(each_row_id);
        output.add(each_item_id);
        output.add(each_rating);
        output.add(each_subject);
        output.add(each_description);
        output.add(each_name);
        output.add(each_email);
        output.add(each_security_code);
        
        return output;
    }
    
    //This method requires a security code, so that a review can safely be changed or deleted.
    protected static ArrayList<ArrayList<String>> unlock_review() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> each_rating = new ArrayList<>();
        ArrayList<String> each_subject = new ArrayList<>();
        ArrayList<String> each_description = new ArrayList<>();
        ArrayList<String> each_name = new ArrayList<>();
        
        int review_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {

            select_statement = connection.prepareStatement("SELECT rating, subject, description, " +
                    "name FROM company_item_reviews WHERE row_id = ? AND item_id = ? " +
                    "AND security_code = ? ORDER BY row_id ASC");
            
            select_statement.setInt(1, Integer.parseInt(get_row_id()));
            select_statement.setInt(2, Integer.parseInt(get_item_id()));
            select_statement.setString(3, get_security_code());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                each_rating.add(String.valueOf(select_results.getInt(1)));
                each_subject.add(select_results.getString(2));
                each_description.add(select_results.getString(3));
                each_name.add(select_results.getString(4));
                
                review_count++;
            }
            
            if (review_count == 0) {
                
                each_rating.add("no review");
                each_subject.add("no review");
                each_description.add("no review");
                each_name.add("no review");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            each_rating.add("fail");
            each_subject.add("fail");
            each_description.add("fail");
            each_name.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            each_rating.add("fail");
            each_subject.add("fail");
            each_description.add("fail");
            each_name.add("fail");
        }
        
        output.add(each_rating);
        output.add(each_subject);
        output.add(each_description);
        output.add(each_name);
        
        return output;
    }
    
    //Allow a review to be visible in a thread.
    protected static String make_review_public() {
        
        String output;
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_item_reviews " +
                    "SET visible = ? WHERE row_id = ? AND item_id = ? AND security_code = ?");
            
            update_statement.setString(1, "yes");
            update_statement.setInt(2, Integer.parseInt(get_row_id()));
            update_statement.setInt(3, Integer.parseInt(get_item_id()));
            update_statement.setString(4, get_security_code());
            
            update_statement.addBatch();
            
            update_statement.executeBatch();
            
            if (update_statement.getUpdateCount() > 0) {
                
                output = "success";
            } else {
                
                output = "fail";
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    //Changing security code is necessary when trying to edit or
    //delete a review.
    protected static String change_security_code() {
        
        String output;
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_item_reviews " +
                    "SET security_code = ? WHERE row_id = ? AND item_id = ?");
            
            update_statement.setString(1, get_security_code());
            update_statement.setInt(2, Integer.parseInt(get_row_id()));
            update_statement.setInt(3, Integer.parseInt(get_item_id()));
            
            update_statement.addBatch();
            
            update_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String add_review() {
        
        String output;
        
        PreparedStatement insert_statement;
        
        try {
            
            insert_statement = connection.prepareStatement("INSERT INTO " +
                    "company_item_reviews (row_id, item_id, rating, subject, description, " +
                    "name, email, security_code, visible, date_received, time_received) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            insert_statement.setInt(1, generate_row_id());
            insert_statement.setInt(2, Integer.parseInt(get_item_id()));
            insert_statement.setInt(3, Integer.parseInt(get_rating()));
            insert_statement.setString(4, get_subject());
            insert_statement.setString(5, get_description());
            insert_statement.setString(6, get_name());
            insert_statement.setString(7, get_email());
            insert_statement.setString(8, get_security_code());
            insert_statement.setString(9, "no");
            insert_statement.setString(10, get_date_received());
            insert_statement.setString(11, get_time_received());
            
            insert_statement.addBatch();
            
            insert_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    //When changing a review, the email cannot be changed because a hacker might otherwise
    //steal control of the review.
    protected static String change_review() {
        
        String output;
        
        PreparedStatement update_statement;
        
        try {
            
            update_statement = connection.prepareStatement("UPDATE company_item_reviews " +
                    "SET rating = ?, subject = ?, description = ?, name = ? " +
                    "WHERE row_id = ? AND item_id = ?");
            
            update_statement.setInt(1, Integer.parseInt(get_rating()));
            update_statement.setString(2, get_subject());
            update_statement.setString(3, get_description());
            update_statement.setString(4, get_name());
            update_statement.setInt(5, Integer.parseInt(get_row_id()));
            update_statement.setInt(6, Integer.parseInt(get_item_id()));
            
            update_statement.addBatch();
            
            update_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
    
    protected static String delete_review() {
        
        String output;
        
        PreparedStatement delete_statement;
        
        try {
            
            delete_statement = connection.prepareStatement("DELETE FROM company_item_reviews WHERE row_id = ? " +
                    "AND item_id = ?");
            
            delete_statement.setInt(1, Integer.parseInt(get_row_id()));
            delete_statement.setInt(2, Integer.parseInt(get_item_id()));
            
            delete_statement.addBatch();
            
            delete_statement.executeBatch();
            
            output = "success";
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'company_item_reviews' " +
                    "table is corrupt or does not exist");
            
            create_new_table();
            
            output = "fail";
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            output = "fail";
        }
        
        return output;
    }
}