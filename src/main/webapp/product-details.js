//Global variables for rendering HTML content
var html_code = "";
var product_details;
var shopping_cart_items;
var show_additional_pictures = new Array();
var additional_picture_position_unresponsive = 0;
var additional_picture_position_responsive = 0;
var item_to_be_added;
var domain = location.protocol + "//" + window.location.hostname + "/store";
var third_party_domain = "https://shopping-cart-java.herokuapp.com";
var forward_slash_or_html_extension = "/";

var socket = io.connect("https://shopping-cart-app-node-1.herokuapp.com");

$.url_params = function(name) {
	
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    
    if (results == null){
        
        return null;
    } else {
        
        return decodeURI(results[1]) || 0;
    }
}

function item_not_found_message() {
    
    return "<p>Sorry, that item was not found.  Try <a href=\"" +
            domain + "/store" + forward_slash_or_html_extension + "\">searching</a> for it.</p>";
}

window.onload = function() {
    
    var product = "";
    
    product = $.url_params("product");
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            document.getElementById("product_details").innerHTML = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            document.getElementById("product_details").innerHTML = "<p>Sorry, the online store is not available.</p>";
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            product_details = JSON.parse(this.responseText);
            item_to_be_added = JSON.parse(this.responseText);
            
            //Control search results from query.
            if (product_details["sale_product_details"][0]["item"] == "no item"
                    || product_details["sale_product_details"][0]["item"] == "fail") {
                
                html_code += item_not_found_message();
            } else {
                
                html_code += html_body_pc_format(product_details);
                html_code += html_body_mobile_format(product_details);
                
                //Add to cart form
                html_code += "<div class=\"add_to_cart_background\">";
                html_code += "<div class=\"add_to_cart_form\">";
                html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label><span class=\"close_add_to_cart_form\" onclick=\"hide_add_to_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                html_code += "</div>";
                html_code += "</div>";
                html_code += "</div>";
                
                //Create review form
                html_code += "<div class=\"create_review_background\">";
                html_code += "<div class=\"create_review_form\">";
                html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label><span class=\"close_create_review_form\" onclick=\"hide_create_review_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<div class=\"create_review_form_feedback\" style=\"text-align: left; width: 100%\"></div>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label>Name:</label>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                html_code += "<input type=\"text\" class=\"review_name\" style=\"text-align: left; width: 100%\" />";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label>Email:</label>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                html_code += "<input type=\"text\" class=\"review_email\" style=\"text-align: left; width: 100%\" />";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label>How was your experience?</label>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                html_code += "<select class=\"review_rating\">";
                html_code += "<option value=\"\">Choose</option>";
                html_code += "<option value=\"5\">Excellent</option>";
                html_code += "<option value=\"4\">Good</option>";
                html_code += "<option value=\"3\">Average</option>";
                html_code += "<option value=\"2\">Fair</option>";
                html_code += "<option value=\"1\">Poor</option>";
                html_code += "</select>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label>Subject: (optional)</label>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                html_code += "<input type=\"text\" class=\"review_subject\" style=\"text-align: left; width: 100%\" />";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label>Describe your experience. (optional)</label>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                html_code += "<textarea class=\"review_description\" style=\"text-align: left; width: 100%; height: 200px; min-width: 100%; min-height: 200px; max-width: 100%; max-height: 200px\"></textarea>";
                html_code += "</div>";
                html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px; padding-bottom: 5px\">";
                html_code += "<input type=\"button\" class=\"submit_new_review\" onclick=\"create_new_review()\" value=\"Submit\" />";
                html_code += "</div>";
                html_code += "</div>";
                html_code += "</div>";
                
                //Send review change email request form
                html_code += "<div class=\"send_review_change_email_request_background\">";
                html_code += "<div class=\"send_review_change_email_request_form\">";
                html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label><span class=\"close_send_review_change_email_request_form\" onclick=\"hide_send_review_change_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                html_code += "</div>";
                html_code += "</div>";
                html_code += "</div>";
                
                //Send review deletion email request form
                html_code += "<div class=\"send_review_deletion_email_request_background\">";
                html_code += "<div class=\"send_review_deletion_email_request_form\">";
                html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label><span class=\"close_send_review_deletion_email_request_form\" onclick=\"hide_send_review_deletion_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                html_code += "</div>";
                html_code += "</div>";
                html_code += "</div>";
            }
            
            document.getElementById("product_details").innerHTML = html_code;
            
            //Get guest session information.
            use_guest_session_search_shopping_cart();
            
            //Get product pictures.
            if (product_details["sale_product_details"].length == 1) {
                
                if (product_details["sale_product_additional_pictures"][0]["thumbnail"] != "no picture" && product_details["sale_product_additional_pictures"][0]["thumbnail"] != "fail") {
                    
                    document.getElementById("additional_pictures_unresponsive_view").innerHTML = additional_pictures_unresponsive_view();
                    document.getElementById("additional_pictures_responsive_view").innerHTML = additional_pictures_responsive_view();
                }
            }
        }
    };
    
    xhttp.open("POST", third_party_domain + "/product-details-interface");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("item_id=" + product);
}

