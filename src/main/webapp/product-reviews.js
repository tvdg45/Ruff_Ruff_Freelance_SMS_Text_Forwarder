//Global variables for rendering HTML content
var html_code = "";
var product_reviews;

//Global variables for form input
var form_sort_by = "";
var form_results_per_page = "";
var form_product = "";
var form_security_code = "";
var form_review = "";
var form_make_review_public = "";
var form_enable_review_editing = "";
var form_enable_review_deletion = "";
var domain = location.protocol + "//" + window.location.hostname + "/store";
var third_party_domain = "https://shopping-cart-java.herokuapp.com";
var forward_slash_or_html_extension = "/";

$.url_params = function(name) {
	
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    
    if (results == null){
        
        return null;
    } else {
        
        return decodeURI(results[1]) || 0;
    }
}

//Sort by page options
function sort_by() {
    
    var output = "";
    
    output += "<option value=\"Sort results by\">Sort results by</option>";
    output += "<option value=\"Newest\">Newest rating</option>";
    output += "<option value=\"Oldest\">Oldest rating</option>";
    output += "<option value=\"Highest rating\">Highest rating</option>";
    output += "<option value=\"Lowest rating\">Lowest rating</option>";
    
    return output;
}

//Results per page options
function results_per_page() {
    
    var output = "";
    
    output += "<option value=\"Select results per page\">Select results per page</option>";
    output += "<option value=\"10\">10 results per page</option>";
    output += "<option value=\"20\">20 results per page</option>";
    output += "<option value=\"30\">30 results per page</option>";
    output += "<option value=\"40\">40 results per page</option>";
    output += "<option value=\"50\">50 results per page</option>";
    
    return output;
}

function item_not_found_message() {
    
    return "<p>Sorry, that item was not found.  Try <a href=\"" +
            domain + "/store" + forward_slash_or_html_extension + "\">searching</a> for it.</p>";
}

function html_header_pc_format() {
    
    var output = "";
    
    output += "<div class=\"header_pc_format\">";
    output += "<div style=\"text-align: left; width: 100%\">";
    output += "<div style=\"display: table; text-align: left; width: 100%\">";
    output += "<div style=\"display: table-row; text-align: left; width: 100%\">";
    output += "<div style=\"display: table-cell; text-align: left; width: 10%\">";
    output += "<select class=\"sort_by\" onchange=\"unresponsive_sort_by_search()\">";
    output += sort_by();
    output += "</select>";
    output += "</div>";
    output += "<div style=\"display: table-cell; text-align: left; width: 90%; padding-left: 2%;\">";
    output += "<select class=\"results_per_page\" onchange=\"unresponsive_results_per_page_search()\">";
    output += results_per_page();
    output += "</select>";
    output += "</div>";
    output += "</div>";
    output += "</div>";
    output += "</div>";
    output += "</div>";
    
    return output;
}

function html_header_mobile_format() {
    
    var output = "";
    
    output += "<div class=\"header_mobile_format\">";
    output += "<div style=\"text-align: left; width: 100%\">";
    output += "<select class=\"sort_by\" onchange=\"responsive_sort_by_search()\">";
    output += sort_by();
    output += "</select>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
    output += "<select class=\"results_per_page\" onchange=\"responsive_results_per_page_search()\">";
    output += results_per_page();
    output += "</select>";
    output += "</div>";
    output += "</div>";
    
    return output;
}

