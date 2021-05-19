//Author Timothy van der Graaff
package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import configuration.Config;
import utilities.Form_Validation;

public class Control_Search_Email_Address extends models.Search_Email_Address {

    //global variables
    public static String first_name;
    public static String last_name;
    public static String email;
    public static String biography;
    public static String product;
    public static String session;
    public static Connection use_connection;
    public static String register_as_customer;
    public static String register_as_vendor;
    public static String log_in;
    public static ArrayList<ArrayList<String>> search_product;
    public static ArrayList<ArrayList<String>> search_vendor_email;
    public static ArrayList<ArrayList<String>> search_vendor_meta;
    public static ArrayList<ArrayList<String>> search_customer_email;
    public static ArrayList<ArrayList<String>> search_customer_meta;
    
    public static String control_search_email_via_login() throws IOException {
        
        String output;
        String file_stream = "";
        
        /*try {
            
            URL url_for_get_request = new URL(Config.domain() +
                                            "wp-content/themes/astra/apps/session-checker.php");
            
            Map<String, Object> get_parameter = new LinkedHashMap<>();
                                    StringBuilder get_data = new StringBuilder();
                                    
            for (Map.Entry<String, Object> each_parameter : get_parameter.entrySet()) {
                
                if (get_data.length() != 0) {
                    
                    get_data.append('&');
                }
                
                get_data.append(URLEncoder.encode(each_parameter.getKey(), "UTF-8"));
                get_data.append('=');
                get_data.append(URLEncoder.encode(String.valueOf(each_parameter.getValue()), "UTF-8"));
            }
            
            byte[] get_data_bytes = get_data.toString().getBytes("UTF-8");
            
            HttpURLConnection session_file_connection = (HttpURLConnection)url_for_get_request.openConnection();
            session_file_connection.setRequestMethod("GET");
            session_file_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            session_file_connection.setRequestProperty("Content-Length", String.valueOf(get_data_bytes.length));
            session_file_connection.setDoOutput(true);
            session_file_connection.getOutputStream().write(get_data_bytes);
            
            Reader get_input = new BufferedReader(
                    new InputStreamReader(session_file_connection.getInputStream(), "UTF-8"));
            
            for (int each_character; (each_character = get_input.read()) >= 0;) {
                
                file_stream += (char)each_character;
            }
            
            session = file_stream;
        } catch (Exception e) {
            
            session = "";
        }
        
        file_stream = "";*/
		
		session = "tvdg45";
        
        if (log_in.equals("Log in") && !(Form_Validation.is_string_null_or_white_space(session))) {
            
            connection = use_connection;
            
            set_product(product);
			
			output = " ";
            
            //search_product = search_product();
			
			//output = search_product.get(0).get(0);
            
            /*if (!(search_product.get(0).get(0).equals("no product")) && !(search_product.get(0).get(0).equals("fail"))) {
            
                set_user_id(search_product.get(0).get(0));
                
                search_vendor_email = search_vendor_email();
                
                if (!(search_vendor_email.get(0).get(0).equals("no vendor")) && !(search_vendor_email.get(0).get(0).equals("fail"))) {
                    
                    search_vendor_meta = search_user_meta();
                    
                    if (!(search_vendor_meta.get(0).get(0).equals("no user meta")) && !(search_vendor_meta.get(0).get(0).equals("fail"))) {
                        
                        set_session(session);
                        
                        search_customer_email = search_customer_email();
                        
                        if (!(search_customer_email.get(0).get(0).equals("no customer")) && !(search_customer_email.get(0).get(0).equals("fail"))) {
                            
                            set_user_id(search_customer_email.get(0).get(0));
                            
                            search_customer_meta = search_user_meta();
                            
                            //Customer's can only inquire on other vendors' products, not their own.
                            if (!(search_customer_meta.get(0).get(0).equals("no user meta"))
                                    && !(search_customer_meta.get(0).get(0).equals("fail"))
                                    && !(search_customer_meta.get(0).get(0).equals(search_customer_email.get(0).get(0)))) {
                                
                                try {
                                    
                                    URL url_for_post_request = new URL(Config.domain() +
                                            "wp-content/themes/astra/apps/email-forwarder.php");
                                    
                                    Map<String, Object> parameter = new LinkedHashMap<>();
                                    
                                    parameter.put("post_title", search_product.get(1).get(0));
                                    parameter.put("post_content", search_product.get(2).get(0));
                                    parameter.put("post_link", search_product.get(3).get(0));
                                    parameter.put("vendor_email", search_vendor_email.get(1).get(0));
                                    parameter.put("vendor_display_name", search_vendor_email.get(2).get(0));
                                    parameter.put("customer_email", search_customer_email.get(1).get(0));
                                    parameter.put("customer_display_name", search_customer_email.get(2).get(0));
                                    
                                    for (int i = 0; i < search_vendor_meta.get(0).size(); i++) {
                                        
                                        if (search_vendor_meta.get(0).get(i).equals("first_name")) {
                                            
                                            parameter.put("vendor_first_name", search_vendor_meta.get(1).get(i));
                                        }
                                        
                                        if (search_vendor_meta.get(0).get(i).equals("last_name")) {
                                            
                                            parameter.put("vendor_last_name", search_vendor_meta.get(1).get(i));
                                        }
                                    }
                                    
                                    for (int i = 0; i < search_customer_meta.get(0).size(); i++) {
                                        
                                        if (search_customer_meta.get(0).get(i).equals("first_name")) {
                                            
                                            parameter.put("customer_first_name", search_customer_meta.get(1).get(i));
                                        }
                                        
                                        if (search_customer_meta.get(0).get(i).equals("last_name")) {
                                            
                                            parameter.put("customer_last_name", search_customer_meta.get(1).get(i));
                                        }
                                        
                                        if (search_customer_meta.get(0).get(i).equals("description")) {
                                            
                                            parameter.put("customer_biography", search_customer_meta.get(1).get(i));
                                        }
                                    }
                                    
                                    StringBuilder post_data = new StringBuilder();
                                    
                                    for (Map.Entry<String, Object> each_parameter : parameter.entrySet()) {
                                        
                                        if (post_data.length() != 0) {
                                            
                                            post_data.append('&');
                                        }
                                        
                                        post_data.append(URLEncoder.encode(each_parameter.getKey(), "UTF-8"));
                                        post_data.append('=');
                                        post_data.append(URLEncoder.encode(String.valueOf(each_parameter.getValue()), "UTF-8"));
                                    }
                                    
                                    byte[] post_data_bytes = post_data.toString().getBytes("UTF-8");
                                    
                                    HttpURLConnection email_file_connection = (HttpURLConnection)url_for_post_request.openConnection();
                                    email_file_connection.setRequestMethod("POST");
                                    email_file_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                    email_file_connection.setRequestProperty("Content-Length", String.valueOf(post_data_bytes.length));
                                    email_file_connection.setDoOutput(true);
                                    email_file_connection.getOutputStream().write(post_data_bytes);
                                    
                                    Reader read_input = new BufferedReader(
                                            new InputStreamReader(email_file_connection.getInputStream(), "UTF-8"));
                                    
                                    for (int each_character; (each_character = read_input.read()) >= 0;) {
                                        
                                        file_stream += (char)each_character;
                                    }
                                    
                                    if (file_stream.equals("email sent")) {
                                        
                                        output = "email sent";
                                    } else {
                                        
                                        output = "email not sent";
                                    }
                                } catch (Exception e) {
                                    
                                    output = "email not sent";
                                }
                            } else {
                                
                                output = "email not sent";
                            }
                        } else {
                            
                            output = "email not sent";
                        }
                    } else {
                        
                        output = "email not sent";
                    }
                } else {
                    
                    output = "email not sent";
                }
            } else {
                
                output = "email not sent";
            }*/
			
            /*try {
                
                use_connection.close();
            } catch (SQLException e) {
            }*/
        } else {
            
            output = "email not sent";
        }
        
        return output;
    }
}