function html_body_pc_format(product_details) {
    
    var output = "";
    
    output += "<div class=\"body_pc_format\"><br />";
    output += "<div style=\"text-align: right; width: 100%\">";
    output += "<label><a href=\"" + domain + "/cart" + forward_slash_or_html_extension + "\">" +
            "<span class=\"number_of_items_in_your_cart\"></span></a></label>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%\">";
    
    if (product_details["sale_product_details"].length == 1) {
       
       output += "<div style=\"text-align: left; width: 100%; display: table\">";
       output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
       output += "<div style=\"text-align: left; width: 50%; height: 100%; display: table-cell; vertical-align: top\">";
       output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
       output += "<div style=\"text-align: left; width: 100%; max-height: 400px; overflow: hidden\">";
       
        if (product_details["sale_product_details"][0]["thumbnail"] != "" && product_details["sale_product_details"][0]["thumbnail"] != null) {
            
            if (product_details["sale_product_details"][0]["thumbnail"].replace(/\s/g, "").length > 0) {
                
                output += "<img class=\"pc_format_for_sale_image\" src=\"" + product_details["sale_product_details"][0]["thumbnail"] + "\" />";
            } else {
                
                output += "<img class=\"pc_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
        } else {
            
            output += "<img class=\"pc_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
        }
       
       output += "</div>";      
       output += "</div>";
       output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
       output += "<div style=\"text-align: left; width: 100%\">";
       
        if (product_details["sale_product_additional_pictures"][0]["thumbnail"] != "no picture" && product_details["sale_product_additional_pictures"][0]["thumbnail"] != "fail") {
            
            //Execute this function twice so that all pictures can be cycled through.
            //Executing this function twice will create an array of two sets of pictures.
            //Two sets are necessary so that the cycling can occur.
            arrange_additional_pictures(product_details);
            arrange_additional_pictures(product_details);
            
            output += "<div id=\"additional_pictures_unresponsive_view\" style=\"text-align: left; width: 100%\"></div>";
            
            if (product_details["sale_product_additional_pictures"].length > 3) {
                
                output += "<div style=\"text-align: left; width: 100%\">";
                output += "<div style=\"text-align: left; width: 100%; display: table\">";
                output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
                output += "<div style=\"padding-top: 15px; text-align: center; width: 50%; display: table-cell\">";
                output += "<label><a onclick=\"scroll_backward()\" href=\"#\"><span>&lt; Previous</span></a></label>";
                output += "</div>";
                output += "<div style=\"padding-top: 15px; text-align: center; width: 50%; display: table-cell\">";
                output += "<label><a onclick=\"scroll_forward()\" href=\"#\"><span>Next &gt;</span></a></label>";
                output += "</div>";
                output += "</div>";
                output += "</div>";
                output += "</div>";
            }
        } else {
            
            output += "<label>There are no additional photos for this item.</label>";
        }
       
       output += "</div>";      
       output += "</div>";
       output += "</div>";
       output += "<div style=\"text-align: left; width: 50%; height: 100%; display: table-cell; vertical-align: top\">";
       output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
       output += "<div style=\"text-align: left; width: 100%\">";
       output += "<h1>" + product_details["sale_product_details"][0]["item"] + "</h1>";
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%\">";
       output += "<span class=\"price\">$" + product_details["sale_product_details"][0]["price"] + "</span>";
       output += "</div>";
       
        if (product_details["sale_product_details"][0]["inventory"] < 1) {
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
            output += "<label>This item is out of stock.</label>";
            output += "</div>";
        } else {
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
            output += "<label>Items available (" + product_details["sale_product_details"][0]["inventory"] + ")</label>";
            output += "</div>";
        }
        
       output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
       output += "<label>Quantity:</label>&nbsp;&nbsp;";
       
        if (product_details["sale_product_details"][0]["inventory"] < 1) {
            
            output += "<select disabled=\"disabled\" class=\"item_quantity\">";
            output += "<option value=\"1\">1</option>";
            output += "<option value=\"2\">2</option>";
            output += "<option value=\"3\">3</option>";
            output += "<option value=\"4\">4</option>";
            output += "<option value=\"5\">5</option>";
            output += "</select><br />";
            output += "<input type=\"button\" disabled=\"disabled\" class=\"place_in_cart\" onclick=\"add_to_cart('0')\" value=\"Add to cart\" />";
        } else {
            
            output += "<select class=\"item_quantity\">";
            output += "<option value=\"1\">1</option>";
            output += "<option value=\"2\">2</option>";
            output += "<option value=\"3\">3</option>";
            output += "<option value=\"4\">4</option>";
            output += "<option value=\"5\">5</option>";
            output += "</select><br />";
            output += "<input type=\"button\" class=\"place_in_cart\" onclick=\"add_to_cart('0')\" value=\"Add to cart\" />";
        }
       
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
       output += "<label><b>Product description:</b></label><br /><span class=\"description\">";
       
        if (product_details["sale_product_details"][0]["description"] != "" && product_details["sale_product_details"][0]["description"] != null) {
            
            if (product_details["sale_product_details"][0]["description"].replace(/\s/g, "").length > 0) {
                
                output += product_details["sale_product_details"][0]["description"];
            } else {
                
                output += "There is no product description.";
            }
        } else {
            
            output += "There is no product description.";
        }
        
       output += "</span>";
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
       
       if (product_details["sale_product_reviews"][0]["subject"] != "no review"
               && product_details["sale_product_reviews"][0]["subject"] != "fail") {
           
           output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
           output += "<p>Leave a review for this product.</p>";
           output += "</div>";
           output += "<div style=\"text-align: left; width: 100%\">";
           output += "<input type=\"button\" class=\"open_create_review_form\" onclick=\"show_create_review_form(" +
                   ")\" value=\"Start\" />";
           output += "</div>";
           
           //Show newest two reviews.
           if (product_details["sale_product_reviews"].length <= 2) {
               
                for (var i = 0; i < product_details["sale_product_reviews"].length; i++) {
                    
                    output += each_unresponsive_review(product_details["sale_product_reviews"][i]["row_id"],
                    product_details["sale_product_reviews"][i]["item_id"],
                    product_details["sale_product_reviews"][i]["rating"],
                    product_details["sale_product_reviews"][i]["subject"],
                    product_details["sale_product_reviews"][i]["description"],
                    product_details["sale_product_reviews"][i]["name"],
                    product_details["sale_product_reviews"][i]["date_received"]);
                } 
           } else {
               
                for (var i = 0; i < 2; i++) {
                    
                    output += each_unresponsive_review(product_details["sale_product_reviews"][i]["row_id"],
                    product_details["sale_product_reviews"][i]["item_id"],
                    product_details["sale_product_reviews"][i]["rating"],
                    product_details["sale_product_reviews"][i]["subject"],
                    product_details["sale_product_reviews"][i]["description"],
                    product_details["sale_product_reviews"][i]["name"],
                    product_details["sale_product_reviews"][i]["date_received"]);
                }               
           }
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
            output += "<p>See more <a href=\"" + domain + "/product-reviews" + forward_slash_or_html_extension + "?product=" +
            product_details["sale_product_details"][0]["row_id"] + "\">reviews</a> on this product.</p>";
            output += "</div>";
       } else {
           
           output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
           output += "<p>Be the first to review this product.</p>";
           output += "</div>";
           output += "<div style=\"text-align: left; width: 100%\">";
           output += "<input type=\"button\" class=\"open_create_review_form\" onclick=\"show_create_review_form(" +
                   ")\" value=\"Start\" />";
           output += "</div>";
       }
       
       output += "</div>";
       output += "</div>";       
       output += "</div>";       
       output += "</div>";
       output += "</div>";
    } else {
        
        html_code += item_not_found_message();
    }
    
    output += "</div>";
    output += "</div>";
    
    return output;
}

function html_body_mobile_format(product_details) {
    
    var output = "";
    
    output += "<div class=\"body_mobile_format\"><br />";
    output += "<div style=\"text-align: right; width: 100%\">";
    output += "<label><a href=\"" + domain + "/cart" + forward_slash_or_html_extension + "\">" +
            "<span class=\"number_of_items_in_your_cart\"></span></a></label>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%\">";
    
    if (product_details["sale_product_details"].length == 1) {
       
       output += "<div style=\"text-align: left; width: 100%; display: table\">";
       output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
       output += "<div style=\"text-align: left; width: 100%; height: 100%; display: table-cell; vertical-align: top\">";
       output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
       output += "<div style=\"text-align: left; width: 100%; overflow: hidden; max-height: 400px\">";
       
        if (product_details["sale_product_details"][0]["thumbnail"] != "" && product_details["sale_product_details"][0]["thumbnail"] != null) {
            
            if (product_details["sale_product_details"][0]["thumbnail"].replace(/\s/g, "").length > 0) {
                
                output += "<img class=\"mobile_format_for_sale_image\" src=\"" + product_details["sale_product_details"][0]["thumbnail"] + "\" />";
            } else {
                
                output += "<img class=\"mobile_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
        } else {
            
            output += "<img class=\"mobile_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
        }
       
       output += "</div>";      
       output += "</div>";
       output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
       output += "<div style=\"text-align: left; width: 100%\">";
       
        if (product_details["sale_product_additional_pictures"][0]["thumbnail"] != "no picture" && product_details["sale_product_additional_pictures"][0]["thumbnail"] != "fail") {
            
            output += "<div id=\"additional_pictures_responsive_view\" style=\"text-align: left; width: 100%\"></div>";
            
            if (product_details["sale_product_additional_pictures"].length > 1) {
                
                output += "<div style=\"text-align: left; width: 100%\">";
                output += "<div style=\"text-align: left; width: 100%; display: table\">";
                output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
                output += "<div style=\"padding-top: 15px; text-align: center; width: 50%; display: table-cell\">";
                output += "<label><a onclick=\"scroll_backward()\" href=\"#\"><span>&lt; Previous</span></a></label>";
                output += "</div>";
                output += "<div style=\"padding-top: 15px; text-align: center; width: 50%; display: table-cell\">";
                output += "<label><a onclick=\"scroll_forward()\" href=\"#\"><span>Next &gt;</span></a></label>";
                output += "</div>";
                output += "</div>";
                output += "</div>";
                output += "</div>";
            }
        } else {
            
            output += "<label>There are no additional photos for this item.</label>";
        }
       
       output += "</div>";      
       output += "</div>";
       output += "</div>";
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
       output += "<div style=\"text-align: left; width: 100%; height: 100%; display: table-cell; vertical-align: top\">";
       output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
       output += "<div style=\"text-align: left; width: 100%\">";
       output += "<h1>" + product_details["sale_product_details"][0]["item"] + "</h1>";
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%\">";
       output += "<span class=\"price\">$" + product_details["sale_product_details"][0]["price"] + "</span>";
       output += "</div>";
       
        if (product_details["sale_product_details"][0]["inventory"] < 1) {
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
            output += "<label>This item is out of stock.</label>";
            output += "</div>";
        } else {
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
            output += "<label>Items available (" + product_details["sale_product_details"][0]["inventory"] + ")</label>";
            output += "</div>";
        }
        
       output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
       output += "<label>Quantity:</label>&nbsp;&nbsp;";
       
        if (product_details["sale_product_details"][0]["inventory"] < 1) {
            
            output += "<select disabled=\"disabled\" class=\"item_quantity\">";
            output += "<option value=\"1\">1</option>";
            output += "<option value=\"2\">2</option>";
            output += "<option value=\"3\">3</option>";
            output += "<option value=\"4\">4</option>";
            output += "<option value=\"5\">5</option>";
            output += "</select><br />";
            output += "<input type=\"button\" disabled=\"disabled\" class=\"place_in_cart\" onclick=\"add_to_cart('0')\" value=\"Add to cart\" />";
        } else {
            
            output += "<select class=\"item_quantity\">";
            output += "<option value=\"1\">1</option>";
            output += "<option value=\"2\">2</option>";
            output += "<option value=\"3\">3</option>";
            output += "<option value=\"4\">4</option>";
            output += "<option value=\"5\">5</option>";
            output += "</select><br />";
            output += "<input type=\"button\" class=\"place_in_cart\" onclick=\"add_to_cart('0')\" value=\"Add to cart\" />";
        }
       
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
       output += "<label><b>Product description:</b></label><br /><span class=\"description\">";
       
        if (product_details["sale_product_details"][0]["description"] != "" && product_details["sale_product_details"][0]["description"] != null) {
            
            if (product_details["sale_product_details"][0]["description"].replace(/\s/g, "").length > 0) {
                
                output += product_details["sale_product_details"][0]["description"];
            } else {
                
                output += "There is no product description.";
            }
        } else {
            
            output += "There is no product description.";
        }
        
       output += "</span>";
       output += "</div>";
       output += "<div style=\"text-align: left; width: 100%; padding-top: 20px\">";
       
       if (product_details["sale_product_reviews"][0]["subject"] != "no review"
               && product_details["sale_product_reviews"][0]["subject"] != "fail") {
           
           output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
           output += "<p>Leave a review for this product.</p>";
           output += "</div>";
           output += "<div style=\"text-align: left; width: 100%\">";
           output += "<input type=\"button\" class=\"open_create_review_form\" onclick=\"show_create_review_form(" +
                   ")\" value=\"Start\" />";
           output += "</div>";
           
           //Show newest two reviews.
           if (product_details["sale_product_reviews"].length <= 2) {
               
                for (var i = 0; i < product_details["sale_product_reviews"].length; i++) {
                    
                    output += each_responsive_review(product_details["sale_product_reviews"][i]["row_id"],
                    product_details["sale_product_reviews"][i]["item_id"],
                    product_details["sale_product_reviews"][i]["rating"],
                    product_details["sale_product_reviews"][i]["subject"],
                    product_details["sale_product_reviews"][i]["description"],
                    product_details["sale_product_reviews"][i]["name"],
                    product_details["sale_product_reviews"][i]["date_received"]);
                } 
           } else {
               
                for (var i = 0; i < 2; i++) {
                    
                    output += each_responsive_review(product_details["sale_product_reviews"][i]["row_id"],
                    product_details["sale_product_reviews"][i]["item_id"],
                    product_details["sale_product_reviews"][i]["rating"],
                    product_details["sale_product_reviews"][i]["subject"],
                    product_details["sale_product_reviews"][i]["description"],
                    product_details["sale_product_reviews"][i]["name"],
                    product_details["sale_product_reviews"][i]["date_received"]);
                }               
           }
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
            output += "<p>See more <a href=\"" + domain + "/product-reviews" + forward_slash_or_html_extension + "?product=" +
            product_details["sale_product_details"][0]["row_id"] + "\">reviews</a> on this product.</p>";
            output += "</div>";
       } else {
           
           output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
           output += "<p>Be the first to review this product.</p>";
           output += "</div>";
           output += "<div style=\"text-align: left; width: 100%\">";
           output += "<input type=\"button\" class=\"open_create_review_form\" onclick=\"show_create_review_form(" +
                   ")\" value=\"Start\" />";
           output += "</div>";
       }
       
       output += "</div>";
       output += "</div>";       
       output += "</div>";       
       output += "</div>";
       output += "</div>";
    } else {
        
        html_code += item_not_found_message();
    }
    
    output += "</div>";
    output += "</div>";
    
    return output;
}

function scroll_backward() {
    
    if (additional_picture_position_unresponsive > 0) {
        
        additional_picture_position_unresponsive -= 1;
    } else {
        
        additional_picture_position_unresponsive = product_details["sale_product_additional_pictures"].length;
    }
    
    if (additional_picture_position_responsive > 0) {
        
        additional_picture_position_responsive -= 1;
    } else {
        
        additional_picture_position_responsive = product_details["sale_product_additional_pictures"].length;
    }
    
    if (product_details["sale_product_details"].length == 1) {
        
        if (product_details["sale_product_additional_pictures"][0]["thumbnail"] != "no picture" && product_details["sale_product_additional_pictures"][0]["thumbnail"] != "fail") {
            
            document.getElementById("additional_pictures_unresponsive_view").innerHTML = additional_pictures_unresponsive_view();
            document.getElementById("additional_pictures_responsive_view").innerHTML = additional_pictures_responsive_view();
        }
    }
}

function scroll_forward() {
    
    if (additional_picture_position_unresponsive < product_details["sale_product_additional_pictures"].length) {
        
        additional_picture_position_unresponsive += 1;
    } else {
        
        additional_picture_position_unresponsive = 0;
    }
    
    if (additional_picture_position_responsive < product_details["sale_product_additional_pictures"].length) {
        
        additional_picture_position_responsive += 1;
    } else {
        
        additional_picture_position_responsive = 0;
    }
    
    if (product_details["sale_product_details"].length == 1) {
        
        if (product_details["sale_product_additional_pictures"][0]["thumbnail"] != "no picture" && product_details["sale_product_additional_pictures"][0]["thumbnail"] != "fail") {
            
            document.getElementById("additional_pictures_unresponsive_view").innerHTML = additional_pictures_unresponsive_view();
            document.getElementById("additional_pictures_responsive_view").innerHTML = additional_pictures_responsive_view();
        }
    }
}

function arrange_additional_pictures(product_details) {
    
    //Show the original product picture.
    if (product_details["sale_product_details"][0]["thumbnail"] != "" && product_details["sale_product_details"][0]["thumbnail"] != null) {
        
        if (product_details["sale_product_details"][0]["thumbnail"].replace(/\s/g, "").length > 0) {
            
            show_additional_pictures.push(product_details["sale_product_details"][0]["thumbnail"]);
        } else {
            
            show_additional_pictures.push(domain + "/images/no-file.png");
        }
    } else {
        
        show_additional_pictures.push(domain + "/images/no-file.png");
    }
    
    //Show additional pictures.
    for (var i = 0; i < product_details["sale_product_additional_pictures"].length; i++) {
        
        show_additional_pictures.push(product_details["sale_product_additional_pictures"][i]["thumbnail"]);
    }    
}

function additional_pictures_unresponsive_view() {
    
    var output = "";
    
    output += "<div style=\"text-align: left; width: 100%\">";
    output += "<div style=\"text-align: left; width: 100%; display: table\">";
    output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
    
    if ((show_additional_pictures.length / 2) < 4) {
        
        for (var i = 0; i < (show_additional_pictures.length / 2); i++) {
            
            output += "<div style=\"text-align: left; width: 25%; vertical-align: top; display: table-cell\">";
            output += "<div class=\"select_additional_picture\" onclick=\"show_additional_picture('" +
                    show_additional_pictures[i + additional_picture_position_unresponsive] +
                    "')\" style=\"overflow: hidden; text-align: center; width: 96%; margin-left: 2%; margin-right: 2%; height: 100px\">";
            output += "<img style=\"width: 100%\" class=\"pc_format_for_sale_additional_image_" + (i + additional_picture_position_unresponsive) + "\" src=\"" + show_additional_pictures[i + additional_picture_position_unresponsive] + "\" />";
            output += "</div>";
            output += "</div>";
        }
        
        for (var i = 0; i < (3 - product_details["sale_product_additional_pictures"].length); i++) {
            
            output += "<div style=\"text-align: left; width: 25%; display: table-cell\">";
            output += "</div>";
        }
    } else {
        
        for (var i = 0; i < 4; i++) {
            
            output += "<div style=\"text-align: left; width: 25%; vertical-align: top; display: table-cell\">";
            output += "<div class=\"select_additional_picture\" onclick=\"show_additional_picture('" +
                    show_additional_pictures[i + additional_picture_position_unresponsive] +
                    "')\" style=\"overflow: hidden; text-align: center; width: 96%; margin-left: 2%; margin-right: 2%; height: 100px\">";
            output += "<img style=\"width: 100%\" class=\"pc_format_for_sale_additional_image_" + (i + additional_picture_position_unresponsive) + "\" src=\"" + show_additional_pictures[i + additional_picture_position_unresponsive] + "\" />";
            output += "</div>";
            output += "</div>";
        }
    }
    
    output += "</div>";
    output += "</div>";
    output += "</div>";
    
    return output;
}

function additional_pictures_responsive_view() {
    
    var output = "";
    
    output += "<div style=\"text-align: left; width: 100%\">";
    output += "<div style=\"text-align: left; width: 100%; display: table\">";
    output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
    
    if ((show_additional_pictures.length / 2) < 2) {
        
        for (var i = 0; i < (show_additional_pictures.length / 2); i++) {
        
            output += "<div style=\"text-align: left; width: 50%; vertical-align: top; display: table-cell\">";
            output += "<div class=\"select_additional_picture\" onclick=\"show_additional_picture('" +
                show_additional_pictures[i + additional_picture_position_responsive] +
                "')\" style=\"overflow: hidden; text-align: center; width: 100%; margin-left: 0%; margin-right: 0%; height: 100px\">";
            output += "<img style=\"width: 100%\" class=\"mobile_format_for_sale_additional_image_" + (i + additional_picture_position_responsive) + "\" src=\"" + show_additional_pictures[i + additional_picture_position_responsive] + "\" />";
            output += "</div>";
            output += "</div>";
        }
        
        for (var i = 0; i < (1 - product_details["sale_product_additional_pictures"].length); i++) {
            
            output += "<div style=\"text-align: left; width: 50%; display: table-cell\">";
            output += "</div>";
        }
    } else {
        
        for (var i = 0; i < 2; i++) {
            
            output += "<div style=\"text-align: left; width: 50%; vertical-align: top; display: table-cell\">";
            output += "<div class=\"select_additional_picture\" onclick=\"show_additional_picture('" +
                    show_additional_pictures[i + additional_picture_position_responsive] +
                    "')\" style=\"overflow: hidden; text-align: center; width: 100%; margin-left: 0%; margin-right: 0%; height: 100px\">";
            output += "<img style=\"width: 100%\" class=\"mobile_format_for_sale_additional_image_" + (i + additional_picture_position_responsive) + "\" src=\"" + show_additional_pictures[i + additional_picture_position_responsive] + "\" />";
            output += "</div>";
            output += "</div>";
        }
    }
    
    output += "</div>";
    output += "</div>";
    output += "</div>";
    
    return output;
}

function show_additional_picture(picture_src) {
    
    $(".pc_format_for_sale_image").attr("src", picture_src);
    $(".mobile_format_for_sale_image").attr("src", picture_src);
}

function each_unresponsive_review(review_number, item_number, rating, subject, description, name, date_received) {
    
    var output = "";
    
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";

    for (var i = 0; i < rating; i++) {
        
        output += "<span class=\"star\">&starf;</span> ";
    }
    
    output += "</div>";
    
    if (subject.length > 100) {
        
        output += "<div class=\"less_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; word-wrap: break-word\">";
        output += "<label><b>" + subject.substr(0, 100) + "</b>...</label>";
        output += "</div>";
        output += "<div class=\"less_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_more_review_subject(" + review_number +
                ") \"href=\"#\">Show more +</a></label>";
        output += "</div>";
        output += "<div class=\"more_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; display: none; word-wrap: break-word\">";
        output += "<label><b>" + subject + "</b></label>";
        output += "</div>";
        output += "<div class=\"more_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_less_review_subject(" + review_number +
                ") \"href=\"#\">Show less -</a></label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 10px; word-wrap: break-word\">";
        output += "<label><b>" + subject + "</b></label>";
        output += "</div>";
    }
    
    if (description.length > 100) {
        
        output += "<div class=\"less_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label>" + description.substr(0, 100) + "...</label>";
        output += "</div>";
        output += "<div class=\"less_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_more_review_description(" + review_number +
                ") \"href=\"#\">Show more +</a></label>";
        output += "</div>";
        output += "<div class=\"more_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label>" + description + "</label>";
        output += "</div>";
        output += "<div class=\"more_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_less_review_description(" + review_number +
                ") \"href=\"#\">Show less -</a></label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label>" + description + "</label>";
        output += "</div>";
    }
    
    if (name.length > 100) {
        
        output += "<div class=\"less_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; word-wrap: break-word\">";
        output += "<label><b>Posted by</b> " + name.substr(0, 100) + "...</label>";
        output += "</div>";
        output += "<div class=\"less_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_more_review_name(" + review_number +
                ") \"href=\"#\">Show more +</a></label>";
        output += "</div>";
        output += "<div class=\"more_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; display: none; word-wrap: break-word\">";
        output += "<label><b>Posted by</b> " + name + "</label>";
        output += "</div>";
        output += "<div class=\"more_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_less_review_name(" + review_number +
                ") \"href=\"#\">Show less -</a></label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
        output += "<label><b>Posted by</b> " + name + "</label>";
        output += "</div>";
    }
    
    output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
    output += "<label><b>Date received:</b> " + date_received + "</label>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
    output += "<label><a href=\"#\" onclick=\"send_review_change_email_request('" + review_number +
            "', '" + item_number + "', 'Edit')\">Edit</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"send_review_deletion_email_request('" + review_number +
            "', '" + item_number + "', 'Delete')\">Delete</a></label>";
    output += "</div>";
    
    return output;
}

function each_responsive_review(review_number, item_number, rating, subject, description, name, date_received) {
    
    var output = "";
    
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";

    for (var i = 0; i < rating; i++) {
        
        output += "<span class=\"star\">&starf;</span> ";
    }
    
    output += "</div>";
    
    if (subject.length > 100) {
        
        output += "<div class=\"less_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; word-wrap: break-word\">";
        output += "<label><b>" + subject.substr(0, 100) + "</b>...</label>";
        output += "</div>";
        output += "<div class=\"less_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_more_review_subject(" + review_number +
                ") \"href=\"#\">Show more +</a></label>";
        output += "</div>";
        output += "<div class=\"more_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; display: none; word-wrap: break-word\">";
        output += "<label><b>" + subject + "</b></label>";
        output += "</div>";
        output += "<div class=\"more_subject_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_less_review_subject(" + review_number +
                ") \"href=\"#\">Show less -</a></label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 10px; word-wrap: break-word\">";
        output += "<label><b>" + subject + "</b></label>";
        output += "</div>";
    }
    
    if (description.length > 100) {
        
        output += "<div class=\"less_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label>" + description.substr(0, 100) + "...</label>";
        output += "</div>";
        output += "<div class=\"less_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_more_review_description(" + review_number +
                ") \"href=\"#\">Show more +</a></label>";
        output += "</div>";
        output += "<div class=\"more_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label>" + description + "</label>";
        output += "</div>";
        output += "<div class=\"more_description_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_less_review_description(" + review_number +
                ") \"href=\"#\">Show less -</a></label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label>" + description + "</label>";
        output += "</div>";
    }
    
    if (name.length > 100) {
        
        output += "<div class=\"less_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; word-wrap: break-word\">";
        output += "<label><b>Posted by</b> " + name.substr(0, 100) + "...</label>";
        output += "</div>";
        output += "<div class=\"less_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_more_review_name(" + review_number +
                ") \"href=\"#\">Show more +</a></label>";
        output += "</div>";
        output += "<div class=\"more_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 10px; display: none; word-wrap: break-word\">";
        output += "<label><b>Posted by</b> " + name + "</label>";
        output += "</div>";
        output += "<div class=\"more_name_" + review_number + "\" style=\"text-align: left; width: 100%; padding-top: 5px; display: none; word-wrap: break-word\">";
        output += "<label><a onclick=\"show_less_review_name(" + review_number +
                ") \"href=\"#\">Show less -</a></label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
        output += "<label><b>Posted by</b> " + name + "</label>";
        output += "</div>";
    }
    
    output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
    output += "<label><b>Date received:</b> " + date_received + "</label>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 10px\">";
    output += "<label><a href=\"#\" onclick=\"send_review_change_email_request('" + review_number +
            "', '" + item_number + "', 'Edit')\">Edit</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"send_review_deletion_email_request('" + review_number +
            "', '" + item_number + "', 'Delete')\">Delete</a></label>";
    output += "</div>";
    
    return output;
}

function create_new_review() {
    
    var create_new_review_message = "";
    var create_new_review_feedback;
    var product = "";
    var name = "";
    var email = "";
    var rating = "";
    var subject = "";
    var description = "";
    
    product = $.url_params("product");
    name = document.getElementsByClassName("review_name")[0].value;
    email = document.getElementsByClassName("review_email")[0].value;
    rating = document.getElementsByClassName("review_rating")[0].value;
    subject = document.getElementsByClassName("review_subject")[0].value;
    description = document.getElementsByClassName("review_description")[0].value;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            create_new_review_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            create_new_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            create_new_review_message += "<label><span class=\"close_create_review_form\" onclick=\"hide_create_review_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            create_new_review_message += "</div>";
            create_new_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".create_review_form").html(create_new_review_message);
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            create_new_review_feedback = JSON.parse(this.responseText);
            
            document.getElementsByClassName("review_name")[0].style.backgroundColor = "white";
            document.getElementsByClassName("review_email")[0].style.backgroundColor = "white";
            document.getElementsByClassName("review_rating")[0].style.backgroundColor = "white";
            document.getElementsByClassName("review_subject")[0].style.backgroundColor = "white";
            document.getElementsByClassName("review_description")[0].style.backgroundColor = "white";
            
            //Check if the user incorrectly submits the form.
            if (create_new_review_feedback[0]["form_message"] == "error creating review") {
                
                for (var i = 1; i < create_new_review_feedback.length; i++) {
                    
                    if (create_new_review_feedback[i]["form_message"] != "") {
                        
                        create_new_review_message += "<div><label><span style=\"color: red\">" + create_new_review_feedback[i]["form_message"] +
                                "</span></label></div>";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Name is required.") {
                        
                        document.getElementsByClassName("review_name")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Email is required.") {
                        
                        document.getElementsByClassName("review_email")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Rating is required.") {
                        
                        document.getElementsByClassName("review_rating")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Subject requires at least one non-space character.  Otherwise, leave this field blank.") {
                        
                        document.getElementsByClassName("review_subject")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Description requires at least one non-space character.  Otherwise, leave this field blank.") {
                        
                        document.getElementsByClassName("review_description")[0].style.backgroundColor = "pink";
                    }
                }
                
                $(".create_review_form_feedback").html(create_new_review_message);
            } else if (create_new_review_feedback[0]["form_message"] == "success creating review") {
                
                create_new_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                create_new_review_message += "<label><span class=\"close_create_review_form\" onclick=\"hide_create_review_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                create_new_review_message += "</div>";
                create_new_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>Thank you for your review.  " +
                        "An email has been sent to you.</label></div>";
                
                $(".create_review_form").html(create_new_review_message);
            } else {
                
                create_new_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                create_new_review_message += "<label><span class=\"close_create_review_form\" onclick=\"hide_create_review_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                create_new_review_message += "</div>";
                create_new_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".create_review_form").html(create_new_review_message);
            }
        }
    };
    
    xhttp.open("POST", third_party_domain + "/create-review");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("item_id=" + product + "&name=" + name + "&email=" + email + "&rating=" + rating + "&subject=" + subject + "&description=" + description + "&create_review=Submit");
}

