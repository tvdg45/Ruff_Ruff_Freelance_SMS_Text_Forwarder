//Author: Timothy van der Graaff
package views;

import java.util.ArrayList;
import utilities.Find_and_replace;

public class Show_Reviews_Update {
    
    public static ArrayList<String> form_messages = new ArrayList<>();
    
    public static String show_form_messages() {
        
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
        
        for (int i = 0; i < form_messages.size(); i++) {
            
            output += "{\"form_message\": \"" +
                    Find_and_replace.find_and_replace(find, replace, String.valueOf(form_messages.get(i)).replace("<", "&lt;").replace(">", "&gt;")) +
                    "\"}, ";
        }
        
        output += "{}]";
        
        output = output.replace(", {}", "");
        
        return output;
    }
}