window.onload = function() {
    
    var product = "";
    var perpage = "";
    var page = "";
    var sort = "";
    
    product = $.url_params("product");
    perpage = $.url_params("perpage");
    page = $.url_params("pagenum");
    sort = $.url_params("sort");
    
    form_make_review_public = $.url_params("showreview");
    form_enable_review_editing = $.url_params("enablereviewediting");
    form_enable_review_deletion = $.url_params("enablereviewdeletion");
    
    html_code += html_header_pc_format();
    html_code += html_header_mobile_format();
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            document.getElementById("product_reviews").innerHTML = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            document.getElementById("product_reviews").innerHTML = "<p>Sorry, the online store is not available.</p>";
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            product_reviews = JSON.parse(this.responseText);
            
            //Control search results from query.
            if (product_reviews["sale_product_details"][0]["item"] == "no item"
                    || product_reviews["sale_product_details"][0]["item"] == "fail") {
                
                html_code += item_not_found_message();
            } else {
                
                if (product_reviews["sale_product_details"].length == 1) {
                    
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
                    html_code += "<input type=\"text\" class=\"name\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>Email:</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<input type=\"text\" class=\"email\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>How was your experience?</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<select class=\"rating\">";
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
                    html_code += "<input type=\"text\" class=\"subject\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>Describe your experience. (optional)</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<textarea class=\"description\" style=\"text-align: left; width: 100%; height: 200px; min-width: 100%; min-height: 200px; max-width: 100%; max-height: 200px\"></textarea>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px; padding-bottom: 5px\">";
                    html_code += "<input type=\"button\" class=\"submit_new_review\" onclick=\"create_new_review()\" value=\"Submit\" />";
                    html_code += "</div>";
                    html_code += "</div>";
                    html_code += "</div>";
                    
                    //Make review public form
                    html_code += "<div class=\"make_review_public_background\">";
                    html_code += "<div class=\"make_review_public_form\">";
                    html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label><span class=\"close_make_review_public_form\" onclick=\"hide_make_review_public_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
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
                    
                    //Enable review editing form
                    html_code += "<div class=\"enable_review_editing_background\">";
                    html_code += "<div class=\"enable_review_editing_form\">";
                    html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<div class=\"enable_review_editing_form_feedback\" style=\"text-align: left; width: 100%\"></div>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>Name:</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<input type=\"text\" class=\"name\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>How was your experience?</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<select class=\"rating\">";
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
                    html_code += "<input type=\"text\" class=\"subject\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>Describe your experience. (optional)</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<textarea class=\"description\" style=\"text-align: left; width: 100%; height: 200px; min-width: 100%; min-height: 200px; max-width: 100%; max-height: 200px\"></textarea>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px; padding-bottom: 5px\">";
                    html_code += "<input type=\"button\" class=\"edit_review\" onclick=\"change_review()\" value=\"Change\" />";
                    html_code += "</div>";
                    html_code += "</div>";
                    html_code += "</div>";
                    
                    //Enable review deletion form
                    html_code += "<div class=\"enable_review_deletion_background\">";
                    html_code += "<div class=\"enable_review_deletion_form\">";
                    html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>Name:</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<input type=\"text\" disabled=\"disabled\" class=\"name\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>How was your experience?</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<select disabled=\"disabled\" class=\"rating\">";
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
                    html_code += "<input disabled=\"disabled\" type=\"text\" class=\"subject\" style=\"text-align: left; width: 100%\" />";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    html_code += "<label>Describe your experience. (optional)</label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-bottom: 5px\">";
                    html_code += "<textarea disabled=\"disabled\" class=\"description\" style=\"text-align: left; width: 100%; height: 200px; min-width: 100%; min-height: 200px; max-width: 100%; max-height: 200px\"></textarea>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px; padding-bottom: 5px\">";
                    html_code += "<input type=\"button\" class=\"erase_review\" onclick=\"delete_review()\" value=\"Delete\" />";
                    html_code += "</div>";
                    html_code += "</div>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: right; width: 100%\">";
                    html_code += "<label><a href=\"" + domain + "/cart" + forward_slash_or_html_extension + "\">" +
                            "<span class=\"number_of_items_in_your_cart\"></span></a></label>";
                    html_code += "</div>";
                    html_code += "<div style=\"text-align: left; width: 100%\">";
                    
                    if (product_reviews["sale_product_reviews"][0]["subject"] != "no review"
                            && product_reviews["sale_product_reviews"][0]["subject"] != "fail") {
                        
                        html_code += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
                        html_code += "<p>Leave a review for this product.</p>";
                        html_code += "</div>";
                        html_code += "<div style=\"text-align: left; width: 100%\">";
                        html_code += "<input type=\"button\" class=\"open_create_review_form\" onclick=\"show_create_review_form()\" value=\"Start\" />";
                        html_code += "</div>";
                        
                        for (var i = 0; i < product_reviews["sale_product_reviews"].length; i++) {
                            
                            html_code += each_review(product_reviews["sale_product_reviews"][i]["row_id"],
                            product_reviews["sale_product_reviews"][i]["item_id"],
                            product_reviews["sale_product_reviews"][i]["rating"],
                            product_reviews["sale_product_reviews"][i]["subject"],
                            product_reviews["sale_product_reviews"][i]["description"],
                            product_reviews["sale_product_reviews"][i]["name"],
                            product_reviews["sale_product_reviews"][i]["date_received"]);
                        }
                        
                        html_code += html_footer_all_formats(product_reviews);
                    } else {
                        
                        html_code += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
                        html_code += "<p>Be the first to review this product.</p>";
                        html_code += "</div>";
                        html_code += "<div style=\"text-align: left; width: 100%\">";
                        html_code += "<input type=\"button\" class=\"open_create_review_form\" onclick=\"show_create_review_form()\" value=\"Start\" />";
                        html_code += "</div>";
                    }
                    
                    html_code += "</div>";
                    
                    //Make review public
                    if (form_make_review_public == "Show") {
                        
                        form_security_code = $.url_params("code");
                        form_review = $.url_params("review");
                        form_product = $.url_params("product");
                        
                        make_review_public(form_security_code, form_review, form_product, form_make_review_public);
                    }
                    
                    //Enable review editing
                    if (form_enable_review_editing == "Enable") {
                        
                        form_security_code = $.url_params("code");
                        form_review = $.url_params("review");
                        form_product = $.url_params("product");
                        
                        enable_review_editing(form_security_code, form_review, form_product, form_enable_review_editing);                        
                    }
                    
                    //Enable review deletion
                    if (form_enable_review_deletion == "Enable") {
                        
                        form_security_code = $.url_params("code");
                        form_review = $.url_params("review");
                        form_product = $.url_params("product");
                        
                        enable_review_deletion(form_security_code, form_review, form_product, form_enable_review_deletion);                        
                    }
                } else {
                    
                    html_code += item_not_found_message();
                }
            }
            
            document.getElementById("product_reviews").innerHTML = html_code;
            
            //Get guest session information.
            use_guest_session_search_shopping_cart();
        }
    };
    
    xhttp.open("POST", third_party_domain + "/product-reviews-interface");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("item_id=" + product + "&results_per_page=" + perpage + "&page_number=" + page + "&sort_by=" + sort);
}