function send_review_change_email_request(review_number, item_number, edit_review) {
    
    var send_review_change_email_request_message = "";
    var send_review_change_email_request_feedback;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            send_review_change_email_request_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            send_review_change_email_request_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            send_review_change_email_request_message += "<label><span class=\"close_send_review_change_email_request_form\" onclick=\"hide_send_review_change_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            send_review_change_email_request_message += "</div>";
            send_review_change_email_request_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".send_review_change_email_request_form").html(send_review_change_email_request_message);
            
            show_send_review_change_email_request_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            send_review_change_email_request_feedback = JSON.parse(this.responseText);
            
            if (send_review_change_email_request_feedback[0]["form_message"] == "success - send email change review request") {
                
                send_review_change_email_request_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                send_review_change_email_request_message += "<label><span class=\"close_send_review_change_email_request_form\" onclick=\"hide_send_review_change_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                send_review_change_email_request_message += "</div>";
                send_review_change_email_request_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>You requested to edit this review.  Check your email for instructions.</label></div>";
            } else {
                
                send_review_change_email_request_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                send_review_change_email_request_message += "<label><span class=\"close_send_review_change_email_request_form\" onclick=\"hide_send_review_change_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                send_review_change_email_request_message += "</div>";
                send_review_change_email_request_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            }
            
            $(".send_review_change_email_request_form").html(send_review_change_email_request_message);
            
            show_send_review_change_email_request_form();
        }
    };
    
    xhttp.open("POST", third_party_domain + "/send-review-change-email-request");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("row_id=" + review_number + "&item_id=" + item_number + "&send_review_change_email_request=" + edit_review);
}

