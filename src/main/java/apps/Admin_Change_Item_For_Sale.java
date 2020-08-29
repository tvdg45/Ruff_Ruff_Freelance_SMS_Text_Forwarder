//Author: Timothy van der Graaff
package apps;

import configuration.Config;
import controllers.Control_Change_For_Sale_Items;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Admin_Change_Item_For_Sale extends HttpServlet {
    
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
  
        PrintWriter out = response.getWriter();
  
        Connection use_open_connection;
  
        use_open_connection = Config.openConnection();
        
        if (String.valueOf(request.getParameter("change_item_for_sale")).equals("Change item")) {
            
            Control_Change_For_Sale_Items.use_connection = use_open_connection;
            Control_Change_For_Sale_Items.item = String.valueOf(request.getParameter("item"));
            Control_Change_For_Sale_Items.thumbnail = String.valueOf(request.getParameter("thumbnail"));
            Control_Change_For_Sale_Items.category = String.valueOf(request.getParameter("category")).split(",");
            Control_Change_For_Sale_Items.description = String.valueOf(request.getParameter("description"));
            Control_Change_For_Sale_Items.price = String.valueOf(request.getParameter("price"));
            Control_Change_For_Sale_Items.inventory = String.valueOf(request.getParameter("inventory"));
            Control_Change_For_Sale_Items.row_id = String.valueOf(request.getParameter("row_id"));
            Control_Change_For_Sale_Items.change_item_for_sale = String.valueOf(request.getParameter("change_item_for_sale"));
            
            out.println(Control_Change_For_Sale_Items.control_change_item_for_sale());
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
