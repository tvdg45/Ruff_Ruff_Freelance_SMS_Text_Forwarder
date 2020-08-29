//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Reviews;
import utilities.Security_Code_Generator;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Create_Review extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
    * Handles the HTTP <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //processRequest(request, response);

    }

    /**
    * Handles the HTTP <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);
  
        response.addHeader("Access-Control-Allow-Origin", "https://www.timothysdigitalsolutions.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
  
        PrintWriter out = response.getWriter();
  
        Connection use_open_connection;
  
        use_open_connection = Config.openConnection();
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
        
        if (String.valueOf(request.getParameter("create_review")).equals("Submit")) {
            
            //Set this default string, so that it can be randomly scrambled in to a specified number of characters.
            Security_Code_Generator.entire_string = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            
            //Specify the number of characters for the new security code.
            Security_Code_Generator.number_of_characters = 25;
            
            Control_Change_Reviews.use_connection = use_open_connection;
            Control_Change_Reviews.item_id = String.valueOf(request.getParameter("item_id"));
            Control_Change_Reviews.name = String.valueOf(request.getParameter("name"));
            Control_Change_Reviews.email = String.valueOf(request.getParameter("email"));
            Control_Change_Reviews.rating = String.valueOf(request.getParameter("rating"));
            Control_Change_Reviews.subject = String.valueOf(request.getParameter("subject"));
            Control_Change_Reviews.description = String.valueOf(request.getParameter("description"));
            Control_Change_Reviews.new_security_code = String.valueOf(Security_Code_Generator.generate_hash());
            Control_Change_Reviews.date_received = String.valueOf(localDate);
            Control_Change_Reviews.time_received = String.valueOf(time_format.format(localTime));
            Control_Change_Reviews.create_review = String.valueOf(request.getParameter("create_review"));
            
            out.println(Control_Change_Reviews.control_create_review());
        }
    }

    /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
    
    @Override
    public String getServletInfo() {

        return "Short description";
    } // </editor-fold>
}