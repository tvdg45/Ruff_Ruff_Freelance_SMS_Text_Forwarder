//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Reviews;
import utilities.Security_Code_Generator;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Enable_Review_Deletion extends HttpServlet {
    
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
        
        if (String.valueOf(request.getParameter("enable_review_deletion")).equals("Enable")) {
            
            //Set this default string, so that it can be randomly scrambled in to a specified number of characters.
            Security_Code_Generator.entire_string = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            
            //Specify the number of characters for the new security code.
            Security_Code_Generator.number_of_characters = 25;
            
            Control_Change_Reviews.use_connection = use_open_connection;
            Control_Change_Reviews.new_security_code = String.valueOf(Security_Code_Generator.generate_hash());
            Control_Change_Reviews.security_code = String.valueOf(request.getParameter("security_code"));
            Control_Change_Reviews.row_id = String.valueOf(request.getParameter("row_id"));
            Control_Change_Reviews.item_id = String.valueOf(request.getParameter("item_id"));
            Control_Change_Reviews.enable_review_deletion = String.valueOf(request.getParameter("enable_review_deletion"));
            
            out.println();
            out.println(Control_Change_Reviews.control_enable_review_deletion());
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