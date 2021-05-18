//Author: Timothy van der Graaff
package apps;

import controllers.Control_Search_Email_Address;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Search_Email_Address extends HttpServlet {
    
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
	@GetMapping("/search-email-address")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);

        response.addHeader("Access-Control-Allow-Origin", "https://www.ruff-ruff.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        PrintWriter out = response.getWriter();
  
        Connection use_open_connection;
  
        use_open_connection = configuration.Config.openConnection();
  
        String first_name = String.valueOf(request.getParameter("first_name"));
        String last_name = String.valueOf(request.getParameter("last_name"));
        String email = String.valueOf(request.getParameter("email"));
        String biography = String.valueOf(request.getParameter("biography"));
        String product = String.valueOf(request.getParameter("product"));
        String register_as_customer = String.valueOf(request.getParameter("register_as_customer"));
        String register_as_vendor = String.valueOf(request.getParameter("register_as_vendor"));
        String log_in = String.valueOf(request.getParameter("log_in"));
		
		product = "saint-bernard";
		log_in = "Log in";
  
        //Control_Search_Email_Address.use_connection = use_open_connection;
        Control_Search_Email_Address.first_name = first_name;
        Control_Search_Email_Address.last_name = last_name;
        Control_Search_Email_Address.email = email;
        Control_Search_Email_Address.biography = biography;
        Control_Search_Email_Address.product = product;
        Control_Search_Email_Address.register_as_customer = register_as_customer;
        Control_Search_Email_Address.register_as_vendor = register_as_vendor;
        
        Control_Search_Email_Address.log_in = log_in;
        
        out.println(Control_Search_Email_Address.control_search_email_via_login());
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
    @PostMapping("/search-email-address")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);

        /*response.addHeader("Access-Control-Allow-Origin", "https://www.ruff-ruff.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        PrintWriter out = response.getWriter();
  
        Connection use_open_connection;
  
        use_open_connection = configuration.Config.openConnection();
  
        String first_name = String.valueOf(request.getParameter("first_name"));
        String last_name = String.valueOf(request.getParameter("last_name"));
        String email = String.valueOf(request.getParameter("email"));
        String biography = String.valueOf(request.getParameter("biography"));
        String product = String.valueOf(request.getParameter("product"));
        String register_as_customer = String.valueOf(request.getParameter("register_as_customer"));
        String register_as_vendor = String.valueOf(request.getParameter("register_as_vendor"));
        String log_in = String.valueOf(request.getParameter("log_in"));
		
		product = "saint-bernard";
		log_in = "Log in";
  
        Control_Search_Email_Address.use_connection = use_open_connection;
        Control_Search_Email_Address.first_name = first_name;
        Control_Search_Email_Address.last_name = last_name;
        Control_Search_Email_Address.email = email;
        Control_Search_Email_Address.biography = biography;
        Control_Search_Email_Address.product = product;
        Control_Search_Email_Address.register_as_customer = register_as_customer;
        Control_Search_Email_Address.register_as_vendor = register_as_vendor;
        
        Control_Search_Email_Address.log_in = log_in;
        
        out.println(Control_Search_Email_Address.control_search_email_via_login());*/  
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
