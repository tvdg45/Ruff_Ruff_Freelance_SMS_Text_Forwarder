//Author: Timothy van der Graaff
package apps;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.mail.*;

import javax.mail.Message.RecipientType;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@RestController
@EnableAutoConfiguration
public class Send_Email_To_Vendor extends HttpServlet {
    
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
    @GetMapping("/send-email-to-vendor")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);
        
        response.setContentType("text/html");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        
        PrintWriter out = response.getWriter();
  
        try {
              
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.byethost.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
        //props.put("mail.smtp.starttls.enable", "true");
			
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("timothy@timothysdigitalsolutions.com", "ranger12");
			}
		};
        
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("timothy@timothysdigitalsolutions.com"));
        message.setRecipient(RecipientType.TO, new InternetAddress("ltrman1996@hotmail.com"));
        message.setSubject("Notification");
        message.setContent("<h1>Successful!</h1>", "text/html;charset=UTF-8"); // as "text/plain"
        message.setSentDate(new Date());
        Transport.send(message);
  
            out.println("Done");
  
        } catch (MessagingException e) {
            out.println(e.getMessage());
        }
        
      out.println("<br />yes 6");     
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
        
        //processRequest(request, response);
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
