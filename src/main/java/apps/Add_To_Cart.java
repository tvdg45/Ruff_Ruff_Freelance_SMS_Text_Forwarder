//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_Shopping_Cart_Items;

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

public class Add_To_Cart extends HttpServlet {
    
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
        
        if (String.valueOf(request.getParameter("add_to_cart")).equals("Add to cart")) {
            
            Control_Change_Shopping_Cart_Items.use_connection = use_open_connection;
            Control_Change_Shopping_Cart_Items.item = String.valueOf(request.getParameter("item"));
            Control_Change_Shopping_Cart_Items.thumbnail = String.valueOf(request.getParameter("thumbnail"));
            Control_Change_Shopping_Cart_Items.category = String.valueOf(request.getParameter("category"));
            Control_Change_Shopping_Cart_Items.description = String.valueOf(request.getParameter("description"));
            Control_Change_Shopping_Cart_Items.price = String.valueOf(request.getParameter("price"));
            Control_Change_Shopping_Cart_Items.quantity = String.valueOf(request.getParameter("quantity")).split(",");
            Control_Change_Shopping_Cart_Items.raw_time_received = String.valueOf(request.getParameter("raw_time_received"));
            Control_Change_Shopping_Cart_Items.guest_session = String.valueOf(request.getParameter("guest_session"));
            Control_Change_Shopping_Cart_Items.item_id = String.valueOf(request.getParameter("item_id")).split(",");
            Control_Change_Shopping_Cart_Items.date_received = String.valueOf(localDate);
            Control_Change_Shopping_Cart_Items.time_received = String.valueOf(time_format.format(localTime));
            Control_Change_Shopping_Cart_Items.add_to_cart = String.valueOf(request.getParameter("add_to_cart"));
            
            out.println(Control_Change_Shopping_Cart_Items.control_add_to_cart());
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