//Accept form input.
function unresponsive_sort_by_search() {
    
    var navigation_url = "";
    
    form_sort_by = document.getElementsByClassName("sort_by")[0].value;
    form_results_per_page = $.url_params("perpage");
    form_product = $.url_params("product");
    
    if (form_sort_by == "Sort results by") {
        
        form_sort_by = "Newest";
    }
    
    if (form_results_per_page == "Select results per page") {
        
        form_results_per_page = "10";
    }
    
    navigation_url += domain + "/product-reviews" + forward_slash_or_html_extension + "?";
    navigation_url += "sort=" + form_sort_by;
    navigation_url += "&perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    navigation_url += "&product=" + form_product;
    
    window.location = navigation_url;
}

//Accept form input.
function unresponsive_results_per_page_search() {
    
    var navigation_url = "";
    
    form_sort_by = $.url_params("sort");
    form_results_per_page = document.getElementsByClassName("results_per_page")[0].value;
    form_product = $.url_params("product");
    
    if (form_sort_by == "Sort results by") {
        
        form_sort_by = "Newest";
    }
    
    if (form_results_per_page == "Select results per page") {
        
        form_results_per_page = "10";
    }
    
    navigation_url += domain + "/product-reviews" + forward_slash_or_html_extension + "?";
    navigation_url += "sort=" + form_sort_by;
    navigation_url += "&perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    navigation_url += "&product=" + form_product;
    
    window.location = navigation_url;
}

//Accept form input.
function responsive_sort_by_search() {
    
    var navigation_url = "";
    
    form_sort_by = document.getElementsByClassName("sort_by")[1].value;
    form_results_per_page = $.url_params("perpage");
    form_product = $.url_params("product");
    
    if (form_sort_by == "Sort results by") {
        
        form_sort_by = "Newest";
    }
    
    if (form_results_per_page == "Select results per page") {
        
        form_results_per_page = "10";
    }
    
    navigation_url += domain + "/product-reviews" + forward_slash_or_html_extension + "?";
    navigation_url += "sort=" + form_sort_by;
    navigation_url += "&perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    navigation_url += "&product=" + form_product;
    
    window.location = navigation_url;
}

