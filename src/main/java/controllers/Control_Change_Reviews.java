//Author: Timothy van der Graaff
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
import views.Show_Reviews_Update;

public class Control_Change_Reviews extends models.Change_Reviews {
    
    //global variables
    public static Connection use_connection;
    public static String row_id;
    public static String item_id;
    public static String name;
    public static String email;
    public static String rating;
    public static String subject;
    public static String description;
    public static String new_security_code;
    public static String security_code;
    public static String date_received;
    public static String time_received;
    public static String create_review;
    public static String change_review;
    public static String delete_review;
    public static String send_review_change_email_request;
    public static String send_review_deletion_email_request;
    public static String make_review_public;
    public static String enable_review_editing;
    public static String enable_review_deletion;
    
    public static String control_create_review() throws IOException {
        
        String output = "";
        String file_stream = "";
        
        if (create_review.equals("Submit")) {
            
            if (Form_Validation.is_string_null_or_white_space(name)
                    || (Form_Validation.is_string_null_or_white_space(email)
                    || !(Form_Validation.is_email_valid(email)))
                    || Form_Validation.is_string_null_or_white_space(rating)
                    || (!(subject.equals("")) && subject.replaceAll(" ", "").length() == 0)
                    || (!(description.equals("")) && description.replaceAll(" ", "").length() == 0)) {
                
                Show_Reviews_Update.form_messages.add("error creating review");
                    
                if (Form_Validation.is_string_null_or_white_space(name)) {
                    
                    Show_Reviews_Update.form_messages.add("Name is required.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (Form_Validation.is_string_null_or_white_space(email) || !(Form_Validation.is_email_valid(email))) {
                    
                    Show_Reviews_Update.form_messages.add("Email is required.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (Form_Validation.is_string_null_or_white_space(rating)) {
                    
                    Show_Reviews_Update.form_messages.add("Rating is required.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (!(subject.equals("")) && subject.replaceAll(" ", "").length() == 0) {
                    
                    Show_Reviews_Update.form_messages.add("Subject requires at least one non-space character.  " +
                            "Otherwise, leave this field blank.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (!(description.equals("")) && description.replaceAll(" ", "").length() == 0) {
                    
                    Show_Reviews_Update.form_messages.add("Description requires at least one non-space character.  " +
                            "Otherwise, leave this field blank.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                output = Show_Reviews_Update.show_form_messages();
                
                Show_Reviews_Update.form_messages.clear();
            } else {
                
                connection = use_connection;
                
                if (subject.equals("")) {
                    
                    subject = "No subject";
                }
                
                if (description.equals("")) {
                    
                    description = "No comment";
                }
                
                set_item_id(item_id);
                set_name(name);
                set_email(email);
                set_rating(rating);
                set_subject(subject);
                set_description(description);
                set_security_code(new_security_code);
                set_date_received(date_received);
                set_time_received(time_received);
                
                if (add_review().equals("success")) {
                    
                    try {
                        
                        URL url_for_post_request = new URL(Config.domain() + "/third-party/email-forwarders/send-email-create-review-request/");
                        
                        Map<String, Object> parameter = new LinkedHashMap<>();
                        
                        parameter.put("domain", Config.domain());
                        parameter.put("row_id", (generate_row_id() - 1));
                        parameter.put("item_id", item_id);
                        parameter.put("rating", rating);
                        parameter.put("subject", subject);
                        parameter.put("description", description);
                        parameter.put("name", name);
                        parameter.put("email", email);
                        parameter.put("security_code", new_security_code);
                        
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
                        
                        HttpURLConnection file_connection = (HttpURLConnection)url_for_post_request.openConnection();
                        file_connection.setRequestMethod("POST");
                        file_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        file_connection.setRequestProperty("Content-Length", String.valueOf(post_data_bytes.length));
                        file_connection.setDoOutput(true);
                        file_connection.getOutputStream().write(post_data_bytes);
                        
                        Reader read_input = new BufferedReader(
                                new InputStreamReader(file_connection.getInputStream(), "UTF-8"));
                        
                        for (int each_character; (each_character = read_input.read()) >= 0;) {
                            
                            file_stream += (char)each_character;
                        }
                        
                        if (file_stream.equals("success")) {
                            
                            Show_Reviews_Update.form_messages.add("success creating review");
                        }
                    } catch (Exception e) {
                        
                        Show_Reviews_Update.form_messages.add("fail");
                    }
                }
                
                output = Show_Reviews_Update.show_form_messages();
                
                try {
                    
                    use_connection.close();
                } catch (SQLException e) {
                }
                
                Show_Reviews_Update.form_messages.clear();
            }
        }
        
        return output;
    }
    
    //When changing a review, the email cannot be changed because a hacker might otherwise
    //steal control of the review.
    public static String control_change_review() {
        
        String output = "";
        
        if (change_review.equals("Submit")) {
            
            if (Form_Validation.is_string_null_or_white_space(name)
                    || Form_Validation.is_string_null_or_white_space(rating)
                    || (!(subject.equals("")) && subject.replaceAll(" ", "").length() == 0)
                    || (!(description.equals("")) && description.replaceAll(" ", "").length() == 0)) {
                
                Show_Reviews_Update.form_messages.add("error changing review");
                    
                if (Form_Validation.is_string_null_or_white_space(name)) {
                    
                    Show_Reviews_Update.form_messages.add("Name is required.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (Form_Validation.is_string_null_or_white_space(rating)) {
                    
                    Show_Reviews_Update.form_messages.add("Rating is required.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (!(subject.equals("")) && subject.replaceAll(" ", "").length() == 0) {
                    
                    Show_Reviews_Update.form_messages.add("Subject requires at least one non-space character.  " +
                            "Otherwise, leave this field blank.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                if (!(description.equals("")) && description.replaceAll(" ", "").length() == 0) {
                    
                    Show_Reviews_Update.form_messages.add("Description requires at least one non-space character.  " +
                            "Otherwise, leave this field blank.");
                } else {
                    
                    Show_Reviews_Update.form_messages.add("");
                }
                
                output = Show_Reviews_Update.show_form_messages();
                
                Show_Reviews_Update.form_messages.clear();
            } else {
                
                connection = use_connection;
                
                set_row_id(row_id);
                set_item_id(item_id);
                set_name(name);
                set_rating(rating);
                set_subject(subject);
                set_description(description);
                
                if (change_review().equals("success")) {
                    
                    set_security_code(new_security_code);
                    
                    //The security code must be changed to prevent hackers from rechanging the review.
                    if (change_security_code().equals("success")) {
                        
                        Show_Reviews_Update.form_messages.add("success changing review");
                    }
                }
                
                output = Show_Reviews_Update.show_form_messages();
                
                try {
                    
                    use_connection.close();
                } catch (SQLException e) {
                }
                
                Show_Reviews_Update.form_messages.clear();
            }
        }
        
        return output;
    }
    
    public static String control_delete_review() {
        
        String output = "";
        
        if (delete_review.equals("Delete")) {
            
            connection = use_connection;
            
            set_row_id(row_id);
            set_item_id(item_id);
            
            if (delete_review().equals("success")) {
                
                Show_Reviews_Update.form_messages.add("success deleting review");
            }
            
            output = Show_Reviews_Update.show_form_messages();
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
            
            Show_Reviews_Update.form_messages.clear();
        }
        
        return output;
    }
    
    //To starting changing a review, an email has to be sent to the authorized review owner.
    public static String control_send_review_change_email_request() throws IOException {
        
        String output = "";
        String file_stream = "";
        
        if (send_review_change_email_request.equals("Edit")) {
            
            connection = use_connection;
            
            set_security_code(new_security_code);
            set_row_id(row_id);
            set_item_id(item_id);
            
            if (change_security_code().equals("success")) {

                if (!(search_particular_review().get(0).get(0).equals("fail"))
                        && !(search_particular_review().get(0).get(0).equals("no review"))
                        && search_particular_review().get(0).size() == 1) {
                    
                    URL url_for_post_request = new URL(Config.domain() + "/third-party/email-forwarders/send-email-change-review-request/");
                    
                    Map<String, Object> parameter = new LinkedHashMap<>();
                    
                    parameter.put("domain", Config.domain());
                    parameter.put("row_id", search_particular_review().get(0).get(0));
                    parameter.put("item_id", search_particular_review().get(1).get(0));
                    parameter.put("rating", search_particular_review().get(2).get(0));
                    
                    if (search_particular_review().get(3).get(0).equals("")
                            || search_particular_review().get(3).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("subject", "No subject");
                    } else {
                        
                        parameter.put("subject", search_particular_review().get(3).get(0));
                    }
                    
                    if (search_particular_review().get(4).get(0).equals("")
                            || search_particular_review().get(4).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("description", "No comment");
                    } else {
                        
                        parameter.put("description", search_particular_review().get(4).get(0));
                    }
                    
                    if (search_particular_review().get(5).get(0).equals("")
                            || search_particular_review().get(5).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("name", "Anonymous");
                    } else {
                        
                        parameter.put("name", search_particular_review().get(5).get(0));
                    }
                    
                    if (search_particular_review().get(6).get(0).equals("")
                            || search_particular_review().get(6).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("email", "anonymous@emailaddress.com");
                    } else {
                        
                        parameter.put("email", search_particular_review().get(6).get(0));
                    }
                    
                    parameter.put("security_code", search_particular_review().get(7).get(0));
                    
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
                    
                    HttpURLConnection file_connection = (HttpURLConnection)url_for_post_request.openConnection();
                    file_connection.setRequestMethod("POST");
                    file_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    file_connection.setRequestProperty("Content-Length", String.valueOf(post_data_bytes.length));
                    file_connection.setDoOutput(true);
                    file_connection.getOutputStream().write(post_data_bytes);
                    
                    Reader read_input = new BufferedReader(
                            new InputStreamReader(file_connection.getInputStream(), "UTF-8"));
                    
                    for (int each_character; (each_character = read_input.read()) >= 0;) {
                        
                        file_stream += (char)each_character;
                    }
                    
                    if (file_stream.equals("success")) {
                        
                        Show_Reviews_Update.form_messages.add("success - send email change review request");
                    }                    
                }
            }
            
            output = Show_Reviews_Update.show_form_messages();
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
            
            Show_Reviews_Update.form_messages.clear();
        }
        
        return output;
    }
    
    //To starting deleting a review, an email has to be sent to the authorized review owner.
    public static String control_send_review_deletion_email_request() throws IOException {
        
        String output = "";
        String file_stream = "";
        
        if (send_review_deletion_email_request.equals("Delete")) {
            
            connection = use_connection;
            
            set_security_code(new_security_code);
            set_row_id(row_id);
            set_item_id(item_id);
            
            if (change_security_code().equals("success")) {

                if (!(search_particular_review().get(0).get(0).equals("fail"))
                        && !(search_particular_review().get(0).get(0).equals("no review"))
                        && search_particular_review().get(0).size() == 1) {
                    
                    URL url_for_post_request = new URL(Config.domain() + "/third-party/email-forwarders/send-email-delete-review-request/");
                    
                    Map<String, Object> parameter = new LinkedHashMap<>();
                    
                    parameter.put("domain", Config.domain());
                    parameter.put("row_id", search_particular_review().get(0).get(0));
                    parameter.put("item_id", search_particular_review().get(1).get(0));
                    parameter.put("rating", search_particular_review().get(2).get(0));
                    
                    if (search_particular_review().get(3).get(0).equals("")
                            || search_particular_review().get(3).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("subject", "No subject");
                    } else {
                        
                        parameter.put("subject", search_particular_review().get(3).get(0));
                    }
                    
                    if (search_particular_review().get(4).get(0).equals("")
                            || search_particular_review().get(4).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("description", "No comment");
                    } else {
                        
                        parameter.put("description", search_particular_review().get(4).get(0));
                    }
                    
                    if (search_particular_review().get(5).get(0).equals("")
                            || search_particular_review().get(5).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("name", "Anonymous");
                    } else {
                        
                        parameter.put("name", search_particular_review().get(5).get(0));
                    }
                    
                    if (search_particular_review().get(6).get(0).equals("")
                            || search_particular_review().get(6).get(0).replaceAll(" ", "").length() == 0) {
                        
                        parameter.put("email", "anonymous@emailaddress.com");
                    } else {
                        
                        parameter.put("email", search_particular_review().get(6).get(0));
                    }
                    
                    parameter.put("security_code", search_particular_review().get(7).get(0));
                    
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
                    
                    HttpURLConnection file_connection = (HttpURLConnection)url_for_post_request.openConnection();
                    file_connection.setRequestMethod("POST");
                    file_connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    file_connection.setRequestProperty("Content-Length", String.valueOf(post_data_bytes.length));
                    file_connection.setDoOutput(true);
                    file_connection.getOutputStream().write(post_data_bytes);
                    
                    Reader read_input = new BufferedReader(
                            new InputStreamReader(file_connection.getInputStream(), "UTF-8"));
                    
                    for (int each_character; (each_character = read_input.read()) >= 0;) {
                        
                        file_stream += (char)each_character;
                    }
                    
                    if (file_stream.equals("success")) {
                        
                        Show_Reviews_Update.form_messages.add("success - send email delete review request");
                    }                    
                }
            }
            
            output = Show_Reviews_Update.show_form_messages();
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
            
            Show_Reviews_Update.form_messages.clear();
        }
        
        return output;
    }
    
    //Allow a review to be visible in a thread.
    public static String control_make_review_public() {
        
        String output = "";
        boolean review_made_public = false;
        boolean security_code_changed = false;
        
        if (make_review_public.equals("Show")) {
            
            connection = use_connection;
            
            set_security_code(security_code);
            set_row_id(row_id);
            set_item_id(item_id);
            
            if (make_review_public().equals("success")) {
                
                review_made_public = true;
            }
            
            set_security_code(new_security_code);
            
            if (change_security_code().equals("success")) {
                
                security_code_changed = true;
            }
            
            if (review_made_public && security_code_changed) {
                
                Show_Reviews_Update.form_messages.add("success making review public");
            } else {
                
                Show_Reviews_Update.form_messages.add("error making review public");
            }
            
            output = Show_Reviews_Update.show_form_messages();
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
            
            Show_Reviews_Update.form_messages.clear();
        }
        
        return output;
    }
    
    public static String control_enable_review_editing() {
        
        String output = "";
        boolean review_found = false;
        boolean security_code_changed = false;
        ArrayList<String> unlock_review = new ArrayList<>();
        
        if (enable_review_editing.equals("Enable")) {
            
            connection = use_connection;
            
            set_security_code(security_code);
            set_row_id(row_id);
            set_item_id(item_id);
            
            if (!(unlock_review().get(0).get(0).equals("fail"))
                    && !(unlock_review().get(0).get(0).equals("no review"))
                    && unlock_review().get(0).size() == 1) {
                
                unlock_review.add(unlock_review().get(0).get(0));
                unlock_review.add(unlock_review().get(1).get(0));
                unlock_review.add(unlock_review().get(2).get(0));
                unlock_review.add(unlock_review().get(3).get(0));
                
                review_found = true;
            }
            
            set_security_code(new_security_code);
            
            if (change_security_code().equals("success")) {
                
                security_code_changed = true;
            }
            
            if (review_found && security_code_changed) {
                
                Show_Reviews_Update.form_messages.add("success enabling review editing");
                Show_Reviews_Update.form_messages.add(unlock_review.get(0));
                Show_Reviews_Update.form_messages.add(unlock_review.get(1));
                Show_Reviews_Update.form_messages.add(unlock_review.get(2));
                Show_Reviews_Update.form_messages.add(unlock_review.get(3));
            } else {
                
                Show_Reviews_Update.form_messages.add("error enabling review editing");
            }
            
            output = Show_Reviews_Update.show_form_messages();
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
            
            Show_Reviews_Update.form_messages.clear();
        }
        
        return output;
    }
    
    public static String control_enable_review_deletion() {
        
        String output = "";
        boolean review_found = false;
        boolean security_code_changed = false;
        ArrayList<String> unlock_review = new ArrayList<>();
        
        if (enable_review_deletion.equals("Enable")) {
            
            connection = use_connection;
            
            set_security_code(security_code);
            set_row_id(row_id);
            set_item_id(item_id);
            
            if (!(unlock_review().get(0).get(0).equals("fail"))
                    && !(unlock_review().get(0).get(0).equals("no review"))
                    && unlock_review().get(0).size() == 1) {
                
                unlock_review.add(unlock_review().get(0).get(0));
                unlock_review.add(unlock_review().get(1).get(0));
                unlock_review.add(unlock_review().get(2).get(0));
                unlock_review.add(unlock_review().get(3).get(0));
                
                review_found = true;
            }
            
            set_security_code(new_security_code);
            
            if (change_security_code().equals("success")) {
                
                security_code_changed = true;
            }
            
            if (review_found && security_code_changed) {
                
                Show_Reviews_Update.form_messages.add("success enabling review deletion");
                Show_Reviews_Update.form_messages.add(unlock_review.get(0));
                Show_Reviews_Update.form_messages.add(unlock_review.get(1));
                Show_Reviews_Update.form_messages.add(unlock_review.get(2));
                Show_Reviews_Update.form_messages.add(unlock_review.get(3));
            } else {
                
                Show_Reviews_Update.form_messages.add("error enabling review deletion");
            }
            
            output = Show_Reviews_Update.show_form_messages();
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
            
            Show_Reviews_Update.form_messages.clear();
        }
        
        return output;
    }
}