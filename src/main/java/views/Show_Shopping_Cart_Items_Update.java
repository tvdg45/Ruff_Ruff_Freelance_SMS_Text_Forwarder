//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Find_and_replace;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Show_Shopping_Cart_Items_Update {

    public static int shopping_cart_total_quantity;
    public static ArrayList<String> receipts;
    public static ArrayList<ArrayList<String>> items_sold;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static String add_item_error_message() {
        
        String output = "";
        
        output += "problem adding item";
        
        return output;
    }
    
    public static String item_added_message() {
        
        String output = "";
        
        output += "item added";
        
        return output;
    }
    
    public static String change_item_error_message() {
        
        String output = "";
        
        output += "problem changing item";
        
        return output;
    }
    
    public static String item_changed_message() {
        
        String output = "";
        
        if (shopping_cart_total_quantity > 0) {
           
            output += "updated - still has items";
        } else {
            
            output += "updated - shopping cart empty";
        }
        
        return output;
    }
    
    public static String item_deleted_message() {
        
        String output = "";
        
        if (shopping_cart_total_quantity > 0) {
           
            output += "updated - still has items";
        } else {
            
            output += "updated - shopping cart empty";
        }
		
            LOGGER.log(Level.INFO, "item deletion: " + output);
        
        return output;
    }
    
    public static String show_receipts() {
        
        String output = "";
        
        ArrayList find = new ArrayList<>();
        ArrayList replace = new ArrayList<>();
        
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
        
        for (int i = 0; i < receipts.size(); i++) {
            
            output += "{\"receipt\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, receipts.get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        return output;
    }
    
    public static String show_items_sold() {
        
        String output = "";
        
        ArrayList find = new ArrayList<>();
        ArrayList replace = new ArrayList<>();
        
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
        
        for (int i = 0; i < items_sold.get(0).size(); i++) {
            
            output += "{\"item\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, items_sold.get(0).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"thumbnail\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, items_sold.get(1).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"category\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, items_sold.get(2).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"description\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, items_sold.get(3).get(i).replaceAll("[\r\n]+", " ")) +
                    "\", \"total_price\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, items_sold.get(4).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\", \"receipt_id\": \"" +
                    Find_and_replace.find_and_replace(
                            find, replace, items_sold.get(5).get(i).replaceAll("[\r\n]+", " ").replaceAll("<", "&lt;").replaceAll(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        return output;
    }
}
