//Author: Timothy van der Graaff
package apps;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import utilities.Find_and_replace;
import utilities.Form_Validation;

import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
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
	@PostMapping("/send-email-to-vendor")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);
        
        response.setContentType("text/html");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        
        PrintWriter out = response.getWriter();
		
		if (String.valueOf(request.getParameter("send_text")).equals("Send")) {
			
			String domain = "";
			String port = "";
			String subject = "";
			String post_title = "";
			String post_content = "";
			String post_link = "";
			String vendor_first_name = "";
			String vendor_last_name = "";
			String vendor_display_name = "";
			String customer_name = "";
			String customer_first_name = "";
			String customer_last_name = "";
			String customer_email = "";
			String customer_billing_phone = "";
			String customer_billing_address_line_1 = "";
			String customer_billing_address_line_2 = "";
			String customer_billing_city = "";
			String customer_billing_state = "";
			String customer_billing_country = "";
			String customer_billing_post_code = "";
			String customer_biography = "";
			String personal_message = "";
			String message_content = "";
			
			final String username = String.valueOf(request.getParameter("username"));
			final String password = String.valueOf(request.getParameter("password"));
			
			domain = String.valueOf(request.getParameter("domain"));
			port = String.valueOf(request.getParameter("port"));
			subject = String.valueOf(request.getParameter("subject"));
			post_title = String.valueOf(request.getParameter("post_title"));
			post_content = String.valueOf(request.getParameter("post_content"));
			post_link = String.valueOf(request.getParameter("post_link"));
			vendor_first_name = String.valueOf(request.getParameter("vendor_first_name"));
			vendor_last_name = String.valueOf(request.getParameter("vendor_last_name"));
			vendor_display_name = String.valueOf(request.getParameter("vendor_display_name"));
			customer_name = String.valueOf(request.getParameter("customer_name"));
			customer_first_name = String.valueOf(request.getParameter("customer_first_name"));
			customer_last_name = String.valueOf(request.getParameter("customer_last_name"));
			customer_email = String.valueOf(request.getParameter("customer_email"));
			customer_billing_phone = String.valueOf(request.getParameter("customer_billing_phone"));
			customer_billing_address_line_1 = String.valueOf(request.getParameter("customer_billing_address_line_1"));
			customer_billing_address_line_2 = String.valueOf(request.getParameter("customer_billing_address_line_2"));
			customer_billing_city = String.valueOf(request.getParameter("customer_billing_city"));
			customer_billing_state = String.valueOf(request.getParameter("customer_billing_state"));
			customer_billing_country = String.valueOf(request.getParameter("customer_billing_country"));
			customer_billing_post_code = String.valueOf(request.getParameter("customer_billing_post_code"));
			customer_biography = String.valueOf(request.getParameter("customer_biography"));
			personal_message = String.valueOf(request.getParameter("personal_message"));
			
			ArrayList<String> find = new ArrayList<>();
			ArrayList<String> replace = new ArrayList<>();
				
			find.add("(");
			find.add(")");
			find.add(", ");
			
			replace.add("");
			replace.add("");
			replace.add(",");
			
			//Multiple recipients can receive this message, by using the each_vendor_text_email array variable.
			message_content += "\n\nProduct: " + post_title + "\r\n";
			message_content += "\n\n" + post_content + "\r\n";
			message_content += "View your item: " + post_link + "\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(vendor_first_name)) && !(Form_Validation.is_string_null_or_white_space(vendor_last_name))) {
				
				message_content += "\n\n" + vendor_first_name + " " + vendor_last_name + ", \r\n";
			} else {
				
				message_content += "\n\n" + vendor_display_name + ", \r\n";
			}
			
			message_content += "the customer's information is listed below.\r\n\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_name))) {
				
				message_content += "Name:\r\n";
				message_content += customer_name + "\r\n";
			} else {
				
				message_content += "First name:\r\n";
				
				if (!(Form_Validation.is_string_null_or_white_space(customer_first_name))) {
					
					message_content += customer_first_name + "\r\n\n";
				} else {
					
					message_content += "(unknown)\r\n\n";
				}
				
				message_content += "Last name:\r\n";
				
				if (!(Form_Validation.is_string_null_or_white_space(customer_last_name))) {
					
					message_content += customer_last_name + "\r\n\n";
				} else {
					
					message_content += "(unknown)\r\n\n";
				}
			}
			
			message_content += "Email:\r\n";
			message_content += customer_email + "\r\n\n";
			message_content += "Phone:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_billing_phone))) {
				
				message_content += customer_billing_phone + "\r\n\n";
			} else {
				
				message_content += "(unknown)\r\n\n";
			}
			
			message_content += "Address:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_billing_address_line_1))) {
				
				message_content += customer_billing_address_line_1 + "\r\n";
				
				if (!(Form_Validation.is_string_null_or_white_space(customer_billing_address_line_2))) {
					
					message_content += customer_billing_address_line_2 + "\r\n\n";
				} else {
					
					message_content += "\r\n\n;";
				}
			} else {
				
				message_content += "(unknown)\r\n\n";
			}
			
			message_content += "City:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_billing_city))) {
				
				message_content += customer_billing_city + "\r\n\n";
			} else {
				
				message_content += "(unknown)\r\n\n";
			}
			
			message_content += "State:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_billing_state))) {
				
				message_content += customer_billing_state + "\r\n\n";
			} else {
				
				message_content += "(unknown)\r\n\n";
			}
			
			message_content += "Country:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_billing_country))) {
				
				message_content += customer_billing_country + "\r\n\n";
			} else {
				
				message_content += "(unknown)\r\n\n";
			}
			
			message_content += "Zip code:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_billing_post_code))) {
				
				message_content += customer_billing_post_code + "\r\n\n";
			} else {
				
				message_content += "(unknown)\r\n\n";
			}
			
			message_content += "Biography:\r\n";
			
			if (!(Form_Validation.is_string_null_or_white_space(customer_biography))) {
				
				message_content += customer_biography + "\r\n\n";
			} else {
				
				message_content += "\r\n\n";
			}
			
			if (!(Form_Validation.is_string_null_or_white_space(personal_message))) {
				
				message_content += "Message:\r\n";
				message_content += personal_message + "\r\n\n";
			}
			
			message_content += "Do not reply to this message.\r\n";
			
			try {
				
				String[] each_vendor_text_email = Find_and_replace.find_and_replace(find, replace,
                    String.valueOf(request.getParameter("vendor_text_email"))).split(",");
				
				for (int i = 0; i < each_vendor_text_email.length; i++) {
					
					try {
						
						Properties props = new Properties();
						props.put("mail.smtp.host", domain);
						props.put("mail.smtp.port", port);
						props.put("mail.debug", "false");
						props.put("mail.smtp.auth", "true");
						//props.put("mail.smtp.starttls.enable", "true");
						props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
						
						Authenticator auth = new Authenticator() {
							
							//override the getPasswordAuthentication method
							protected PasswordAuthentication getPasswordAuthentication() {
								
								return new PasswordAuthentication(username, password);
							}
						};
						
						Session session = Session.getDefaultInstance(props, auth);
						MimeMessage message = new MimeMessage(session);
						message.setFrom(new InternetAddress(username));
						//DestinationPhoneNumber@sms.ipipi.com
						message.setRecipient(RecipientType.TO, new InternetAddress(each_vendor_text_email[i]));
						
						LOGGER.log(Level.INFO, "Address: " + each_vendor_text_email[i]);
						message.setSubject(subject);
						message.setContent(message_content, "text/plain;charset=UTF-8"); // as "text/plain"
						message.setSentDate(new Date());
						Transport.send(message);
						
						out.println("");
					} catch (MessagingException e) {
						
						LOGGER.log(Level.INFO, e.getMessage());
						
						out.println("");
					}
				}
			} catch (NullPointerException e) {
				
				LOGGER.log(Level.INFO, e.getMessage());
				
				out.println("");
			}
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
