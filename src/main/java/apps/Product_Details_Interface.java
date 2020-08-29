//Author: Timothy van der Graaff
package apps;

import controllers.Control_Search_For_Sale_Item_Details;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Product_Details_Interface extends HttpServlet {

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
  
        use_open_connection = configuration.Config.openConnection();
        
        Control_Search_For_Sale_Item_Details.use_connection = use_open_connection;
        
        Control_Search_For_Sale_Item_Details.item_id = String.valueOf(request.getParameter("item_id"));
        Control_Search_For_Sale_Item_Details.results_per_page = "10";
        Control_Search_For_Sale_Item_Details.page_number = "1";
        Control_Search_For_Sale_Item_Details.sort_by = "Newest";
        
        out.println("{\"sale_product_details\": ");
        out.println(Control_Search_For_Sale_Item_Details.control_search_for_sale_item_details() + ",");
        out.println(" \"sale_product_additional_pictures\": ");
        out.println(Control_Search_For_Sale_Item_Details.control_search_for_sale_item_additional_pictures() + ",");
        out.println(" \"sale_product_reviews\": ");
        out.println(Control_Search_For_Sale_Item_Details.control_search_for_sale_item_reviews() + "}");
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