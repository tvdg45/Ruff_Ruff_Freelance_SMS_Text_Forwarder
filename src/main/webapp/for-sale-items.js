//Global variables for rendering HTML content
var html_code = "";
var html_select_tag_code = "";
var for_sale_items;
var items_to_be_added;

//Global variables for form input
var form_category = "";
var form_keywords = "";
var form_sort_by = "";
var form_results_per_page = "";
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

//Sort by page options
function sort_by() {
    
    var output = "";
    
    output += "<option value=\"Low to high\">Price low to high</option>";
    output += "<option value=\"High to low\">Price high to low</option>";
    
    return output;
}

//Results per page options
function results_per_page() {
    
    var output = "";
    
    output += "<option value=\"10\">10 results per page</option>";
    output += "<option value=\"20\">20 results per page</option>";
    output += "<option value=\"30\">30 results per page</option>";
    output += "<option value=\"40\">40 results per page</option>";
    output += "<option value=\"50\">50 results per page</option>";
    
    return output;
}

function html_header_pc_format() {
    
    var output = "";
    
    output += "<div class=\"header_pc_format\">";
    output += "<div style=\"text-align: left; width: 100%\">";
    output += "<div style=\"display: table; text-align: left; width: 100%\">";
    output += "<div style=\"display: table-row; text-align: left; width: 100%\">";
    output += "<div style=\"display: table-cell; text-align: left; width: 15%; vertical-align: top\">";
    output += "<select class=\"category\">";
    output += "<option value=\"All categories\">All categories</option>";
    output += "</select>";
    output += "</div>";
    output += "<div style=\"display: table-cell; text-align: left; width: 70%; vertical-align: top\">";
    output += "<input type=\"text\" class=\"unresponsive_keywords\" style=\"text-align: left; margin-left: 1%; margin-right: 1%; width: 98%\" placeholder=\"Type your search here\" />";
    output += "</div>";
    output += "<div style=\"display: table-cell; text-align: left; width: auto; vertical-align: top\">";
    output += "<input type=\"button\" class=\"find\" onclick=\"unresponsive_search()\" value=\"Search\" />";
    output += "</div>";
    output += "</div>";
    output += "</div>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%\"><br />";
    output += "<div style=\"display: table; text-align: left; width: 100%\">";
    output += "<div style=\"display: table-row; text-align: left; width: 100%\">";
    output += "<div style=\"display: table-cell; text-align: left; width: 15%\">";
    output += "<select class=\"sort_by\">";
    output += sort_by();
    output += "</select>";
    output += "</div>";
    output += "<div style=\"display: table-cell; text-align: left; width: auto\">";
    output += "<select class=\"results_per_page\">";
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
    output += "<select class=\"category\">";
    output += "<option value=\"All categories\">All categories</option>";
    output += "</select>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
    output += "<input type=\"text\" class=\"responsive_keywords\" style=\"text-align: left; width: 100%\" placeholder=\"Type your search here\" />";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
    output += "<select class=\"sort_by\">";
    output += sort_by();
    output += "</select>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
    output += "<select class=\"results_per_page\">";
    output += results_per_page();
    output += "</select>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
    output += "<input type=\"button\" class=\"find\" onclick=\"responsive_search()\" value=\"Search\" />";
    output += "</div>";
    output += "</div>";
    
    return output;
}

