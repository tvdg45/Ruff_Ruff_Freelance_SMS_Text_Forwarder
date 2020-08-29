//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Form_Validation;
import utilities.Find_and_replace;

public class Show_For_Sale_Item_Details {
    
    //global variables
    public static ArrayList<ArrayList<String>> for_sale_item_details = new ArrayList<>();
    public static ArrayList<ArrayList<String>> for_sale_item_additional_pictures = new ArrayList<>();
    public static ArrayList<ArrayList<String>> for_sale_item_reviews = new ArrayList<>();
    public static int page_number_count;
    
    public static String show_for_sale_item_details() {
        
        String output = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("<script");
        find.add("<style");
        find.add("\"");
        find.add("'");
        find.add("<br />");
        find.add("<br>");
        find.add("<div>");
        find.add("</div>");
        
        replace.add("&lt;script");
        replace.add("&lt;style");
        replace.add("&quot;");
        replace.add("&apos;");
        replace.add(" ");
        replace.add("");
        replace.add("");
        replace.add("");
        
        output += "[";
        
        for (int i = 0; i < for_sale_item_details.get(0).size(); i++) {
            
            output += "{\"row_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(0).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(1).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"thumbnail\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(2).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item_category\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(3).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"description\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(4).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"price\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(5).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"inventory\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_details.get(6).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        output = output.replace(", {}", "");
        
        return output;
    }
    
    public static String show_for_sale_item_additional_pictures() {
        
        String output = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("<script");
        find.add("<style");
        find.add("\"");
        find.add("'");
        find.add("<br />");
        find.add("<br>");
        find.add("<div>");
        find.add("</div>");
        
        replace.add("&lt;script");
        replace.add("&lt;style");
        replace.add("&quot;");
        replace.add("&apos;");
        replace.add(" ");
        replace.add("");
        replace.add("");
        replace.add("");
        
        output += "[";
        
        for (int i = 0; i < for_sale_item_additional_pictures.get(0).size(); i++) {
            
            output += "{\"row_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_additional_pictures.get(0).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"thumbnail\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_additional_pictures.get(1).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        output = output.replace(", {}", "");
        
        return output;
    }
    
    public static String show_for_sale_item_reviews() {
        
        String output = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("<script");
        find.add("<style");
        find.add("\"");
        find.add("'");
        find.add("<br />");
        find.add("<br>");
        find.add("<div>");
        find.add("</div>");
        
        replace.add("&lt;script");
        replace.add("&lt;style");
        replace.add("&quot;");
        replace.add("&apos;");
        replace.add(" ");
        replace.add("");
        replace.add("");
        replace.add("");
        
        output += "[";
        
        for (int i = 0; i < for_sale_item_reviews.get(0).size(); i++) {
            
            output += "{\"row_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(0).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(1).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"rating\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(2).get(i)).replace("<", "&lt;").replace(">", "&gt;"));
            
            if (Form_Validation.is_string_null_or_white_space(for_sale_item_reviews.get(3).get(i))) {
                
                output += "\", \"subject\": \"No subject";
            } else {
                
                output += "\", \"subject\": \"" +
                        Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(3).get(i)).replace("<", "&lt;").replace(">", "&gt;"));
            }
            
            if (Form_Validation.is_string_null_or_white_space(for_sale_item_reviews.get(4).get(i))) {
                
                output += "\", \"description\": \"No comment";
            } else {
                
                output += "\", \"description\": \"" +
                        Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(4).get(i)).replace("<", "&lt;").replace(">", "&gt;"));
            }
            
            if (Form_Validation.is_string_null_or_white_space(for_sale_item_reviews.get(5).get(i))) {
                
                output += "\", \"name\": \"Anonymous";
            } else {
                
                output += "\", \"name\": \"" +
                        Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(5).get(i)).replace("<", "&lt;").replace(">", "&gt;"));
            }
            
            if (Form_Validation.is_string_null_or_white_space(for_sale_item_reviews.get(6).get(i))) {
                
                output += "\", \"date_received\": \"Not specified";
            } else {
                
                output += "\", \"date_received\": \"" +
                        Find_and_replace.find_and_replace(find, replace, String.valueOf(for_sale_item_reviews.get(6).get(i)).replace("<", "&lt;").replace(">", "&gt;"));
            }
            
            output += "\"}, ";
        }
        
        output += "{}]";
        
        output = output.replace(", {}", "");
        
        return output;
    }
    
    public static String show_page_numbers() {
        
        String output = "";
        
        output += "[";
        
        for (int i = 0; i < page_number_count; i++) {
            
            output += "{\"page_number\": \"" + (i + 1) + "\"}, "; 
        }
        
        output += "{}]";
        
        output = output.replace(", {}", "");
        
        return output;
    }
}