function send_review_deletion_email_request(review_number, item_number, delete_review) {
    
    var send_review_deletion_email_request_message = "";
    var send_review_deletion_email_request_feedback;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            send_review_deletion_email_request_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            send_review_deletion_email_request_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            send_review_deletion_email_request_message += "<label><span class=\"close_send_review_deletion_email_request_form\" onclick=\"hide_send_review_deletion_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            send_review_deletion_email_request_message += "</div>";
            send_review_deletion_email_request_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".send_review_deletion_email_request_form").html(send_review_deletion_email_request_message);
            
            show_send_review_deletion_email_request_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            send_review_deletion_email_request_feedback = JSON.parse(this.responseText);
            
            if (send_review_deletion_email_request_feedback[0]["form_message"] == "success - send email delete review request") {
                
                send_review_deletion_email_request_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                send_review_deletion_email_request_message += "<label><span class=\"close_send_review_deletion_email_request_form\" onclick=\"hide_send_review_deletion_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                send_review_deletion_email_request_message += "</div>";
                send_review_deletion_email_request_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>You requested to delete this review.  Check your email for instructions.</label></div>";
            } else {
                
                send_review_deletion_email_request_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                send_review_deletion_email_request_message += "<label><span class=\"close_send_review_deletion_email_request_form\" onclick=\"hide_send_review_deletion_email_request_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                send_review_deletion_email_request_message += "</div>";
                send_review_deletion_email_request_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            }
            
            $(".send_review_deletion_email_request_form").html(send_review_deletion_email_request_message);
            
            show_send_review_deletion_email_request_form();
        }
    };
    
    xhttp.open("POST", third_party_domain + "/send-review-deletion-email-request");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("row_id=" + review_number + "&item_id=" + item_number + "&send_review_deletion_email_request=" + delete_review);
}

