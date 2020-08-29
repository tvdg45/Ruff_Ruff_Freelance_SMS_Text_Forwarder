//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Find_and_replace;

public class Show_Shopping_Cart_Items {
    
    //global variables
    public static ArrayList<ArrayList<String>> all_guest_shopping_cart_items = new ArrayList<>();
    public static ArrayList<ArrayList<String>> shopping_cart_items = new ArrayList<>();
    public static int page_number_count;
    
    public static String show_all_guest_shopping_cart_items() {
        
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
        
        for (int i = 0; i < all_guest_shopping_cart_items.get(0).size(); i++) {
            
            output += "{\"row_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(0).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(1).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"thumbnail\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(2).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item_category\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(3).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"description\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(4).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"price\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(5).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"quantity\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(6).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(all_guest_shopping_cart_items.get(7).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        output = output.replace(", {}", "");
        
        return output;
    }
    
    public static String show_shopping_cart_items() {
        
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
        
        for (int i = 0; i < shopping_cart_items.get(0).size(); i++) {
            
            output += "{\"row_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(0).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(1).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"thumbnail\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(2).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item_category\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(3).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"description\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(4).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"price\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(5).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"quantity\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(6).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\", \"item_id\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(shopping_cart_items.get(7).get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\"}, ";
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