//Accept form input.
function responsive_results_per_page_search() {
    
    var navigation_url = "";
    
    form_sort_by = $.url_params("sort");
    form_results_per_page = document.getElementsByClassName("results_per_page")[1].value;
    form_product = $.url_params("product");
    
    if (form_sort_by == "Sort results by") {
        
        form_sort_by = "Newest";
    }
    
    if (form_results_per_page == "Select results per page") {
        
        form_results_per_page = "10";
    }
    
    navigation_url += domain + "/product-reviews" + forward_slash_or_html_extension + "?";
    navigation_url += "sort=" + form_sort_by;
    navigation_url += "&perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    navigation_url += "&product=" + form_product;
    
    window.location = navigation_url;
}

function each_review(review_number, item_number, rating, subject, description, name, date_received) {
    
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

//Control the page number display.
function html_footer_all_formats(product_reviews) {
    
    var output = "";
    var page = "";
    var page_num = 1;
    var last_several_pages = 0;
    var two_pages_before = 0;
    var two_pages_after = 0;
    var redirect = "";
    
    page = $.url_params("pagenum");
    
    page_num = parseInt(page, 10);
    
    if (isNaN(page_num)) {
        
        page_num = 1;
        
        redirect = document.location.href;
        
        if (page != null) {
            
            if (page.replace(/\s/g, "").length == 0) {
                
                redirect = redirect.replace(/%20/g, "");
                redirect = redirect.replace("&pagenum=%20", "&pagenum=1");
                redirect = redirect.replace("page=%20", "page=1");
            }
            
            redirect = redirect.replace("&pagenum=", "&pagenum=1");
            redirect = redirect.replace("page=", "page=1");
        } else {
            
            redirect = redirect.replace("&search=Search", "&pagenum=1&search=Search");
            redirect = redirect.replace("&search=Search", "page=1&search=Search");
        }
        
        //If there is no query string in URL, give it a default one.
        if (window.location.href == domain + "/product-reviews" + forward_slash_or_html_extension) {
            
            window.location = domain + "/product-reviews" + forward_slash_or_html_extension + "?sort=Low%20to%20high&perpage=10&pagenum=1&search=Search";
        } else {
            
            window.location = redirect + "&sort=Low%20to%20high&perpage=10&pagenum=1&search=Search";
        }
    }
    
    if (!(page_num < 1) && !(page_num > product_reviews["pages"].length)) {
        
        output += "<span style=\"text-align: center\"><p>";
        
        if (page_num != 1) {
            
            output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + (page_num - 1)) + "\"><< Previous</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";			
        }
        
        if (product_reviews["pages"][0]["page_number"] != page_num) {
            
            output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + product_reviews["pages"][0]["page_number"]) + "\">";
            output += product_reviews["pages"][0]["page_number"];
            output += "</a>";
        } else {
            
            output += "<span style=\"text-decoration: underline\">";
            output += product_reviews["pages"][0]["page_number"];
            output += "</span>";
        }
        
        if (product_reviews["pages"].length <= 5) {
            
            //Format spaces for other page numbers.
            for (var i = 1; i < product_reviews["pages"].length; i++) {
                
                output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                
                if (product_reviews["pages"][i]["page_number"] == page_num) {
                    
                    output += "<span style=\"text-decoration: underline\">";
                } else {
                    
                    output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                    "&pagenum=" + product_reviews["pages"][i]["page_number"]) + "\">";
                }
                
                output += product_reviews["pages"][i]["page_number"];
                
                if (product_reviews["pages"][i]["page_number"] == page_num) {
                    
                    output += "</span>";
                } else {
                    
                    output += "</a>";
                }
            }
        } else {
            
            //Reformat page numbers if page quantity is greater than 5.
            //If page between 1 and 5 is browsed show the first five pages, plus the last page.
            if (page_num <= 5) {
                
                //Format spaces for other page numbers.
                //Show page numbers 2 through 5.
                for (var i = 1; i <= 5; i++) {
                    
                    output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    
                    if (product_reviews["pages"][i]["page_number"] == page_num) {
                        
                        output += "<span style=\"text-decoration: underline\">";
                    } else {
                        
                        output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                        "&pagenum=" + product_reviews["pages"][i]["page_number"]) + "\">";
                    }
                    
                    output += product_reviews["pages"][i]["page_number"];
                    
                    if (product_reviews["pages"][i]["page_number"] == page_num) {
                        
                        output += "</span>";
                    } else {
                        
                        output += "</a>";
                    }
                }
                
                //Show last page number.
                output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                "&pagenum=" + product_reviews["pages"][product_reviews["pages"].length - 1]["page_number"]) + "\">" +
                        product_reviews["pages"][product_reviews["pages"].length - 1]["page_number"] + "</a>";
            } else {
                
                //If page browsed page number is within the last five pages, show the last five pages.
                if (page_num > (product_reviews["pages"].length - 5)) {
                    
                    last_several_pages = product_reviews["pages"].length - 5;
                    
                    for (var i = last_several_pages; i < product_reviews["pages"].length; i++) {
                        
                        //Last several pages should follow right after the ellipses.
                        if (i == last_several_pages) {
                            
                            output += "&nbsp;&nbsp;...&nbsp;&nbsp;";
                            
                            //Focused page
                            if (product_reviews["pages"][i]["page_number"] == page_num) {
                                
                                output += "<span style=\"text-decoration: underline\">";
                            } else {
                                
                                output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                                "&pagenum=" + product_reviews["pages"][i]["page_number"]) + "\">";
                            }
                            
                            output += product_reviews["pages"][i]["page_number"];
                            
                            //Focused page
                            if (product_reviews["pages"][i]["page_number"] == page_num) {
                                
                                output += "</span>";
                            } else {
                                
                                output += "</a>";
                            }
                        } else {
                            
                            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                            
                            //Focused page
                            if (product_reviews["pages"][i]["page_number"] == page_num) {
                                
                                output += "<span style=\"text-decoration: underline\">";
                            } else {
                                
                                output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                                "&pagenum=" + product_reviews["pages"][i]["page_number"]) + "\">";
                            }
                            
                            output += product_reviews["pages"][i]["page_number"];
                            
                            //Focused page
                            if (product_reviews["pages"][i]["page_number"] == page_num) {
                                
                                output += "</span>";
                            } else {
                                
                                output += "</a>";
                            }
                        }
                    }
                } else {
                    
                    two_pages_before = page_num - 2;
                    two_pages_after = page_num + 2;
                    
                    for (var i = two_pages_before; i < page_num; i++) {
                        
                        //Show two page numbers before focused page.
                        if (i == two_pages_before) {
                            
                            output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                            "&pagenum=" + product_reviews["pages"][i - 1]["page_number"]) + "\">" + product_reviews["pages"][i - 1]["page_number"] + "</a>";
                        } else {
                            
                            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                            "&pagenum=" + product_reviews["pages"][i - 1]["page_number"]) + "\">" + product_reviews["pages"][i - 1]["page_number"] + "</a>";
                        }
                    }
                    
                    output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style=\"text-decoration: underline\">" + page_num + "</span>";
                    
                    for (var i = page_num; i < two_pages_after; i++) {
                        
                        //Show two page numbers after focused page.
                        output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                        "&pagenum=" + product_reviews["pages"][i]["page_number"]) + "\">" + product_reviews["pages"][i]["page_number"] + "</a>";
                    }
                    
                    output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                    "&pagenum=" + product_reviews["pages"].length) + "\">" + product_reviews["pages"].length + "</a>";
                }
            }
        }
        
        if (page_num != product_reviews["pages"].length) {
            
            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + (page_num + 1)) + "\">Next >></a>";
        }
        
        output += "</p></span>";
    }
    
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
    name = document.getElementsByClassName("name")[0].value;
    email = document.getElementsByClassName("email")[0].value;
    rating = document.getElementsByClassName("rating")[0].value;
    subject = document.getElementsByClassName("subject")[0].value;
    description = document.getElementsByClassName("description")[0].value;
    
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
            
            document.getElementsByClassName("name")[0].style.backgroundColor = "white";
            document.getElementsByClassName("email")[0].style.backgroundColor = "white";
            document.getElementsByClassName("rating")[0].style.backgroundColor = "white";
            document.getElementsByClassName("subject")[0].style.backgroundColor = "white";
            document.getElementsByClassName("description")[0].style.backgroundColor = "white";
            
            //Check if the user incorrectly submits the form.
            if (create_new_review_feedback[0]["form_message"] == "error creating review") {
                
                for (var i = 1; i < create_new_review_feedback.length; i++) {
                    
                    if (create_new_review_feedback[i]["form_message"] != "") {
                        
                        create_new_review_message += "<div><label><span style=\"color: red\">" + create_new_review_feedback[i]["form_message"] +
                                "</span></label></div>";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Name is required.") {
                        
                        document.getElementsByClassName("name")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Email is required.") {
                        
                        document.getElementsByClassName("email")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Rating is required.") {
                        
                        document.getElementsByClassName("rating")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Subject requires at least one non-space character.  Otherwise, leave this field blank.") {
                        
                        document.getElementsByClassName("subject")[0].style.backgroundColor = "pink";
                    }
                    
                    if (create_new_review_feedback[i]["form_message"] == "Description requires at least one non-space character.  Otherwise, leave this field blank.") {
                        
                        document.getElementsByClassName("description")[0].style.backgroundColor = "pink";
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

function change_review() {

    var change_review_message = "";
    var change_review_feedback;
    var review = "";
    var product = "";
    var name = "";
    var rating = "";
    var subject = "";
    var description = "";
    
    review = $.url_params("review");
    product = $.url_params("product");
    name = document.getElementsByClassName("name")[1].value;
    rating = document.getElementsByClassName("rating")[1].value;
    subject = document.getElementsByClassName("subject")[1].value;
    description = document.getElementsByClassName("description")[1].value;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            change_review_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            change_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            change_review_message += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            change_review_message += "</div>";
            change_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".enable_review_editing_form").html(change_review_message);
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            change_review_feedback = JSON.parse(this.responseText);
            
            document.getElementsByClassName("name")[1].style.backgroundColor = "white";
            document.getElementsByClassName("rating")[1].style.backgroundColor = "white";
            document.getElementsByClassName("subject")[1].style.backgroundColor = "white";
            document.getElementsByClassName("description")[1].style.backgroundColor = "white";
            
            //Check if the user incorrectly submits the form.
            if (change_review_feedback[0]["form_message"] == "error changing review") {
                
                for (var i = 1; i < change_review_feedback.length; i++) {
                    
                    if (change_review_feedback[i]["form_message"] != "") {
                        
                        change_review_message += "<div><label><span style=\"color: red\">" + change_review_feedback[i]["form_message"] +
                                "</span></label></div>";
                    }
                    
                    if (change_review_feedback[i]["form_message"] == "Name is required.") {
                        
                        document.getElementsByClassName("name")[1].style.backgroundColor = "pink";
                    }
                    
                    if (change_review_feedback[i]["form_message"] == "Rating is required.") {
                        
                        document.getElementsByClassName("rating")[1].style.backgroundColor = "pink";
                    }
                    
                    if (change_review_feedback[i]["form_message"] == "Subject requires at least one non-space character.  Otherwise, leave this field blank.") {
                        
                        document.getElementsByClassName("subject")[1].style.backgroundColor = "pink";
                    }
                    
                    if (change_review_feedback[i]["form_message"] == "Description requires at least one non-space character.  Otherwise, leave this field blank.") {
                        
                        document.getElementsByClassName("description")[1].style.backgroundColor = "pink";
                    }
                }
                
                $(".enable_review_editing_form_feedback").html(change_review_message);
            } else if (change_review_feedback[0]["form_message"] == "success changing review") {
                
                change_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                change_review_message += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                change_review_message += "</div>";
                change_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>Your review was successfully changed.</label></div>";
                
                $(".enable_review_editing_form").html(change_review_message);
            } else {
                
                change_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                change_review_message += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                change_review_message += "</div>";
                change_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".enable_review_editing_form").html(change_review_message);
            }
        }
    };
    
    xhttp.open("POST", third_party_domain + "/change-review");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("row_id=" + review + "&item_id=" + product + "&name=" + name + "&rating=" + rating + "&subject=" + subject + "&description=" + description + "&change_review=Submit");
}

