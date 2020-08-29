//Author: Timothy van der Graaff
package apps;

import controllers.Control_Search_For_Sale_Items;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class For_Sale_Items_Interface extends HttpServlet {
    
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
        
        Control_Search_For_Sale_Items.use_connection = use_open_connection;
        
        Control_Search_For_Sale_Items.category = String.valueOf(request.getParameter("category"));
        Control_Search_For_Sale_Items.keywords = String.valueOf(request.getParameter("keywords"));
        Control_Search_For_Sale_Items.results_per_page = String.valueOf(request.getParameter("results_per_page"));
        Control_Search_For_Sale_Items.page_number = String.valueOf(request.getParameter("page_number"));
        Control_Search_For_Sale_Items.sort_by = String.valueOf(request.getParameter("sort_by"));
        Control_Search_For_Sale_Items.search_items = String.valueOf(request.getParameter("search_items"));
        
        out.println("{\"sale_categories\": ");
        out.println(Control_Search_For_Sale_Items.control_search_for_sale_categories() + ",");
        out.println(" \"sale_items\": ");
        out.println(Control_Search_For_Sale_Items.control_search_for_sale_items() + ",");
        out.println(" \"pages\": ");
        out.println(Control_Search_For_Sale_Items.control_calculate_page_number_count() + "}");
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