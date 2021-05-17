//Author: Timothy van der Graaff
package models;

import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Search_Email_Address {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static Connection connection;
    
    //global variables
    private static String product;
    private static String user_id;
    private static String session;
    
    //mutators
    protected static void set_product(String this_product) {
        
        product = this_product;
    }
    
    protected static void set_user_id(String this_user_id) {
        
        user_id = this_user_id;
    }
    
    protected static void set_session(String this_session) {
        
        session = this_session;
    }
    
    //accessors
    private static String get_product() {
        
        return product;
    }
    
    private static String get_user_id() {
       
        return user_id;
    }
    
    private static String get_session() {
       
        return session;
    }
    
    
    
    
    
    private static void create_new_general_users_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement("CREATE TABLE `wpct_users` " +
                    "(`ID` bigint(20) UNSIGNED NOT NULL, `user_login` varchar(60) " + 
                    "COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `user_pass` varchar(255) " +
                    "COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `user_nicename` varchar(250) " +
                    "COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `user_email` varchar(100) " +
                    "COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `user_url` varchar(100) " +
                    "COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `user_registered` datetime NOT NULL " +
                    "DEFAULT current_timestamp(), `user_activation_key` varchar(255) COLLATE " +
                    "utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `user_status` int(11) NOT NULL " +
                    "DEFAULT 0, `display_name` varchar(250) COLLATE utf8mb4_unicode_520_ci NOT NULL " +
                    "DEFAULT '') ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;");
            
            create_statement.execute();
        } catch(SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_users " +
                    "table was not created beceuse it alreada exists.  " +
                    "This is not necessarily an error.");
        }
    }
        
    private static void create_new_general_user_meta_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement("CREATE TABLE `wpct_usermeta` " +
                    "(`umeta_id` bigint(20) UNSIGNED NOT NULL, `user_id` bigint(20) UNSIGNED NOT NULL " +
                    "DEFAULT 0, `meta_key` varchar(255) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL, " +
                    "`meta_value` longtext COLLATE utf8mb4_unicode_520_ci DEFAULT NULL) ENGINE=InnoDB " + 
                    "DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;");
            
            create_statement.execute();
        } catch(SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_usermeta " +
                    "table was not created beceuse it alreada exists.  " +
                    "This is not necessarily an error.");
        }        
    }
    
    private static void create_new_posts_table() {
        
        try {
            
            PreparedStatement create_statement = connection.prepareStatement("CREATE TABLE `wpct_posts` " +
                    "(`ID` bigint(20) UNSIGNED NOT NULL, `post_author` bigint(20) UNSIGNED NOT NULL " +
                    "DEFAULT 0, `post_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00', `post_date_gmt` " +
                    "datetime NOT NULL DEFAULT '0000-00-00 00:00:00', `post_content` longtext COLLATE " +
                    "utf8mb4_unicode_520_ci NOT NULL, `post_title` text COLLATE utf8mb4_unicode_520_ci NOT " +
                    "NULL, `post_excerpt` text COLLATE utf8mb4_unicode_520_ci NOT NULL, `post_status` " +
                    "varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT 'publish', " +
                    "`comment_status` varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT " +
                    "'open', `ping_status` varchar(20) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT " +
                    "'open', `post_password` varchar(255) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT " +
                    "'', `post_name` varchar(200) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', " +
                    "`to_ping` text COLLATE utf8mb4_unicode_520_ci NOT NULL, `pinged` text COLLATE " +
                    "utf8mb4_unicode_520_ci NOT NULL, `post_modified` datetime NOT NULL " +
                    "DEFAULT '0000-00-00 00:00:00', `post_modified_gmt` datetime NOT NULL " +
                    "DEFAULT '0000-00-00 00:00:00', `post_content_filtered` longtext COLLATE " +
                    "utf8mb4_unicode_520_ci NOT NULL, `post_parent` bigint(20) UNSIGNED NOT NULL " +
                    "DEFAULT 0, `guid` varchar(255) COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', " +
                    "`menu_order` int(11) NOT NULL DEFAULT 0, `post_type` varchar(20) COLLATE " +
                    "utf8mb4_unicode_520_ci NOT NULL DEFAULT 'post', `post_mime_type` varchar(100) " +
                    "COLLATE utf8mb4_unicode_520_ci NOT NULL DEFAULT '', `comment_count` bigint(20) " +
                    "NOT NULL DEFAULT 0) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;");
            
            create_statement.execute();
        } catch(SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_posts " +
                    "table was not created beceuse it alreada exists.  " +
                    "This is not necessarily an error.");
        }        
    }
    
    //A customer wants to inquire about the vendor's product.
    //The vendor identification and product details need to be searched.
    protected static ArrayList<ArrayList<String>> search_product() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> post_author = new ArrayList<>();
        ArrayList<String> post_title = new ArrayList<>();
        ArrayList<String> post_content = new ArrayList<>();
        ArrayList<String> post_url = new ArrayList<>();
        
        int post_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT post_author, post_title, " +
                    "post_content, guild FROM wpct_posts WHERE post_type = ? AND " +
                    "post_name = ? ORDER BY ID DESC LIMIT 1");
            
            select_statement.setString(1, "product");
            select_statement.setString(2, get_product());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                post_author.add(String.valueOf(select_results.getInt(1)));
                post_title.add(select_results.getString(2));
                post_content.add(select_results.getString(3));
                post_url.add(select_results.getString(4));
                
                post_count++;
            }
            
            if (post_count == 0) {
                
                post_author.add("no product");
                post_title.add("no product");
                post_content.add("no product");
                post_url.add("no content");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_posts' " +
                    "table is corrupt or does not exist");
            
            create_new_posts_table();
            
            post_author.add("fail");
            post_title.add("fail");
            post_content.add("fail");
            post_url.add("fail");
        }
        
        output.add(post_author);
        output.add(post_title);
        output.add(post_content);
        output.add(post_url);
        
        return output;
    }
    
    //Search the vendor's email address and display name.
    protected static ArrayList<ArrayList<String>> search_vendor_email() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> vendor_id = new ArrayList<>();
        ArrayList<String> vendor_email = new ArrayList<>();
        ArrayList<String> vendor_display_name = new ArrayList<>();
        
        int vendor_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT ID, user_email, display_name FROM wpct_users " +
                    "WHERE ID = ? ORDER BY ID DESC LIMIT 1");
            
            select_statement.setInt(1, Integer.valueOf(get_user_id()));
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                vendor_id.add(String.valueOf(select_results.getInt(1)));
                vendor_email.add(select_results.getString(2));
                vendor_display_name.add(select_results.getString(3));
                
                vendor_count++;
            }
            
            if (vendor_count == 0) {
                
                vendor_id.add("no vendor");
                vendor_email.add("no vendor");
                vendor_display_name.add("no vendor");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_users' " +
                    "table is corrupt or does not exist");
            
            create_new_general_users_table();
            
            vendor_id.add("fail");
            vendor_email.add("fail");
            vendor_display_name.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            vendor_id.add("fail");
            vendor_email.add("fail");
            vendor_display_name.add("fail");
        }
        
        output.add(vendor_id);
        output.add(vendor_email);
        output.add(vendor_display_name);
        
        return output;        
    }
    
    protected static ArrayList<ArrayList<String>> search_customer_email() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> customer_id = new ArrayList<>();
        ArrayList<String> customer_email = new ArrayList<>();
        ArrayList<String> customer_display_name = new ArrayList<>();
        
        int customer_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT ID, user_email, display_name FROM " +
                    "wpct_users WHERE user_nicename = ? ORDER BY ID DESC LIMIT 1");
            
            select_statement.setString(1, get_session());
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                customer_id.add(String.valueOf(select_results.getInt(1)));
                customer_email.add(select_results.getString(2));
                customer_display_name.add(select_results.getString(3));
                
                customer_count++;
            }
            
            if (customer_count == 0) {
                
                customer_id.add("no customer");
                customer_email.add("no customer");
                customer_display_name.add("no customer");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_users' " +
                    "table is corrupt or does not exist");
            
            create_new_general_users_table();
            
            customer_id.add("fail");
            customer_email.add("fail");
            customer_display_name.add("fail");
        }
        
        output.add(customer_id);
        output.add(customer_email);
        output.add(customer_display_name);
        
        return output;        
    }
    
    protected static ArrayList<ArrayList<String>> search_user_meta() {
        
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        
        ArrayList<String> meta_key = new ArrayList<>();
        ArrayList<String> meta_value = new ArrayList<>();
        
        int meta_count = 0;
        
        PreparedStatement select_statement;
        ResultSet select_results;
        
        try {
            
            select_statement = connection.prepareStatement("SELECT meta_key, meta_value FROM " +
                    "wpct_usermeta WHERE (meta_key = ? OR meta_key = ? OR meta_key = ?) AND " +
                    "user_id = ? ORDER BY ID DESC");
            
            select_statement.setString(1, "first_name");
            select_statement.setString(2, "last_name");
            select_statement.setString(3, "description");
            select_statement.setInt(1, Integer.valueOf(get_user_id()));
            
            select_results = select_statement.executeQuery();
            
            while (select_results.next()) {
                
                meta_key.add(select_results.getString(1));
                meta_value.add(select_results.getString(2));
                
                meta_count++;
            }
            
            if (meta_count == 0) {
                
                meta_key.add("no user meta");
                meta_value.add("no user meta");
            }
        } catch (SQLException e) {
            
            LOGGER.log(Level.INFO, "The 'wpct_usermeta' " +
                    "table is corrupt or does not exist");
            
            create_new_general_user_meta_table();
            
            meta_key.add("fail");
            meta_value.add("fail");
        } catch (Exception e) {
            
            LOGGER.log(Level.INFO, e.getMessage());
            
            meta_key.add("fail");
            meta_value.add("fail");
        }
        
        output.add(meta_key);
        output.add(meta_value);
        
        return output;         
    }
}