function use_guest_session_search_shopping_cart() {
    
    var guest_session_message = "";
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            guest_session_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            guest_session_message = "";
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            guest_session_message = this.responseText;
        }
        
        if (guest_session_message != "") {
            
            //Get shopping cart information.
            search_shopping_cart(guest_session_message);
        } 
    };
    
    xhttp.open("POST", domain + "/third-party/session-controls/shopping-cart-guest-session/");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("check_session=Check");
}

function search_shopping_cart(guest_session) {
    
    var search_shopping_cart_message = "";
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            search_shopping_cart_message = "fail";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            search_shopping_cart_message = "fail";
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            search_shopping_cart_message = this.responseText;
        }
        
        if (search_shopping_cart_message != "fail") {
            
            shopping_cart_items = JSON.parse(search_shopping_cart_message);
            
            $(".number_of_items_in_your_cart").html("Your cart (" +
                    shopping_cart_items["shopping_cart_items_count"] + ")");
        } else {
            
            $(".number_of_items_in_your_cart").html("Your cart (0)");
        }
    };
    
    xhttp.open("POST", third_party_domain + "/search-shopping-cart");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("raw_time_received=" + get_raw_minute() + "&guest_session=" + guest_session);
}

function show_more_review_subject(item_id) {
    
    $(".less_subject_" + item_id).fadeOut();
    $(".more_subject_" + item_id).slideDown();
}