function delete_review() {

    var delete_review_message = "";
    var delete_review_feedback;
    var review = "";
    var product = "";
    
    review = $.url_params("review");
    product = $.url_params("product");
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            delete_review_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            delete_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            delete_review_message += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            delete_review_message += "</div>";
            delete_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".enable_review_deletion_form").html(delete_review_message);
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            delete_review_feedback = JSON.parse(this.responseText);
            
            if (delete_review_feedback[0]["form_message"] == "success deleting review") {
                
                delete_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                delete_review_message += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                delete_review_message += "</div>";
                delete_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>Your review was successfully deleted.</label></div>";
                
                $(".enable_review_deletion_form").html(delete_review_message);
            } else {
                
                delete_review_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                delete_review_message += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                delete_review_message += "</div>";
                delete_review_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".enable_review_deletion_form").html(delete_review_message);
            }
        }
    };
    
    xhttp.open("POST", third_party_domain + "/delete-review");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("row_id=" + review + "&item_id=" + product + "&delete_review=Delete");
}

function make_review_public(form_security_code, form_review, form_product, form_make_review_public) {
    
    var make_review_public_message = "";
    var make_review_public_feedback;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            make_review_public_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            make_review_public_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            make_review_public_message += "<label><span class=\"close_make_review_public_form\" onclick=\"hide_make_review_public_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            make_review_public_message += "</div>";
            make_review_public_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".make_review_public_form").html(make_review_public_message);
            
            show_make_review_public_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            make_review_public_feedback = JSON.parse(this.responseText);
            
            //Check if the user incorrectly submits the form.
            if (make_review_public_feedback[0]["form_message"] == "error making review public") {
                
                make_review_public_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                make_review_public_message += "<label><span class=\"close_make_review_public_form\" onclick=\"hide_make_review_public_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                make_review_public_message += "</div>";
                make_review_public_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>That review is not valid. " +
                        "Check your email, and try again.</label></div>";
            } else if (make_review_public_feedback[0]["form_message"] == "success making review public") {
                
                make_review_public_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                make_review_public_message += "<label><span class=\"close_make_review_public_form\" onclick=\"hide_make_review_public_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                make_review_public_message += "</div>";
                make_review_public_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>Your review is now visible to everyone.</label></div>";
            } else {
                
                make_review_public_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                make_review_public_message += "<label><span class=\"close_make_review_public_form\" onclick=\"hide_make_review_public_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                make_review_public_message += "</div>";
                make_review_public_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            }
            
            $(".make_review_public_form").html(make_review_public_message);
            
            show_make_review_public_form();
        }
    };
    
    xhttp.open("POST", third_party_domain + "/make-review-public");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("security_code=" + form_security_code + "&row_id=" + form_review +
            "&item_id=" + form_product + "&make_review_public=" + form_make_review_public);
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