window.onload = function() {
    
    var category = "";
    var keywords = "";
    var perpage = "";
    var page = "";
    var sort = "";
    var search = "";
    
    category = $.url_params("category");
    keywords = $.url_params("keywords");
    perpage = $.url_params("perpage");
    page = $.url_params("pagenum");
    sort = $.url_params("sort");
    search = $.url_params("search");
    
    html_code += html_header_pc_format();
    html_code += html_header_mobile_format();
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            document.getElementById("for_sale_items").innerHTML = "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            document.getElementById("for_sale_items").innerHTML = "<p>Sorry, the online store is not available.</p>";
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            for_sale_items = JSON.parse(this.responseText);
            items_to_be_added = JSON.parse(this.responseText)
            
            //Gather possible search categories for user.
            if (for_sale_items["sale_categories"][0]["category"] != "no category" && for_sale_items["sale_categories"][0]["category"] != "fail") {
                
                for (var i = 0; i < for_sale_items["sale_categories"].length; i++) {
                    
                    html_select_tag_code += "<option value=\"" + for_sale_items["sale_categories"][i]["category"] +
                            "\">" + for_sale_items["sale_categories"][i]["category"] + "</option>";
                }
            }
            
            //Control search results from query.
            if (for_sale_items["sale_items"][0]["item"] == "no item" || for_sale_items["sale_items"][0]["item"] == "fail") {
                
                if (search != "Search") {
                    
                    category = "";
                    keywords = "";
                    
                    html_code += "<p>Sorry, the store is currently closed.</p>";
                } else {
                    
                    if (keywords != "" && keywords != null) {
                        
                        if (keywords.replace(/\s/g, "").length > 0) {
                            
                            html_code += "<p>Sorry, but no product by the keywords of \"" + keywords + "\" was found.  Try a broader search.</p>";
                        } else {
                            
                            category = "";
                            keywords = "";
                        }
                    } else if (category != "" && category != null) {
                        
                        if (category.replace(/\s/g, "").length > 0) {
                            
                            html_code += "<p>Sorry, but no product by the category of \"" + category + "\" was found.  Try a broader search.</p>";
                        } else {
                            
                            category = "";
                            keywords = "";
                        }
                    } else {
                        
                        category = "";
                        keywords = "";
                        
                        html_code += "<p>Sorry, no products were found.  Try searching by category, keywords, or both.</p>";
                    }
                }
            } else {
                
                html_code += html_body_pc_format(for_sale_items);
                html_code += html_body_mobile_format(for_sale_items);
                html_code += html_footer_all_formats(for_sale_items);
                
                //Add to cart form
                html_code += "<div class=\"add_to_cart_background\">";
                html_code += "<div class=\"add_to_cart_form\">";
                html_code += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                html_code += "<label><span class=\"close_add_to_cart_form\" onclick=\"hide_add_to_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                html_code += "</div>";
                html_code += "</div>";
                html_code += "</div>";
            }
            
            document.getElementById("for_sale_items").innerHTML = html_code;
            
            //Get guest session information.
            use_guest_session_search_shopping_cart();
            
            //Add sales category options from JSON string.
            $(".category").append(html_select_tag_code);
        }
    };
    
    xhttp.open("POST", third_party_domain + "/for-sale-items-interface");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("category=" + category + "&keywords=" + keywords + "&results_per_page=" + perpage + "&page_number=" + page + "&sort_by=" + sort + "&search_items=" + search);
}

//Accept form input.
function unresponsive_search() {
    
    var navigation_url = "";
    
    form_category = document.getElementsByClassName("category")[0].value;
    form_keywords = $(".unresponsive_keywords").val();
    form_sort_by = document.getElementsByClassName("sort_by")[0].value;
    form_results_per_page = document.getElementsByClassName("results_per_page")[0].value;
    
    navigation_url += domain + "/store" + forward_slash_or_html_extension + "?";
    navigation_url += "sort=" + form_sort_by;
    navigation_url += "&perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    
    if (form_category != "All categories") {
        
        navigation_url += "&category=" + form_category
    }
    
    if (form_keywords != "" && form_keywords != null) {
        
        if (form_keywords.replace(/\s/g, "").length > 0) {
            
            navigation_url += "&keywords=" + form_keywords;
        }
    }
    
    navigation_url += "&search=Search";
    
    window.location = navigation_url;
}

//Accept form input.
function responsive_search() {
    
    var navigation_url = "";
    
    form_category = document.getElementsByClassName("category")[1].value;
    form_keywords = $(".responsive_keywords").val();
    form_sort_by = document.getElementsByClassName("sort_by")[1].value;
    form_results_per_page = document.getElementsByClassName("results_per_page")[1].value;
    
    navigation_url += domain + "/store" + forward_slash_or_html_extension + "?";
    navigation_url += "sort=" + form_sort_by;
    navigation_url += "&perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    
    if (form_category != "All categories") {
        
        navigation_url += "&category=" + form_category
    }
    
    if (form_keywords != "" && form_keywords != null) {
        
        if (form_keywords.replace(/\s/g, "").length > 0) {
            
            navigation_url += "&keywords=" + form_keywords;
        }
    }
    
    navigation_url += "&search=Search";
    
    window.location = navigation_url;
}

