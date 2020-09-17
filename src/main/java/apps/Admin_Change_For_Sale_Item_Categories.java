//Author: Timothy van der Graaff
package apps;

import java.io.IOException;

import java.sql.Connection;
import configuration.Config;
import utilities.Form_Validation;
import controllers.Control_Search_Company_Users;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "https://tdscloud-dev-ed--c.visualforce.com", maxAge = 3600)
@RestController
//@EnableAutoConfiguration
public class Admin_Chat_Interface extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    }
    
    @Override
    @RequestMapping("/admin-chat-interface")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        processRequest(request, response);
        
        response.addHeader("Access-Control-Allow-Origin", "https://tdscloud-dev-ed--c.visualforce.com");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        
        PrintWriter out = response.getWriter();
        
        Connection use_open_connection;
  
        use_open_connection = Config.openConnection();
        
        String admin_session;
        String guest_full_name = "";
        String guest_session = "";
        String conversation_owner = "";
        String user_status;
		
        out.println("<label>If page does not load, click <a href=\"https://tdscloud-dev-ed.lightning.force.com/lightning/page/home\">here</a> to refresh.</label><br /><br />");
        
        //Attempt to find a logged in administrator.
        if (request.getParameter("admin_session") == null) {
      
            admin_session = "";
        } else {
      
            admin_session = request.getParameter("admin_session");
        }
  
        //Attempt to find a logged in guest.
        try {
      
            Cookie each_cookie[] = request.getCookies();
            
            if (each_cookie.length == 3) {
                
                guest_full_name = each_cookie[0].getValue();
                guest_session = each_cookie[1].getValue();
                conversation_owner = each_cookie[2].getValue();
            } else {
                
                Cookie cookie_guest_full_name = new Cookie("guest_full_name", "");
        
                cookie_guest_full_name.setMaxAge(0);
        
                response.addCookie(cookie_guest_full_name);
        
                Cookie cookie_guest_session = new Cookie("guest_session", "");
        
                cookie_guest_session.setMaxAge(0);
        
                response.addCookie(cookie_guest_session);
            
                Cookie cookie_conversation_owner = new Cookie("conversation_owner", "");
        
                cookie_conversation_owner.setMaxAge(0);
        
                response.addCookie(cookie_conversation_owner);
                
                out.println("<script type=\"text/javascript\">");
                out.println("window.location = document.location.href.replace(\"#\", \"\");");
                out.println("</script>");
            } 
        } catch (NullPointerException e) {
      
            if (e.getMessage() == null) {
        
                guest_full_name = "";
                guest_session = "";
                conversation_owner = "";
            }
        }
        
        out.println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery-3.2.1.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery.min.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"https://www.timothysdigitalsolutions.com/backstretch/js/jquery.backstretch.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"https://chat-app-node-1.herokuapp.com/socket.io/socket.io.js\"></script>");
        
        if (!(admin_session.equals("")) || (!(guest_full_name.equals("")) && !(guest_session.equals(""))
            && !(conversation_owner.equals("")))) {
            
            out.println("<script type=\"text/javascript\">");
            out.println("function check_html(html) {\n" +
                    "	\n" +
                    "	var doc = document.createElement('div');\n" +
                    "	\n" +
                    "	doc.innerHTML = html;\n" +
                    "	\n" +
                    "	return (doc.innerHTML === html);\n" +
                    "}");
            out.println("");
            out.println("var number_of_submissions = 0;");
            out.println("");
            out.println("setInterval(function() {");
            out.println("");
            out.println("number_of_submissions = 0;");
            out.println("}, 1000);");
            out.println("");
            out.println("function send_message() {");
            out.println("");
            out.println("number_of_submissions++;");
            out.println("");
            out.println("document.getElementById(\"message\").innerHTML = " +
                    "document.getElementById(\"message\").innerHTML.replace(/<div><br><\\/div><div><br><\\/div>/g, \"\");");
            out.println("");
            out.println("if (number_of_submissions <= 1 " +
                    "&& document.getElementById(\"message\").innerHTML != \"\" " +
                    "&& document.getElementById(\"message\").innerHTML.replace(/<div><br><\\/div><div><br><\\/div>/g, \"\").length > 0 " +
                    "&& document.getElementById(\"message\").innerHTML.replace(/\\s/g, \"\").length > 0) {");
            out.println("");
            out.println("var xhttp = new XMLHttpRequest();");
            out.println("");
            out.println("xhttp.onreadystatechange = function() {");
            out.println("");
            out.println("if (this.readyState == 4 && this.status == 200) {");
            out.println("");
            out.println("$(\"#send_message\").html(this.responseText);");
            out.println("}");
            out.println("};");
            out.println("");
            out.println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-create-message\");");
            out.println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            out.println("");
            out.println("var message = \"\";");
            out.println("");
            out.println("if (!(check_html(document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\")))) {");
            out.println("");
            out.println("message = document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
            out.println("} else {");
            out.println("");
            out.println("message = document.getElementById(\"message\").innerHTML.replace(/<a/g, \"<a target=\\\"_blank\\\"\");");
            out.println("}");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                out.println("xhttp.send(\"message=\" + message + \"&admin_session=" + admin_session + "&create_message=Send\");");
            } else {
                
                out.println("xhttp.send(\"message=\" + message + \"&create_message=Send\");");
            }
            
            out.println("");
            out.println("document.getElementById(\"message\").innerHTML = \"\";");
            out.println("}");
            out.println("}");
            out.println("");
            out.println("var socket = io.connect(\"https://chat-app-node-1.herokuapp.com\");");
            out.println("");
            out.println("window.onload = function() {");
            out.println("");
            out.println("var xhttp = new XMLHttpRequest();");
            out.println("");
            out.println("xhttp.onreadystatechange = function() {");
            out.println("");
            out.println("if (this.readyState == 4 && this.status == 200) {");
            out.println("");
            out.println("var compare_socket_data = \"\";");
            out.println("");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                out.println("compare_socket_data = \"" + admin_session + "\";");
            } else {
                
                out.println("compare_socket_data = \"" + conversation_owner + "\";");
            }
            
            out.println("");
            out.println("if (this.responseText != \"\" && this.responseText.replace(/\\s/g, \"\").length != 0 && this.responseText != \"[object XMLDocument]\") {");
            out.println("");
            out.println("var live_thread;");
            out.println("var thread_content = \"\";");
            out.println("");
            out.println("live_thread = JSON.parse(this.responseText.replace(\", {}\", \"\"));");
            out.println("");
            
            if (Form_Validation.is_string_null_or_white_space(admin_session)) {
                
                out.println("if (live_thread[0][\"user_id\"] == \"log user out\") {");
                out.println("");
                out.println("window.location = document.location.href.replace(\"#\", \"\");");
                out.println("}");
            }
            
            out.println("");
            out.println("for (var i = 0; i < live_thread.length; i++) {");
            out.println("");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {

                out.println("if (live_thread[i][\"conversation_owner\"] == \"" + admin_session + "\") {");
                out.println("");
                out.println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                        "padding-bottom: 20px; word-wrap: break-word'>\";");
                out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";");
                out.println("");
                out.println("if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                out.println("");
                out.println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                out.println("} else {");
                out.println("");
                out.println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                out.println("}");
                out.println("");
                out.println("thread_content += \"</label><br /><br />\";");
                out.println("thread_content += \"</div>\";");
                out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                        "\" + live_thread[i][\"time_received\"] + \"</b></label>\";");
                out.println("thread_content += \"</div>\";");
                out.println("thread_content += \"</div>\";");
                out.println("}");
            } else {
                
                Control_Search_Company_Users.use_connection = use_open_connection;
                
                user_status = Control_Search_Company_Users.control_search_company_user_status(conversation_owner);
                
                if (!(user_status.equals("Available - online"))) {
                    
                    if (user_status.equals("Available - offline")) {
                        
                        out.println("if (live_thread[i][\"user_id\"] == \"" + guest_session + "\" && " +
                                "live_thread[i][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        out.println("");
                        out.println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";");
                        out.println("");
                        out.println("if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        out.println("");
                        out.println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        out.println("} else {");
                        out.println("");
                        out.println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        out.println("}");
                        out.println("");
                        out.println("thread_content += \"</label><br /><br />\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                                "\" + live_thread[i][\"time_received\"] + \"</b></label>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("}");                        
                    } else {
                        
                        out.println("if (live_thread[i][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        out.println("");
                        out.println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>\" + live_thread[i][\"full_name\"] + \":</b> \";");
                        out.println("");
                        out.println("if (!(check_html(live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        out.println("");
                        out.println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        out.println("} else {");
                        out.println("");
                        out.println("thread_content += live_thread[i][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        out.println("}");
                        out.println("");
                        out.println("thread_content += \"</label><br /><br />\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[i][\"date_received\"] + \" at " +
                                "\" + live_thread[i][\"time_received\"] + \"</b></label>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("}");                        
                    }
                }                
            }
            
            out.println("}");
            out.println("");
            out.println("$(\"#thread\").html(thread_content);");
            out.println("document.getElementById(\"conversation\").scrollTop = document.getElementById(\"conversation\").scrollHeight - document.getElementById(\"conversation\").clientHeight;");
            out.println("} else {");
            out.println("");
            out.println("$(\"#thread\").html(\"\");");
            out.println("}");
            out.println("}");
            out.println("};");
            out.println("");
            out.println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-search-messages\");");
            out.println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            out.println("");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                out.println("xhttp.send(\"conversation_owner=" + admin_session + "&admin_session=" + admin_session + "\");");
            } else {
                
                out.println("xhttp.send(\"conversation_owner=" + conversation_owner + "\");");
            }
            
            out.println("}");
            
            if (!(Form_Validation.is_string_null_or_white_space(conversation_owner))
                && Form_Validation.is_string_null_or_white_space(admin_session)) {
                
                out.println("");
                out.println("socket.on('log_other_users_out', function(data) {");
                out.println("");
                out.println("if (data == \"" + conversation_owner + "\") {");
                out.println("");
				out.println("window.location = document.location.href.replace(\"#\", \"\");");
				out.println("");
                out.println("log_out();");
                out.println("}");
                out.println("});");
            }
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {

                out.println("");
                out.println("socket.on('refresh_admin_window', function(data) {");
                out.println("");
                out.println("if (data == \"" + admin_session + "\") {");
                out.println("");
                out.println("window.location = document.location.href.replace(\"#\", \"\");");
                out.println("}");
                out.println("});");
            }
            
            out.println("");
            out.println("socket.on('load_threads', function(data) {");
            out.println("");
            out.println("var live_thread;");
            out.println("var thread_content = \"\";");
            out.println("");
            out.println("live_thread = data;");
            out.println("");
            
            Control_Search_Company_Users.use_connection = use_open_connection;
            
            user_status = Control_Search_Company_Users.control_search_company_user_status(conversation_owner);
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                out.println("if (live_thread[0][\"conversation_owner\"] == \"" + admin_session + "\") {");
                out.println("");
                out.println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                        "padding-bottom: 20px; word-wrap: break-word'>\";");
                out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[0][\"full_name\"] + \":</b> \";");
                out.println("");
                out.println("if (!(check_html(live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                out.println("");
                out.println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                out.println("} else {");
                out.println("");
                out.println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                out.println("}");
                out.println("");
                out.println("thread_content += \"</label><br /><br />\";");
                out.println("thread_content += \"</div>\";");
                out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>On \" + live_thread[0][\"date_received\"] + \" at " +
                        "\" + live_thread[0][\"time_received\"] + \"</b></label>\";");
                out.println("thread_content += \"</div>\";");
                out.println("thread_content += \"</div>\";");
                out.println("}");
                out.println("");
                out.println("$(\"#thread\").append(thread_content);");
            } else {

                switch (user_status) {
                    case "Available - offline":
                        
                        out.println("if (live_thread[0][\"user_id\"] == \"" + guest_session + "\" && " +
                                "live_thread[0][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        out.println("");
                        out.println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[0][\"full_name\"] + \":</b> \";");
                        out.println("");
                        out.println("if (!(check_html(live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        out.println("");
                        out.println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        out.println("} else {");
                        out.println("");
                        out.println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        out.println("}");
                        out.println("");
                        out.println("thread_content += \"</label><br /><br />\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[0][\"date_received\"] + \" at " +
                                "\" + live_thread[0][\"time_received\"] + \"</b></label>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("}");
                        out.println("");
                        out.println("$(\"#thread\").append(thread_content);");
                        
                        break;
                    case "Occupied":
                        
                        out.println("if (live_thread[0][\"conversation_owner\"] == \"" + conversation_owner + "\") {");
                        out.println("");
                        out.println("thread_content += \"<div style='text-align: left; padding-top: 20px; " +
                                "padding-bottom: 20px; word-wrap: break-word'>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                        "<label style='font-size: 12pt'><b>\" + live_thread[0][\"full_name\"] + \":</b> \";");
                        out.println("");
                        out.println("if (!(check_html(live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\")))) {");
                        out.println("");
                        out.println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\").replace(/</g, \"&lt;\").replace(/>/g, \"&gt;\");");
                        out.println("} else {");
                        out.println("");
                        out.println("thread_content += live_thread[0][\"message\"].replace(/&apos;/g, \"'\").replace(/&quot;/g, \"\\\"\");");
                        out.println("}");
                        out.println("");
                        out.println("thread_content += \"</label><br /><br />\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"<div style='text-align: left; width: 100%'>" +
                                "<label style='font-size: 12pt'><b>On \" + live_thread[0][\"date_received\"] + \" at " +
                                "\" + live_thread[0][\"time_received\"] + \"</b></label>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("thread_content += \"</div>\";");
                        out.println("}");
                        out.println("");
                        out.println("$(\"#thread\").append(thread_content);");
                        
                        break;
                }
            }
            
            out.println("");
            out.println("document.getElementById(\"conversation\").scrollTop = document.getElementById(\"conversation\").scrollHeight - document.getElementById(\"conversation\").clientHeight;");
            out.println("});");
            out.println("");
            out.println("function log_out() {");
            out.println("");
            out.println("var xhttp = new XMLHttpRequest();");
            out.println("");
            out.println("xhttp.onreadystatechange = function() {");
            out.println("");
            out.println("if (this.readyState == 4 && this.status == 200) {");
            out.println("");
            out.println("$(\"#conversation\").html(this.responseText);");
            out.println("}");
            out.println("};");
            out.println("");
            out.println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/log-out\");");
            out.println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            out.println("");
            out.println("xhttp.send(\"log_out=Log out\");");
            out.println("}");
            out.println("");
            out.println("function clear_message() {");
            out.println("");
            out.println("var default_message = document.getElementById(\"message\").innerHTML;");
            out.println("");
            out.println("if (default_message == \"<label>message</label>\") {");
            out.println("");
            out.println("document.getElementById(\"message\").innerHTML = \"\";");
            out.println("}");
            out.println("}");
            out.println("");
            out.println("function default_message() {");
            out.println("");
            out.println("var default_message = document.getElementById(\"message\").innerHTML;");
            out.println("");
            out.println("if (default_message == \"\") {");
            out.println("");
            out.println("document.getElementById(\"message\").innerHTML = \"<label>message</label>\";");
            out.println("}");
            out.println("}");
            out.println("</script>");
            out.println("<div id=\"conversation\" style=\"text-align: left; min-height: 350px; max-height: 350px; overflow: auto; width: 100%\">");
            out.println("<div id=\"thread\" style=\"text-align: left; width: 100%\"></div>");
            out.println("</div>");
            out.println("<div style=\"text-align: left; width: 100%\"><br />");
            out.println("<div id=\"message\" contenteditable=\"true\" onfocusout=\"default_message()\" onfocus=\"clear_message()\" style=\"width: 100%; overflow: auto; min-height: 100px; max-height: 100px\"><label>message</label></div>");
            out.println("<br /><br /><input id=\"create_message\" type=\"button\" value=\"Send\" onclick=\"send_message()\" />");
            
            if (Form_Validation.is_string_null_or_white_space(admin_session)) {
            
                out.println("<input id=\"sign_out\" type=\"button\" value=\"Leave chat\" onclick=\"log_out()\" />");
            } else {
                
                Control_Search_Company_Users.use_connection = use_open_connection;
                
                if (!(Control_Search_Company_Users.control_search_company_user_status(admin_session).equals("Occupied"))) {
                    
                    out.println("");
                    out.println("<script type=\"text/javascript\">");
                    out.println("document.getElementById(\"message\").innerHTML = \"<label>message</label>\";");
                    out.println("document.getElementById(\"message\").contentEditable = false;");
                    out.println("document.getElementById(\"create_message\").disabled = true;");
                    out.println("</script>");
                }
            }
            
            out.println("</div>");
            
            if (!(Form_Validation.is_string_null_or_white_space(admin_session))) {
                
                out.println("<div style=\"text-align: left; width: 100%\"><br />");
                out.println("<label>Current status: " +
                        Control_Search_Company_Users.control_search_company_user_status(admin_session) + "</label>");
                out.println("</div>");
                out.println("<div style=\"text-align: left; width: 100%\"><br />");
                out.println("<label>Change status:</label>");
                out.println("<select id=\"status\" style=\"width: 98%\">");
                out.println("<option value=\"Choose\">Choose</option>");
                out.println("<option value=\"Available - online\">Available - online</option>");
                out.println("<option value=\"Available - offline\">Available - offline</option>");
                out.println("<option value=\"Not available\">Not available</option>");
                out.println("</select>");
                out.println("</div>");
                out.println("<div style=\"text-align: left; width: 100%\"><br />");
                out.println("<input type=\"button\" id=\"change_status\" value=\"Change status\" onclick=\"change_user_status()\" /><br /><br />");
                out.println("</div>");
                out.println("<script type=\"text/javascript\">");
                out.println("");
                out.println("function change_user_status() {");
                out.println("");
                out.println("var xhttp = new XMLHttpRequest();");
                out.println("");
                out.println("xhttp.onreadystatechange = function() {");
                out.println("");
                out.println("if (this.readyState == 4 && this.status == 200) {");
                out.println("");
                out.println("$(\".change_status\").html(this.responseText);");
                out.println("}");
                out.println("};");
                out.println("");
                out.println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/admin-change-user-status\");");
                out.println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
                out.println("");
                out.println("xhttp.send(\"admin_session=" + 
                        admin_session + "&status=\" + document.getElementById(\"status\").value + " +
                        "\"&change_status=Change status\");");
                out.println("}");
                out.println("</script>");
            }
            
            out.println("<div class=\"change_status\" style=\"text-align: left; width: 100%\"></div>");
            out.println("<div id=\"send_message\" style=\"text-align: left; width: 100%\"></div>");
        } else {
            
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<div id=\"step_one\" style=\"text-align: left; width: 100%; display: none\">");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<label>What is your name?</label>");
            out.println("</div>");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<input type=\"text\" id=\"full_name\" style=\"width: 98%\" />");
            out.println("</div>");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<br />");
            out.println("<input type=\"checkbox\" id=\"no_name\" value=\"Anonymous\" onchange=\"exclude_full_name()\" />");
            out.println("<label>I prefer not to answer.</label>");
            out.println("</div>");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<br />");
            out.println("<input type=\"button\" id=\"provide_full_name\" value=\"Next\" onclick=\"provide_full_name()\" />");
            out.println("</div>");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<br />");
            out.println("<div id=\"step_one_feedback\" style=\"text-align: left; width: 100%\"></div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div id=\"step_two\" style=\"text-align: left; width: 100%; display: none\">");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<br />");
            out.println("<input type=\"button\" class=\"return_to_first_step\" value=\"Back\" onclick=\"return_to_first_step()\" />");
            out.println("<br /><br />");
            out.println("</div>");
            
            Control_Search_Company_Users.use_connection = use_open_connection;
            
            out.println(Control_Search_Company_Users.control_search_company_users());
            
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<br />");
            out.println("<input type=\"button\" class=\"return_to_first_step\" value=\"Back\" onclick=\"return_to_first_step()\" />");
            out.println("</div>");
            out.println("<div style=\"text-align: left; width: 100%\">");
            out.println("<br />");
            out.println("<div id=\"step_two_feedback\" style=\"text-align: left; width: 100%\"></div>");
            out.println("</div>");            
            out.println("</div>");
            out.println("</div>");
            out.println("");
            out.println("<script type=\"text/javascript\">");
            out.println("var choose_person = \"\";");
            out.println("");
            out.println("$(document).ready(function() {");
            out.println("");
            out.println("$(\"#step_one\").fadeIn();");
            out.println("document.getElementById(\"full_name\").value = \"\";");
            out.println("});");
            out.println("");
            out.println("function exclude_full_name() {");
            out.println("");
            out.println("if (document.getElementById(\"no_name\").checked) {");
            out.println("");
            out.println("document.getElementById(\"full_name\").disabled = true;");
            out.println("document.getElementById(\"full_name\").value = \"Anonymous\";");
            out.println("} else {");
            out.println("");
            out.println("document.getElementById(\"full_name\").disabled = false;");
            out.println("document.getElementById(\"full_name\").value = \"\";");
            out.println("}");
            out.println("}");
            out.println("");
            out.println("function provide_full_name() {");
            out.println("");
            out.println("if (document.getElementById(\"full_name\").value == \"\" || document.getElementById(\"full_name\").value.replace(/\\s/g, \"\").length == 0) {");
            out.println("");
            out.println("$(\"#step_one_feedback\").html(\"<label><span style=\\\"color: red\\\">Please provide your name.</span></label>\");");
            out.println("} else {");
            out.println("");
            out.println("$(\"#step_one\").fadeOut();");
            out.println("$(\"#step_two\").fadeIn();");
            out.println("}");
            out.println("}");
            out.println("");
            out.println("function select_person(user_id) {");
            out.println("");
            out.println("choose_person = user_id;");
            out.println("log_in();");
            out.println("}");
            out.println("");
            out.println("function return_to_first_step() {");
            out.println("");
            out.println("$(\"#step_two\").fadeOut();");
            out.println("$(\"#step_one\").fadeIn();");
            out.println("}");
            out.println("");
            out.println("function log_in() {");
            out.println("");
            out.println("var guest_session = \"\";");
            out.println("var characters = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\";");
            out.println("");
            out.println("for (var i = 0; i < 20; i++) {");
            out.println("");
            out.println("guest_session += characters.charAt(Math.floor(Math.random() * characters.length));");
            out.println("}");
            out.println("");
            out.println("var xhttp = new XMLHttpRequest();");
            out.println("");
            out.println("xhttp.onreadystatechange = function() {");
            out.println("");
            out.println("if (this.readyState == 4 && this.status == 200) {");
            out.println("");
            out.println("$(\"#step_two_feedback\").html(this.responseText);");
            out.println("}");
            out.println("};");
            out.println("");
            out.println("xhttp.open(\"POST\", \"" + Config.third_party_domain() + "/log-in\");");
            out.println("xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");");
            out.println("");
            out.println("xhttp.send(\"full_name=\" + document.getElementById(\"full_name\").value + \"&guest_session=\" + guest_session + \"&select_person=\" + choose_person + \"&log_in=Log in\");");
            out.println("}");
            out.println("");
            out.println("var socket = io.connect(\"https://chat-app-node-1.herokuapp.com\");");
            out.println("");
            out.println("socket.on('refresh_admin_window', function(data) {");
            out.println("");
            out.println("if (data != \"\") {");
            out.println("");
            out.println("window.location = document.location.href.replace(\"#\", \"\");");
            out.println("}");
            out.println("});");
            out.println("</script>");
        }
    }
	
    public static void main(String[] args) throws Exception, IOException {
		
        SpringApplication.run(Admin_Chat_Interface.class, args);
    }
}