function show_less_review_subject(item_id) {
    
    $(".more_subject_" + item_id).fadeOut();
    $(".less_subject_" + item_id).slideDown();    
}

function show_more_review_description(item_id) {
    
    $(".less_description_" + item_id).fadeOut();
    $(".more_description_" + item_id).slideDown();
}

function show_less_review_description(item_id) {
    
    $(".more_description_" + item_id).fadeOut();
    $(".less_description_" + item_id).slideDown();    
}

function show_more_review_name(item_id) {
    
    $(".less_name_" + item_id).fadeOut();
    $(".more_name_" + item_id).slideDown();
}

function show_less_review_name(item_id) {
    
    $(".more_name_" + item_id).fadeOut();
    $(".less_name_" + item_id).slideDown();    
}

//A guest session needs to be active, first.  If there is not a session,
//one will be created.
function add_to_cart(index) {
    
    var item_id = "";
    var item_name = "";
    var thumbnail = "";
    var category = "";
    var description = "";
    var price = "";
    var guest_session_message = "";
    
    item_id = item_to_be_added["sale_product_details"][index]["row_id"];
    item_name = item_to_be_added["sale_product_details"][index]["item"].replace(/&apos;/g,'\'');
    thumbnail = item_to_be_added["sale_product_details"][index]["thumbnail"];
    category = item_to_be_added["sale_product_details"][index]["item_category"];
    description = item_to_be_added["sale_product_details"][index]["description"].replace(/&apos;/g,'\'');
    price = item_to_be_added["sale_product_details"][index]["price"];
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            guest_session_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            guest_session_message = "";
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            guest_session_message = this.responseText;
        }
        
        if (guest_session_message != "") {
            
            //Get shopping cart information.
            place_in_cart(item_id, item_name, thumbnail, category, description, price, guest_session_message);
        } 
    };
    
    xhttp.open("POST", domain + "/third-party/session-controls/shopping-cart-guest-session/");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("check_session=Check");
}