function html_body_pc_format(for_sale_items) {
    
    var output = "";
    var items_in_row = 0;
    
    output += "<div class=\"body_pc_format\"><br />";
    
    output += "<div style=\"text-align: right; width: 100%\">";
    output += "<label><a href=\"" + domain + "/cart" + forward_slash_or_html_extension + "\">" +
            "<span class=\"number_of_items_in_your_cart\"></span></a></label>";
    output += "</div>";
    
    if (for_sale_items["sale_items"].length < 4) {
        
        output += "<div style=\"text-align: left; width: 100%\">";
        output += "<div style=\"text-align: left; width: 100%; display: table\">";
        output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
        
        for (var i = 0; i < for_sale_items["sale_items"].length; i++) {
        
            output += "<div style=\"text-align: left; width: 25%; height: 100%; display: table-cell; vertical-align: top\">";
            output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
            output += "<div style=\"text-align: left; width: 100%; height: 300px\">";
            output += "<a href=\"" + domain + "/product-details" + forward_slash_or_html_extension + "?product=" +
                    for_sale_items["sale_items"][i]["row_id"] + "\">";
            
            if (for_sale_items["sale_items"][i]["thumbnail"] != "" && for_sale_items["sale_items"][i]["thumbnail"] != null) {
                
                if (for_sale_items["sale_items"][i]["thumbnail"].replace(/\s/g, "").length > 0) {
                    
                    output += "<img class=\"pc_format_for_sale_image\" src=\"" + for_sale_items["sale_items"][i]["thumbnail"] + "\" />";
                } else {
                    
                    output += "<img class=\"pc_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
                }
            } else {
                
                output += "<img class=\"pc_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
            
            output += "</a>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label><a href=\"" + domain + "/product-details" + forward_slash_or_html_extension + "?product=" +
                    for_sale_items["sale_items"][i]["row_id"] + "\">View item</a></label>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label><b>" + for_sale_items["sale_items"][i]["item"] + "</b></label>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label><span style=\"font-size: 16pt\"><b>$" + for_sale_items["sale_items"][i]["price"] + "</b></span></label>";
            output += "</div>";
            
            if (for_sale_items["sale_items"][i]["inventory"] < 1) {
                
                output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
                output += "<label>This item is out of stock.</label>";
                output += "</div>";
            } else {
                
                output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
                output += "<label>Items available (" + for_sale_items["sale_items"][i]["inventory"] + ")</label>";
                output += "</div>";
            }
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px; margin-bottom: 15px\">";
            
            if (for_sale_items["sale_items"][i]["inventory"] < 1) {
                
                output += "<input type=\"button\" disabled=\"disabled\" class=\"place_in_cart\" onclick=\"add_to_cart('" +
                        i + "')\" value=\"Add to cart\" />";
            } else {
                
                output += "<input type=\"button\" class=\"place_in_cart\" onclick=\"add_to_cart('" +
                        i + "')\" value=\"Add to cart\" />";
            }
            
            output += "</div>";
            output += "</div>";
            output += "</div>";
        }
        
        for (var i = for_sale_items["sale_items"].length; i < 4; i++) {
            
            output += "<div style=\"text-align: left; width: 25%; height: 100%; display: table-cell; vertical-align: top\">";
            output += "</div>";
        }
        
        output += "</div>";
        output += "</div>";
        output += "</div>";
    } else { 
    
        output += "<div style=\"text-align: left; width: 100%\">";
        output += "<div style=\"text-align: left; width: 100%; display: table\">";
        
        for (var i = 0; i < for_sale_items["sale_items"].length; i++) {
            
            if (items_in_row == 4) {
                
                output += "</div>";
                
                items_in_row = 0;
            }
            
            if (items_in_row == 0) {
                
                output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
            }
            
            output += "<div style=\"text-align: left; width: 25%; height: 100%; display: table-cell; vertical-align: top\">";
            output += "<div style=\"text-align: left; width: 96%; margin-left: 2%; margin-right: 2%; margin-top: 15px\">";
            output += "<div style=\"text-align: left; width: 100%; height: 300px\">";
            output += "<a href=\"" + domain + "/product-details" + forward_slash_or_html_extension + "?product=" +
                    for_sale_items["sale_items"][i]["row_id"] + "\">";
            
            if (for_sale_items["sale_items"][i]["thumbnail"] != "" && for_sale_items["sale_items"][i]["thumbnail"] != null) {
                
                if (for_sale_items["sale_items"][i]["thumbnail"].replace(/\s/g, "").length > 0) {
                    
                    output += "<img class=\"pc_format_for_sale_image\" src=\"" + for_sale_items["sale_items"][i]["thumbnail"] + "\" />";
                } else {
                    
                    output += "<img class=\"pc_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
                }
            } else {
                
                output += "<img class=\"pc_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
            
            output += "</a>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label><a href=\"" + domain + "/product-details" + forward_slash_or_html_extension + "?product=" +
                    for_sale_items["sale_items"][i]["row_id"] + "\">View item</a></label>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label><b>" + for_sale_items["sale_items"][i]["item"] + "</b></label>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label><span style=\"font-size: 16pt\"><b>$" + for_sale_items["sale_items"][i]["price"] + "</b></span></label>";
            output += "</div>";
            
            if (for_sale_items["sale_items"][i]["inventory"] < 1) {
                
                output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
                output += "<label>This item is out of stock.</label>";
                output += "</div>";
            } else {
                
                output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
                output += "<label>Items available (" + for_sale_items["sale_items"][i]["inventory"] + ")</label>";
                output += "</div>";
            }
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px; margin-bottom: 15px\">";
            
            if (for_sale_items["sale_items"][i]["inventory"] < 1) {
                
                output += "<input type=\"button\" disabled=\"disabled\" class=\"place_in_cart\" onclick=\"add_to_cart('" +
                        for_sale_items["sale_items"][i]["row_id"] + "', '" + for_sale_items["sale_items"][i]["item"] + "', '" + for_sale_items["sale_items"][i]["thumbnail"] + 
                        "', '" + for_sale_items["sale_items"][i]["item_category"] + "', '" + for_sale_items["sale_items"][i]["description"] +
                        "', '" + for_sale_items["sale_items"][i]["price"] + "')\" value=\"Add to cart\" />";
            } else {
                
                output += "<input type=\"button\" class=\"place_in_cart\" onclick=\"add_to_cart('" +
                        for_sale_items["sale_items"][i]["row_id"] + "', '" + for_sale_items["sale_items"][i]["item"] + "', '" + for_sale_items["sale_items"][i]["thumbnail"] + 
                        "', '" + for_sale_items["sale_items"][i]["item_category"] + "', '" + for_sale_items["sale_items"][i]["description"] +
                        "', '" + for_sale_items["sale_items"][i]["price"] + "')\" value=\"Add to cart\" />";
            }
            
            output += "</div>";
            output += "</div>";
            
            items_in_row++;
            
            output += "</div>";
        }
        
        output += "</div>";
        output += "</div>";
        
        if (items_in_row > 0) {
            
            output += "</div>";
        }
    }
    
    output += "</div>";
    
    return output;
}

function html_body_mobile_format(for_sale_items) {
    
    var output = "";
    
    output += "<div class=\"body_mobile_format\">";
    output += "<div style=\"text-align: right; width: 100%\">";
    output += "<label><a href=\"" + domain + "/cart" + forward_slash_or_html_extension + "\">" +
            "<span class=\"number_of_items_in_your_cart\"></span></a></label>";
    output += "</div>";
    output += "<div style=\"text-align: left; width: 100%\">";
    
    for (var i = 0; i < for_sale_items["sale_items"].length; i++) {
        
        output += "<div style=\"text-align: left; width: 100%; margin-top: 15px; height: 300px;\">";
        output += "<a href=\"" + domain + "/product-details" + forward_slash_or_html_extension + "?product=" +
                for_sale_items["sale_items"][i]["row_id"] + "\">";
        
        if (for_sale_items["sale_items"][i]["thumbnail"] != "" && for_sale_items["sale_items"][i]["thumbnail"] != null) {
            
            if (for_sale_items["sale_items"][i]["thumbnail"].replace(/\s/g, "").length > 0) {
                
                output += "<img class=\"mobile_format_for_sale_image\" src=\"" + for_sale_items["sale_items"][i]["thumbnail"] + "\" />";
            } else {
                
                output += "<img class=\"mobile_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
        } else {
            
            output += "<img class=\"mobile_format_for_sale_image\" src=\"" + domain + "/images/no-file.png\" />";
        }
        
        output += "</a>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
        output += "<label><a href=\"" + domain + "/product-details" + forward_slash_or_html_extension + "?product=" +
                for_sale_items["sale_items"][i]["row_id"] + "\">View item</a></label>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
        output += "<label><b>" + for_sale_items["sale_items"][i]["item"] + "</b></label>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
        output += "<label><span style=\"font-size: 16pt\"><b>$" + for_sale_items["sale_items"][i]["price"] + "</b></span></label>";
        output += "</div>";
        
        if (for_sale_items["sale_items"][i]["inventory"] < 1) {
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label>This item is out of stock.</label>";
            output += "</div>";
        } else {
            
            output += "<div style=\"text-align: left; width: 100%; padding-top: 15px\">";
            output += "<label>Items available (" + for_sale_items["sale_items"][i]["inventory"] + ")</label>";
            output += "</div>";
        }
        
        output += "<div style=\"text-align: left; width: 100%; padding-top: 15px; margin-bottom: 15px\">";
        
        if (for_sale_items["sale_items"][i]["inventory"] < 1) {
            
            output += "<input type=\"button\" disabled=\"disabled\" class=\"place_in_cart\" onclick=\"add_to_cart('" +
                    i + "')\" value=\"Add to cart\" />";
        } else {
            
            output += "<input type=\"button\" class=\"place_in_cart\" onclick=\"add_to_cart('" +
                    i + "')\" value=\"Add to cart\" />";
        }
        
        output += "</div>";
    }
    
    output += "</div>";
    
    output += "</div>";
    
    return output;
}

//Control the page number display.
function html_footer_all_formats(for_sale_items) {
    
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
        if (window.location.href == (domain + "/store" + forward_slash_or_html_extension)) {
            
            window.location = domain + "/store" + forward_slash_or_html_extension + "?sort=Low%20to%20high&perpage=10&pagenum=1&search=Search";
        } else {
            
            window.location = redirect;
        }
    }
    
    if (!(page_num < 1) && !(page_num > for_sale_items["pages"].length)) {
        
        output += "<span style=\"text-align: center\"><p>";
        
        if (page_num != 1) {
            
            output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + (page_num - 1)) + "\"><< Previous</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";			
        }
        
        if (for_sale_items["pages"][0]["page_number"] != page_num) {
            
            output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + for_sale_items["pages"][0]["page_number"]) + "\">";
            output += for_sale_items["pages"][0]["page_number"];
            output += "</a>";
        } else {
            
            output += "<span style=\"text-decoration: underline\">";
            output += for_sale_items["pages"][0]["page_number"];
            output += "</span>";
        }
        
        if (for_sale_items["pages"].length <= 5) {
            
            //Format spaces for other page numbers.
            for (var i = 1; i < for_sale_items["pages"].length; i++) {
                
                output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                
                if (for_sale_items["pages"][i]["page_number"] == page_num) {
                    
                    output += "<span style=\"text-decoration: underline\">";
                } else {
                    
                    output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                    "&pagenum=" + for_sale_items["pages"][i]["page_number"]) + "\">";
                }
                
                output += for_sale_items["pages"][i]["page_number"];
                
                if (for_sale_items["pages"][i]["page_number"] == page_num) {
                    
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
                    
                    if (for_sale_items["pages"][i]["page_number"] == page_num) {
                        
                        output += "<span style=\"text-decoration: underline\">";
                    } else {
                        
                        output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                        "&pagenum=" + for_sale_items["pages"][i]["page_number"]) + "\">";
                    }
                    
                    output += for_sale_items["pages"][i]["page_number"];
                    
                    if (for_sale_items["pages"][i]["page_number"] == page_num) {
                        
                        output += "</span>";
                    } else {
                        
                        output += "</a>";
                    }
                }
                
                //Show last page number.
                output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                "&pagenum=" + for_sale_items["pages"][for_sale_items["pages"].length - 1]["page_number"]) + "\">" +
                        for_sale_items["pages"][for_sale_items["pages"].length - 1]["page_number"] + "</a>";
            } else {
                
                //If page browsed page number is within the last five pages, show the last five pages.
                if (page_num > (for_sale_items["pages"].length - 5)) {
                    
                    last_several_pages = for_sale_items["pages"].length - 5;
                    
                    for (var i = last_several_pages; i < for_sale_items["pages"].length; i++) {
                        
                        //Last several pages should follow right after the ellipses.
                        if (i == last_several_pages) {
                            
                            output += "&nbsp;&nbsp;...&nbsp;&nbsp;";
                            
                            //Focused page
                            if (for_sale_items["pages"][i]["page_number"] == page_num) {
                                
                                output += "<span style=\"text-decoration: underline\">";
                            } else {
                                
                                output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                                "&pagenum=" + for_sale_items["pages"][i]["page_number"]) + "\">";
                            }
                            
                            output += for_sale_items["pages"][i]["page_number"];
                            
                            //Focused page
                            if (for_sale_items["pages"][i]["page_number"] == page_num) {
                                
                                output += "</span>";
                            } else {
                                
                                output += "</a>";
                            }
                        } else {
                            
                            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                            
                            //Focused page
                            if (for_sale_items["pages"][i]["page_number"] == page_num) {
                                
                                output += "<span style=\"text-decoration: underline\">";
                            } else {
                                
                                output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                                "&pagenum=" + for_sale_items["pages"][i]["page_number"]) + "\">";
                            }
                            
                            output += for_sale_items["pages"][i]["page_number"];
                            
                            //Focused page
                            if (for_sale_items["pages"][i]["page_number"] == page_num) {
                                
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
                            "&pagenum=" + for_sale_items["pages"][i - 1]["page_number"]) + "\">" + for_sale_items["pages"][i - 1]["page_number"] + "</a>";
                        } else {
                            
                            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                            "&pagenum=" + for_sale_items["pages"][i - 1]["page_number"]) + "\">" + for_sale_items["pages"][i - 1]["page_number"] + "</a>";
                        }
                    }
                    
                    output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style=\"text-decoration: underline\">" + page_num + "</span>";
                    
                    for (var i = page_num; i < two_pages_after; i++) {
                        
                        //Show two page numbers after focused page.
                        output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                        "&pagenum=" + for_sale_items["pages"][i]["page_number"]) + "\">" + for_sale_items["pages"][i]["page_number"] + "</a>";
                    }
                    
                    output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                    "&pagenum=" + for_sale_items["pages"].length) + "\">" + for_sale_items["pages"].length + "</a>";
                }
            }
        }
        
        if (page_num != for_sale_items["pages"].length) {
            
            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + (page_num + 1)) + "\">Next >></a>";
        }
        
        output += "</p></span>";
    }
    
    return output;
}

//Allow users to hit enter key while using search form
document.addEventListener("keyup", function(event) {
    
    if (event.keyCode === 13) {
        
        if (document.activeElement.className.match("unresponsive_keywords")) {
            
            event.preventDefault();
            unresponsive_search();
        } else if (document.activeElement.className.match("responsive_keywords")) {
            
            event.preventDefault();
            responsive_search();
        }
    }
});

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
    
    item_id = items_to_be_added["sale_items"][index]["row_id"];
    item_name = items_to_be_added["sale_items"][index]["item"].replace(/&apos;/g,'\'');
    thumbnail = items_to_be_added["sale_items"][index]["thumbnail"];
    category = items_to_be_added["sale_items"][index]["item_category"];
    description = items_to_be_added["sale_items"][index]["description"].replace(/&apos;/g,'\'');
    price = items_to_be_added["sale_items"][index]["price"];
    
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
    
    quantity = 1;
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
    
    document.querySelector("body").style.overflowY = "auto";
    document.getElementsByClassName("add_to_cart_background")[0].style.overflowY = "hidden";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_add_to_cart_form() {
    
    window.location = document.location.href.replace("#", "");
}

//console.clear();