function enable_review_editing(form_security_code, form_review, form_product, form_enable_review_editing) {
    
    var enable_review_editing_message = "";
    var enable_review_editing_feedback;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            enable_review_editing_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            enable_review_editing_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            enable_review_editing_message += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            enable_review_editing_message += "</div>";
            enable_review_editing_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".enable_review_editing_form").html(enable_review_editing_message);
            
            show_enable_review_editing_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            enable_review_editing_feedback = JSON.parse(this.responseText);
            
            if (enable_review_editing_feedback[0]["form_message"] == "error enabling review editing") {
                
                enable_review_editing_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                enable_review_editing_message += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                enable_review_editing_message += "</div>";
                enable_review_editing_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>That review is not valid.  Check your email, and try again.</label></div>";
                
                $(".enable_review_editing_form").html(enable_review_editing_message);
            } else if (enable_review_editing_feedback[0]["form_message"] == "success enabling review editing") {
                
                document.getElementsByClassName("name")[1].value = enable_review_editing_feedback[4]["form_message"].replace(/&apos;/g,'\'');
                document.getElementsByClassName("rating")[1].value = enable_review_editing_feedback[1]["form_message"];
                
                if (enable_review_editing_feedback[2]["form_message"] == "No subject") {
                    
                    document.getElementsByClassName("subject")[1].value = "";
                } else {
                    
                    document.getElementsByClassName("subject")[1].value = enable_review_editing_feedback[2]["form_message"].replace(/&apos;/g,'\'');
                }
                
                if (enable_review_editing_feedback[3]["form_message"] == "No comment") {
                    
                    document.getElementsByClassName("description")[1].value = "";
                } else {
                    
                    document.getElementsByClassName("description")[1].value = enable_review_editing_feedback[3]["form_message"].replace(/&apos;/g,'\'');
                }
            } else {
                
                enable_review_editing_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                enable_review_editing_message += "<label><span class=\"close_enable_review_editing_form\" onclick=\"hide_enable_review_editing_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                enable_review_editing_message += "</div>";
                enable_review_editing_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".enable_review_editing_form").html(enable_review_editing_message);
            }
            
            show_enable_review_editing_form();
        }
    };
    
    xhttp.open("POST", third_party_domain + "/enable-review-editing");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("security_code=" + form_security_code + "&row_id=" + form_review + "&item_id=" + form_product + "&enable_review_editing=" + form_enable_review_editing);    
}

