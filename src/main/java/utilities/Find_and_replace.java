//Author: Timothy van der Graaff
package utilities;

import java.util.ArrayList;

public class Find_and_replace {

    //This method finds substrings and replaces them with substitute substrings.
    public static String find_and_replace(ArrayList<String> find, ArrayList<String> replace, String input_value) {
        
        String output;
        
        for (int i = 0; i < find.size(); i++) {
            
            input_value = input_value.replace(find.get(i), replace.get(i));
        }
        
        output = input_value;
        
        return output;
    }
}
