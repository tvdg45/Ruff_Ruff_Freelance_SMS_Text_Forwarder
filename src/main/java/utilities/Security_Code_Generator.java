//Author: Timothy van der Graaff
package utilities;

import java.util.Random;

public class Security_Code_Generator {
    
    public static String entire_string;
    public static int number_of_characters;
    
    //Randomly choose a string position each time this
    //method is called;
    private static int get_random_string_position() {
        
        int random_string_position;
        
        Random random_generator = new Random();
        random_string_position = random_generator.nextInt(entire_string.length());
        
        if (random_string_position - 1 == -1) {
            
            return random_string_position;
        } else {
            
            return random_string_position - 1;
        }
    }
    
    public static String generate_hash() {
        
        int string_position;
        char this_character;
        
        StringBuilder random_string = new StringBuilder();
        
        //This loop executes a specified number of times,
        //depending on the desired number of characters in the new string.
        for (int i = 0; i < number_of_characters; i++) {
            
            //The string position retrieves one character and
            //adds it to the new string.
            string_position = get_random_string_position();
            this_character = entire_string.charAt(string_position);
            random_string.append(this_character);
        }
        
        return random_string.toString();
    }
}
