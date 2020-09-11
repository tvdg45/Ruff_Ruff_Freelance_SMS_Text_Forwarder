//Author: Timothy van der Graaff
package configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public class Config {
    
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private static String database_server;
    private static String database_username;
    private static String database_password;
    private static String database_port;
    private static String database_name;
    private static String database_url;
    
    public static void call_database_information() throws IOException {
        
        String url_for_get_request = "https://tds-webhook.herokuapp.com/tds-webhook-shopping-cart";    
        String read_line;
        
        RestTemplate restTemplate = new RestTemplate();

        //Create headers
        HttpHeaders headers = new HttpHeaders();

        //Request body parameters
        Map<String, Object> request_parameters = new HashMap<>();

        //Build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request_parameters, headers);

        //Send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url_for_get_request, entity, String.class);
		
        if (response.getStatusCode() == HttpStatus.OK) {
            
            String[] credentials = response.getBody().toString().split("\\s*,\\s*");
            
            if (credentials.length == 5) {
                
                try {

                    database_server = credentials[0].trim();
                    database_username = credentials[1].trim();
                    database_password = credentials[2].trim();
                    database_port = credentials[3].trim();
                    database_name = credentials[4].trim();
                    
                    database_url = "jdbc:mysql://" + database_server + ":" + database_port + "/" + database_name;
                } catch (Exception e) {
                }
            }
        }
    }
    
    public static Connection openConnection() throws IOException {
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            
            call_database_information();
            
            Connection connection = DriverManager.getConnection(database_url, database_username, database_password);
            
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            
            LOGGER.log(Level.INFO, "Unable to connect to database: " + e.getMessage());
            
            return null;
        }
    }
    
    public static ArrayList<String> bad_domain() {
        
        ArrayList<String> output = new ArrayList<> ();
        
        output.add("timothysdigitalsolutions.com");
        output.add("http://timothysdigitalsolutions.com");
        output.add("http://www.timothysdigitalsolutions.com");
        output.add("https://timothysdigitalsolutions.com");
        
        return output;
    }
    
    public static String domain() {
        
        String output = "";
        
        //Define any domain name below.  Your domain name can also have a directory included.
        //Example: Directory not included - https://www.timothysdigitalsolutions.com or directory included - https://www.timothysdigitalsolutions.com/contact-me
        String domain = "https://www.timothysdigitalsolutions.com/store";
        
        output += domain;
        
        return output;
    }
    
    public static String third_party_domain() {
        
        String output = "";
        
        //Define any domain name below.  Your domain name can also have a directory included.
        
        //Example: Directory not included - https://www.timothysdigitalsolutions.com or directory included - https://www.timothysdigitalsolutions.com/contact-me
        //String third_party_domain = "https://user-account-management-1.herokuapp.com";
        //String third_party_domain = "https://time-tracker-java.herokuapp.com";
        String third_party_domain = "https://shopping-cart-java.herokuapp.com";
        
        output += third_party_domain;
        
        return output;
    }
}
