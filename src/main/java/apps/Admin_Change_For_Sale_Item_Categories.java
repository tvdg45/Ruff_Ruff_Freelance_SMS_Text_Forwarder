//Author: Timothy van der Graaff
package apps;

import controllers.Control_Change_For_Sale_Items;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.IOException;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import utilities.Find_and_replace;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Admin_Change_For_Sale_Item_Categories extends HttpServlet {

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
        
        response.addHeader("Access-Control-Allow-Origin", "https://tdscloud-dev-ed--c.visualforce.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        
        Connection use_open_connection;
        
        use_open_connection = configuration.Config.openConnection();
        
        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("hh:mm a 'EST'");
          
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
  
        Control_Change_For_Sale_Items.use_connection = use_open_connection;
        
        String category_exception = "";
        String external_id_exception = "";
        
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> replace = new ArrayList<>();
        
        find.add("(");
        find.add(")");
        find.add("; ");
        
        replace.add("");
        replace.add("");
        replace.add(";");
        
        try {
            
            String[] category = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("category"))).split(";");
            
            Control_Change_For_Sale_Items.category = category;
        } catch (NullPointerException e) {
            
            category_exception = "yes";
        }
        
        try {
            
            String[] external_id = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("external_id"))).split(";");
            
            Control_Change_For_Sale_Items.external_id = external_id;
        } catch (NullPointerException e) {
            
            external_id_exception = "yes";
        }
        
        Control_Change_For_Sale_Items.date_received = String.valueOf(localDate);
        Control_Change_For_Sale_Items.time_received = String.valueOf(time_format.format(localTime));
        
        if (String.valueOf(request.getParameter("add_categories")).equals("Add categories")
            && !(category_exception.equals("yes") || external_id_exception.equals("yes"))) {
            
            Control_Change_For_Sale_Items.control_add_sale_categories();
        }
        
        if (String.valueOf(request.getParameter("change_categories")).equals("Change categories")
            && !(category_exception.equals("yes") || external_id_exception.equals("yes"))) {
            
            Control_Change_For_Sale_Items.control_change_sale_categories();
        }
        
        if (String.valueOf(request.getParameter("delete_categories")).equals("Delete categories")
            && !(external_id_exception.equals("yes"))) {
            
            Control_Change_For_Sale_Items.control_delete_sale_categories();
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