function enable_review_deletion(form_security_code, form_review, form_product, form_enable_review_deletion) {
    
    var enable_review_deletion_message = "";
    var enable_review_deletion_feedback;
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            enable_review_deletion_message = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            enable_review_deletion_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            enable_review_deletion_message += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            enable_review_deletion_message += "</div>";
            enable_review_deletion_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".enable_review_deletion_form").html(enable_review_deletion_message);
            
            show_enable_review_deletion_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            enable_review_deletion_feedback = JSON.parse(this.responseText);
            
            if (enable_review_deletion_feedback[0]["form_message"] == "error enabling review deletion") {
                
                enable_review_deletion_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                enable_review_deletion_message += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                enable_review_deletion_message += "</div>";
                enable_review_deletion_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>That review is not valid.  Check your email, and try again.</label></div>";
                
                $(".enable_review_deletion_form").html(enable_review_deletion_message);
            } else if (enable_review_deletion_feedback[0]["form_message"] == "success enabling review deletion") {
                
                document.getElementsByClassName("name")[2].value = enable_review_deletion_feedback[4]["form_message"].replace(/&apos;/g,'\'');
                document.getElementsByClassName("rating")[2].value = enable_review_deletion_feedback[1]["form_message"];
                
                if (enable_review_deletion_feedback[2]["form_message"] == "No subject") {
                    
                    document.getElementsByClassName("subject")[2].value = "";
                } else {
                    
                    document.getElementsByClassName("subject")[2].value = enable_review_deletion_feedback[2]["form_message"].replace(/&apos;/g,'\'');
                }
                
                if (enable_review_deletion_feedback[3]["form_message"] == "No comment") {
                    
                    document.getElementsByClassName("description")[2].value = "";
                } else {
                    
                    document.getElementsByClassName("description")[2].value = enable_review_deletion_feedback[3]["form_message"].replace(/&apos;/g,'\'');
                }
            } else {
                
                enable_review_deletion_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                enable_review_deletion_message += "<label><span class=\"close_enable_review_deletion_form\" onclick=\"hide_enable_review_deletion_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                enable_review_deletion_message += "</div>";
                enable_review_deletion_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".enable_review_deletion_form").html(enable_review_deletion_message);
            }
            
            show_enable_review_deletion_form();
        }
    };
    
    xhttp.open("POST", third_party_domain + "/enable-review-deletion");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("security_code=" + form_security_code + "&row_id=" + form_review + "&item_id=" + form_product + "&enable_review_deletion=" + form_enable_review_deletion);    
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
    var shopping_cart_items;
    
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