//This function calls the server, so that the item can be added to the shopping cart.
function place_in_cart(item_id, item_name, thumbnail, category, description, price, guest_session) {
    
    var add_to_cart_message = "";
    var add_to_cart_feedback = "";
    var quantity = 0;
    var raw_time_received = 0;
    
    quantity = $(".item_quantity").val();
    raw_time_received = get_raw_minute();
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            add_to_cart_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            add_to_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            add_to_cart_message += "<label><span class=\"close_add_to_cart_form\" onclick=\"hide_add_to_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            add_to_cart_message += "</div>";
            add_to_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".add_to_cart_form").html(add_to_cart_message);
            
            show_add_to_cart_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            add_to_cart_feedback = this.responseText;
            
            if (add_to_cart_feedback.indexOf("problem adding item") >= 0) {
                
                add_to_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                add_to_cart_message += "<label><span class=\"close_add_to_cart_form\" onclick=\"hide_add_to_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                add_to_cart_message += "</div>";
                add_to_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "You are attempting to add more items to your cart than the number of items currently available.  Try again.</label></div>";
                
                $(".add_to_cart_form").html(add_to_cart_message);
            } else if (add_to_cart_feedback.indexOf("item added") >= 0) {
                
                socket.emit("refresh_cart_window", guest_session);
                
                add_to_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                add_to_cart_message += "<label><span class=\"close_add_to_cart_form\" onclick=\"hide_add_to_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                add_to_cart_message += "</div>";
                add_to_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "The item was added to your cart.</label></div>";
                
                $(".add_to_cart_form").html(add_to_cart_message);                
            } else {
                
                add_to_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                add_to_cart_message += "<label><span class=\"close_add_to_cart_form\" onclick=\"hide_add_to_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                add_to_cart_message += "</div>";
                add_to_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".add_to_cart_form").html(add_to_cart_message);                
            }
            
            show_add_to_cart_form();
        } 
    };
    
    xhttp.open("POST", third_party_domain + "/add-to-cart");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("item_id=" + item_id + "&item=" + item_name + "&thumbnail=" + thumbnail +
            "&category=" + category + "&description=" + description +
            "&price=" + price + "&quantity=" + quantity +
            "&raw_time_received=" + raw_time_received +
            "&guest_session=" + guest_session + "&add_to_cart=Add to cart");   
}

function get_raw_minute() {
    
    var today_date = new Date();
    var raw_minute = parseInt((today_date.getTime() / 1000) / 60);
    
    return raw_minute;
}

//Add to cart
function show_add_to_cart_form() {
    
    $(".add_to_cart_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("add_to_cart_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_add_to_cart_form() {
    
    window.location = document.location.href.replace("#", "");
}

//Create review
function show_create_review_form() {
    
    $(".create_review_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("create_review_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_create_review_form() {
    
    window.location = document.location.href.replace("#", "");
}

//Send review change email request
function show_send_review_change_email_request_form() {
    
    $(".send_review_change_email_request_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("send_review_change_email_request_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_send_review_change_email_request_form() {
    
    window.location = document.location.href.replace("#", "");
}

//Send review change deletion request
function show_send_review_deletion_email_request_form() {
    
    $(".send_review_deletion_email_request_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("send_review_deletion_email_request_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_send_review_deletion_email_request_form() {
    
    window.location = document.location.href.replace("#", "");
}