function get_raw_minute() {
    
    var today_date = new Date();
    var raw_minute = parseInt((today_date.getTime() / 1000) / 60);
    
    return raw_minute;
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

//Make review public
function show_make_review_public_form() {
    
    $(".make_review_public_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("make_review_public_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_make_review_public_form() {
    
    window.location = document.location.href.replace("code=" + form_security_code + "&review=" + form_review +
            "&product=" + form_product + "&sort=Low%20to%20high&perpage=10&pagenum=1&showreview=Show&search=Search",
    "product=" + form_product + "&sort=Low%20to%20high&perpage=10&pagenum=1&search=Search");
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

//Enable review editing
function show_enable_review_editing_form() {
    
    $(".enable_review_editing_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("enable_review_editing_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_enable_review_editing_form() {
    
    var page = "";
    var perpage = "";
    
    page = $.url_params("pagenum");
    perpage = $.url_params("perpage");
    
    window.location = document.location.href.replace("code=" + form_security_code + "&review=" + form_review +
            "&product=" + form_product + "&sort=Low%20to%20high&perpage=" + perpage + "&pagenum=" + page + "&enablereviewediting=Enable&search=Search",
    "product=" + form_product + "&sort=Low%20to%20high&perpage=" + perpage + "&pagenum=" + page + "&search=Search");
}

//Enable review deletion
function show_enable_review_deletion_form() {
    
    $(".enable_review_deletion_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("enable_review_deletion_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_enable_review_deletion_form() {
    
    var page = "";
    var perpage = "";
    
    page = $.url_params("pagenum");
    perpage = $.url_params("perpage");
    
    window.location = document.location.href.replace("code=" + form_security_code + "&review=" + form_review +
            "&product=" + form_product + "&sort=Low%20to%20high&perpage=" + perpage + "&pagenum=" + page + "&enablereviewdeletion=Enable&search=Search",
    "product=" + form_product + "&sort=Low%20to%20high&perpage=" + perpage + "&pagenum=" + page + "&search=Search");
}