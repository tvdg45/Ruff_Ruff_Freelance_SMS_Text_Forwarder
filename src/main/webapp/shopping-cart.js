//Global variables for rendering HTML content
var html_code = "";
var shopping_cart_items;
var use_guest_session = "";

//Global variables for form input
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

//Refresh the page, using a node socket.
socket.on("refresh_cart_window", function(data) {

    if (data == use_guest_session) {
        
        window.location = document.location.href.replace("#", "");
    }
});

window.onload = function() {
    
    //Get guest session information.
    use_guest_session_search_shopping_cart();
}

function delete_guest_session() {
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.open("POST", domain + "/third-party/session-controls/shopping-cart-guest-session/");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("delete_session=Delete");
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
            use_guest_session = guest_session_message;
            search_shopping_cart(guest_session_message);
        } 
    };
    
    xhttp.open("POST", domain + "/third-party/session-controls/shopping-cart-guest-session/");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("check_session=Check");    
}

function search_shopping_cart(guest_session) {
    
    var output = "";
    var perpage = "";
    var page = "";
    var search_shopping_cart_message = "";
    var shopping_cart_items;
    
    perpage = $.url_params("perpage");
    page = $.url_params("pagenum");
    
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
        
        if (!(search_shopping_cart_message.indexOf("fail") >= 0)) {
            
            shopping_cart_items = JSON.parse(search_shopping_cart_message);
            
            output = "<div style=\"display: table-cell; text-align: left; width: 100%\">";
            output += "<select class=\"results_per_page\" onchange=\"search()\">";
            output += results_per_page();
            output += "</select>";
            output += "</div>";
            
            output += html_body_pc_format(shopping_cart_items);
            output += html_body_mobile_format(shopping_cart_items);
            
            if (shopping_cart_items["shopping_cart_items"].length > 0) {
                
                if (!(shopping_cart_items["shopping_cart_items"][0]["row_id"].indexOf("0") >= 0)) {
                    
                    output += html_footer_all_formats(shopping_cart_items);
                }
            }
            
            //Add to cart form
            output += "<div class=\"change_cart_background\">";
            output += "<div class=\"change_cart_form\">";
            output += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            output += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            output += "</div>";
            output += "</div>";
            
            document.getElementById("shopping_cart").innerHTML = output;
            
            paypal_transaction(shopping_cart_items, guest_session);
        } else {
            
            output = "<div style=\"text-align: left; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            output += "<label>Loading... Please wait.</label>";
            output += "</div>";
            
            document.getElementById("shopping_cart").innerHTML = output;
        }
    };
    
    xhttp.open("POST", third_party_domain + "/search-shopping-cart");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("raw_time_received=" + get_raw_minute() + "&results_per_page=" + perpage + "&page_number=" + page + "&guest_session=" + guest_session);
}

function paypal_transaction(shopping_cart_items, guest_session) {
    
    var total_price = 0.01;
    var all_items = [];
    
    for (var i = 0; i < shopping_cart_items["all_shopping_cart_items"].length; i++) {
        
        all_items.push({
            
            name: shopping_cart_items["all_shopping_cart_items"][i]["item"].replace(/&apos;/g,'\''),
            description: shopping_cart_items["all_shopping_cart_items"][i]["description"].replace(/&apos;/g,'\''),
            quantity: shopping_cart_items["all_shopping_cart_items"][i]["quantity"],
            price: shopping_cart_items["all_shopping_cart_items"][i]["price"],
            tax: "0.00",
            sku: shopping_cart_items["all_shopping_cart_items"][i]["item_id"],
            currency: "USD"
        });
    }
    
    if (shopping_cart_items["shopping_cart_price_total"] < 0.01 || shopping_cart_items["shopping_cart_items"][0]["row_id"].indexOf("0") >= 0) {
        
        document.getElementsByClassName("paypal_button_container")[0].style.visibility = "hidden";
        
        total_price = 0.01;
    } else {
        
        total_price = shopping_cart_items["shopping_cart_price_total"];
    }
    
    // Render the PayPal button
    paypal.Button.render({
        
        // Set your environment
        env: 'sandbox', // sandbox | production
        
        // Specify the style of the button
        style: {
            
            layout: 'horizontal',  // horizontal | vertical
            size:   'responsive',    // medium | large | responsive
            shape:  'rect',      // pill | rect
            color:  'blue'       // gold | blue | silver | white | black
        },
        
        // Specify allowed and disallowed funding sources
        
        // Options:
        // - paypal.FUNDING.CARD
        // - paypal.FUNDING.CREDIT
        // - paypal.FUNDING.ELV
        funding: {
            
            allowed: [
                
                paypal.FUNDING.CARD,
                paypal.FUNDING.CREDIT
            ],
            disallowed: []
        },
        
        // PayPal Client IDs - replace with your own
        // Create a PayPal app: https://developer.paypal.com/developer/applications/create
        client: {
            
            sandbox: 'ATJku4NWCB1HYjKOeGw7XmWLOoS3ZlQHE4s4GdbE9CZMCqDE4ak2VTGnTTkQ3Qzobl_jNDrGa17Ddl1F',
            production: 'AYdJXjMTIi4hRh3BwFIQUgsffREl830efirwvIM7O9f3CRvyogB_tcoUuEbWuq6MVi372ll1lU2aIlnx'
        },
        
        payment: function(data, actions) {
            
            return actions.payment.create({
                
                transactions: [{
                        
                        amount: {
                            
                            total: total_price,
                            currency: 'USD',
                            
                            details: {
                                
                                subtotal: total_price,
                                tax: '0.00',
                                shipping: '0.00',
                                handling_fee: '0.00',
                                shipping_discount: '0.00',
                                insurance: '0.00'
                            }
                        },
                        
                        description: 'The payment transaction description.',
                        
                        custom: '90048630024435',
                        
                        //invoice_number: '12345', Insert a unique invoice number
                        payment_options: {
                            
                            allowed_payment_method: 'INSTANT_FUNDING_SOURCE'
                        },
                        
                        soft_descriptor: 'ECHI5786786',
                        
                        item_list: {
                            
                            items: all_items,
                            
                            shipping_address: {
                                
                                recipient_name: 'John Doe',
                                line1: '233 S Wacker Dr.',
                                line2: '108th Floor',
                                city: 'Chicago',
                                country_code: 'US',
                                postal_code: '60606',
                                phone: '(312) 875-0066',
                                state: 'IL'
                            }
                        }
                    }],
                
                note_to_payer: 'Large orders may be packaged and shipped separately.'
            });
        },
        
        onAuthorize: function (data, actions) {
            
            return actions.payment.execute().then(function () {
                
                //Generate receipt
                create_receipt(shopping_cart_items, guest_session);
            });
        }
    }, '#paypal-button-container');
}

function create_receipt(shopping_cart_items, guest_session) {
    
    var create_receipt_message = "";
    var create_receipt_feedback = "";
    var shopping_cart_items_list = "";
    
    var xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = function() {
        
        if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
            
            create_receipt_message += "";
        }
        
        if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
            
            create_receipt_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            create_receipt_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            create_receipt_message += "</div>";
            create_receipt_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
            
            $(".change_cart_form").html(create_receipt_message);
            
            show_change_cart_form();
        }
        
        if (this.readyState == 4 && this.status == 200) {
            
            create_receipt_feedback = this.responseText;
        }
        
        if (create_receipt_feedback.indexOf("success") >= 0) {
            
            create_receipt_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
            create_receipt_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
            create_receipt_message += "</div>";
            create_receipt_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                    "Thank you for your purchase!</label></div>";
            
            $(".change_cart_form").html(create_receipt_message);
            
            show_change_cart_form();
            
            delete_guest_session();
        }
    };
    
    for (var i = 0; i < shopping_cart_items["all_shopping_cart_items"].length; i++) {
        
        shopping_cart_items_list += shopping_cart_items["all_shopping_cart_items"][i]["item_id"] + ",";
    }
    
    shopping_cart_items_list += " {}";
    
    shopping_cart_items_list = shopping_cart_items_list.replace(", {}", "");
    
    xhttp.open("POST", third_party_domain + "/create-receipt");
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("item_id=" + shopping_cart_items_list + "&guest_session=" + guest_session + "&create_receipt=Create receipt");
}

//used for formatting numbers, such as prices
function add_commas(nStr){
    
    nStr += '';
    
    var x = nStr.split('.');
    var x1 = x[0];
    var x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    
    while (rgx.test(x1)) {
        
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    
    return x1 + x2;
}

function html_body_pc_format(items_in_cart) {
    
    var output = "";
    
    output += "<div class=\"body_pc_format\"><br />";
    
    if (items_in_cart["shopping_cart_items_count"] == 0 || items_in_cart["shopping_cart_items"][0]["row_id"].indexOf("0") >= 0) {
        
        output += "<div style=\"text-align: left; width: 100%\">";
        output += "<label>Your shopping cart is empty!</label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 5px\">";
        output += "<label>Your cart (" + add_commas(items_in_cart["shopping_cart_items_count"]) + ")</label>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 15px\">";
        output += "<label><b>Total ($" + add_commas(items_in_cart["shopping_cart_price_total"]) + ")</b></label>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 10px\">";
        output += "<input type=\"button\" class=\"update_cart\" onclick=\"change_cart_unresponsive()\" value=\"Update cart\" />";
        output += "&nbsp;&nbsp;<input type=\"button\" class=\"remove_from_cart\" onclick=\"delete_from_cart_unresponsive()\" value=\"Delete item(s)\" />";
        output += "</div>"; 
        output += "<div style=\"text-align: left; width: 100%\">";
        output += "<div style=\"text-align: left; width: 100%; display: table\">";
        
        for (var i = 0; i < items_in_cart["shopping_cart_items"].length; i++) {
            
            output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
            output += "<div style=\"text-align: left; width: 1%; display: table-cell; vertical-align: top\">";
            output += "<input type=\"hidden\" name=\"each_shopping_cart_item_unresponsive\" class=\"each_shopping_cart_item_unresponsive\" value=\"" + items_in_cart["shopping_cart_items"][i]["row_id"] + "\" />";
            output += "<input type=\"hidden\" name=\"each_for_sale_item_unresponsive\" class=\"each_for_sale_item_unresponsive\" value=\"" + items_in_cart["shopping_cart_items"][i]["item_id"] + "\" />";
            output += "<input type=\"checkbox\" name=\"select_shopping_cart_item_unresponsive\" class=\"select_shopping_cart_item_unresponsive\" value=\"" + items_in_cart["shopping_cart_items"][i]["row_id"] + "\" /> ";
            output += "</div>";
            output += "<div style=\"text-align: left; padding-left: 2%; width: 18%; display: table-cell; word-wrap: break-word; padding-bottom: 20px; vertical-align: top\">";
            
            if (items_in_cart["shopping_cart_items"][i]["thumbnail"] != "" && items_in_cart["shopping_cart_items"][i]["thumbnail"] != null) {
                
                if (items_in_cart["shopping_cart_items"][i]["thumbnail"].replace(/\s/g, "").length > 0) {
                    
                    output += "<img class=\"shopping_cart_image\" src=\"" + items_in_cart["shopping_cart_items"][i]["thumbnail"] + "\" />";
                } else {
                    
                    output += "<img class=\"shopping_cart_image\" src=\"" + domain + "/images/no-file.png\" />";
                }
            } else {
                
                output += "<img class=\"shopping_cart_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
            
            output += "</div>";
            output += "<div style=\"text-align: left; padding-left: 2%; width: 18%; display: table-cell; word-wrap: break-word; padding-bottom: 20px; vertical-align: top\">";
            output += "<label><b>" + items_in_cart["shopping_cart_items"][i]["item"] + "</b></label>";
            output += "</div>";
            output += "<div style=\"text-align: left; padding-left: 2%; width: 18%; display: table-cell; word-wrap: break-word; padding-bottom: 20px; vertical-align: top\">";
            output += "<label>Quantity:</label>&nbsp;&nbsp;";
            output += "<select name=\"item_quantity_unresponsive\" class=\"item_quantity_unresponsive\">";
            output += "<option value=\"1\">1</option>";
            output += "<option value=\"2\">2</option>";
            output += "<option value=\"3\">3</option>";
            output += "<option value=\"4\">4</option>";
            output += "<option value=\"5\">5</option>";
            output += "</select>&nbsp;&nbsp;";
            output += "<label>Currently: " + items_in_cart["shopping_cart_items"][i]["quantity"] + "</label>";
            output += "</div>";
            output += "<div style=\"text-align: left; padding-left: 2%; width: 41%; display: table-cell; word-wrap: break-word; padding-bottom: 20px; vertical-align: top\">";
            output += "<label>$" + add_commas(items_in_cart["shopping_cart_items"][i]["price"]) + "</label>";
            output += "</div>";
            output += "</div>";
        }
        
        output += "</div>";
        output += "</div>";
    }
    
    output += "</div>";
    
    return output;
}

function html_body_mobile_format(items_in_cart) {
    
    var output = "";
    
    output += "<div class=\"body_mobile_format\"><br />";
    
    if (items_in_cart["shopping_cart_items_count"] == 0 || items_in_cart["shopping_cart_items"][0]["row_id"].indexOf("0") >= 0) {
        
        output += "<div style=\"text-align: left; width: 100%\">";
        output += "<label>Your shopping cart is empty!</label>";
        output += "</div>";
    } else {
        
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 5px\">";
        output += "<label>Your cart (" + add_commas(items_in_cart["shopping_cart_items_count"]) + ")</label>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 15px\">";
        output += "<label><b>Total ($" + add_commas(items_in_cart["shopping_cart_price_total"]) + ")</b></label>";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 5px\">";
        output += "<input type=\"button\" class=\"update_cart\" onclick=\"change_cart_responsive()\" value=\"Update cart\" />";
        output += "</div>";
        output += "<div style=\"text-align: left; width: 100%; padding-bottom: 10px\">";
        output += "<input type=\"button\" class=\"remove_from_cart\" onclick=\"delete_from_cart_responsive()\" value=\"Delete item(s)\" />";
        output += "</div>"; 
        output += "<div style=\"text-align: left; width: 100%\">";
        output += "<div style=\"text-align: left; width: 100%; display: table\">";
        
        for (var i = 0; i < items_in_cart["shopping_cart_items"].length; i++) {
            
            output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
            output += "<div style=\"text-align: left; width: 100%; padding-bottom: 5px; display: table-cell\">";
            output += "<input type=\"hidden\" name=\"each_shopping_cart_item_responsive\" class=\"each_shopping_cart_item_responsive\" value=\"" + items_in_cart["shopping_cart_items"][i]["row_id"] + "\" />";
            output += "<input type=\"hidden\" name=\"each_for_sale_item_responsive\" class=\"each_for_sale_item_responsive\" value=\"" + items_in_cart["shopping_cart_items"][i]["item_id"] + "\" />";
            output += "<input type=\"checkbox\" name=\"select_shopping_cart_item_responsive\" class=\"select_shopping_cart_item_responsive\" value=\"" + items_in_cart["shopping_cart_items"][i]["row_id"] + "\" /> " +
                    "<label><b>" + items_in_cart["shopping_cart_items"][i]["item"] + "</b></label>";
            output += "</div>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
            output += "<div style=\"text-align: left; width: 100%; padding-bottom: 5px; display: table-cell; word-wrap: break-word\">";
            
            if (items_in_cart["shopping_cart_items"][i]["thumbnail"] != "" && items_in_cart["shopping_cart_items"][i]["thumbnail"] != null) {
                
                if (items_in_cart["shopping_cart_items"][i]["thumbnail"].replace(/\s/g, "").length > 0) {
                    
                    output += "<img class=\"shopping_cart_image\" src=\"" + items_in_cart["shopping_cart_items"][i]["thumbnail"] + "\" />";
                } else {
                    
                    output += "<img class=\"shopping_cart_image\" src=\"" + domain + "/images/no-file.png\" />";
                }
            } else {
                
                output += "<img class=\"shopping_cart_image\" src=\"" + domain + "/images/no-file.png\" />";
            }
            
            output += "</div>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
            output += "<div style=\"text-align: left; width: 100%; padding-bottom: 5px; display: table-cell; word-wrap: break-word\">";
            output += "<label>Quantity:</label>&nbsp;&nbsp;";
            output += "<select name=\"item_quantity_responsive\" class=\"item_quantity_responsive\">";
            output += "<option value=\"1\">1</option>";
            output += "<option value=\"2\">2</option>";
            output += "<option value=\"3\">3</option>";
            output += "<option value=\"4\">4</option>";
            output += "<option value=\"5\">5</option>";
            output += "</select>&nbsp;&nbsp;";
            output += "<label>Currently: " + items_in_cart["shopping_cart_items"][i]["quantity"] + "</label>";
            output += "</div>";
            output += "</div>";
            output += "<div style=\"text-align: left; width: 100%; display: table-row\">";
            output += "<div style=\"text-align: left; width: 100%; padding-bottom: 10px; display: table-cell; word-wrap: break-word\">";
            output += "<label>$" + add_commas(items_in_cart["shopping_cart_items"][i]["price"]) + "</label>";
            output += "</div>";
            output += "</div>";
        }
        
        output += "</div>";
        output += "</div>";
    }
    
    output += "</div>";
    
    return output;
}

//Control the page number display.
function html_footer_all_formats(items_in_cart) {
    
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
                redirect = redirect.replace("pagenum=%20", "pagenum=1");
            }
            
            redirect = redirect.replace("&pagenum=", "&pagenum=1");
            redirect = redirect.replace("pagenum=", "pagenum=1");
        } else {
            
            redirect = "&pagenum=1";
            redirect = "pagenum=1";
        }
        
        //If there is no query string in URL, give it a default one.
        if (window.location.href == (domain + "/cart" + forward_slash_or_html_extension)) {
            
            window.location = domain + "/cart" + forward_slash_or_html_extension + "?perpage=10&pagenum=1";
        } else {
            
            window.location = redirect;
        }
    }
    
    if (!(page_num < 1) && !(page_num > items_in_cart["pages"].length)) {
        
        output += "<span style=\"text-align: center\"><p>";
        
        if (page_num != 1) {
            
            output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + (page_num - 1)) + "\"><< Previous</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";			
        }
        
        if (items_in_cart["pages"][0]["page_number"] != page_num) {
            
            output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + items_in_cart["pages"][0]["page_number"]) + "\">";
            output += items_in_cart["pages"][0]["page_number"];
            output += "</a>";
        } else {
            
            output += "<span style=\"text-decoration: underline\">";
            output += items_in_cart["pages"][0]["page_number"];
            output += "</span>";
        }
        
        if (items_in_cart["pages"].length <= 5) {
            
            //Format spaces for other page numbers.
            for (var i = 1; i < items_in_cart["pages"].length; i++) {
                
                output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                
                if (items_in_cart["pages"][i]["page_number"] == page_num) {
                    
                    output += "<span style=\"text-decoration: underline\">";
                } else {
                    
                    output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                    "&pagenum=" + items_in_cart["pages"][i]["page_number"]) + "\">";
                }
                
                output += items_in_cart["pages"][i]["page_number"];
                
                if (items_in_cart["pages"][i]["page_number"] == page_num) {
                    
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
                    
                    if (items_in_cart["pages"][i]["page_number"] == page_num) {
                        
                        output += "<span style=\"text-decoration: underline\">";
                    } else {
                        
                        output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                        "&pagenum=" + items_in_cart["pages"][i]["page_number"]) + "\">";
                    }
                    
                    output += items_in_cart["pages"][i]["page_number"];
                    
                    if (items_in_cart["pages"][i]["page_number"] == page_num) {
                        
                        output += "</span>";
                    } else {
                        
                        output += "</a>";
                    }
                }
                
                //Show last page number.
                output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                "&pagenum=" + items_in_cart["pages"][items_in_cart["pages"].length - 1]["page_number"]) + "\">" +
                        items_in_cart["pages"][items_in_cart["pages"].length - 1]["page_number"] + "</a>";
            } else {
                
                //If page browsed page number is within the last five pages, show the last five pages.
                if (page_num > (items_in_cart["pages"].length - 5)) {
                    
                    last_several_pages = items_in_cart["pages"].length - 5;
                    
                    for (var i = last_several_pages; i < items_in_cart["pages"].length; i++) {
                        
                        //Last several pages should follow right after the ellipses.
                        if (i == last_several_pages) {
                            
                            output += "&nbsp;&nbsp;...&nbsp;&nbsp;";
                            
                            //Focused page
                            if (items_in_cart["pages"][i]["page_number"] == page_num) {
                                
                                output += "<span style=\"text-decoration: underline\">";
                            } else {
                                
                                output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                                "&pagenum=" + items_in_cart["pages"][i]["page_number"]) + "\">";
                            }
                            
                            output += items_in_cart["pages"][i]["page_number"];
                            
                            //Focused page
                            if (items_in_cart["pages"][i]["page_number"] == page_num) {
                                
                                output += "</span>";
                            } else {
                                
                                output += "</a>";
                            }
                        } else {
                            
                            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                            
                            //Focused page
                            if (items_in_cart["pages"][i]["page_number"] == page_num) {
                                
                                output += "<span style=\"text-decoration: underline\">";
                            } else {
                                
                                output += "<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                                "&pagenum=" + items_in_cart["pages"][i]["page_number"]) + "\">";
                            }
                            
                            output += items_in_cart["pages"][i]["page_number"];
                            
                            //Focused page
                            if (items_in_cart["pages"][i]["page_number"] == page_num) {
                                
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
                            "&pagenum=" + items_in_cart["pages"][i - 1]["page_number"]) + "\">" + items_in_cart["pages"][i - 1]["page_number"] + "</a>";
                        } else {
                            
                            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                            "&pagenum=" + items_in_cart["pages"][i - 1]["page_number"]) + "\">" + items_in_cart["pages"][i - 1]["page_number"] + "</a>";
                        }
                    }
                    
                    output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style=\"text-decoration: underline\">" + page_num + "</span>";
                    
                    for (var i = page_num; i < two_pages_after; i++) {
                        
                        //Show two page numbers after focused page.
                        output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                        "&pagenum=" + items_in_cart["pages"][i]["page_number"]) + "\">" + items_in_cart["pages"][i]["page_number"] + "</a>";
                    }
                    
                    output += "&nbsp;&nbsp;...&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
                    "&pagenum=" + items_in_cart["pages"].length) + "\">" + items_in_cart["pages"].length + "</a>";
                }
            }
        }
        
        if (page_num != items_in_cart["pages"].length) {
            
            output += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"" + document.location.href.replace("&pagenum=" + page_num,
            "&pagenum=" + (page_num + 1)) + "\">Next >></a>";
        }
        
        output += "</p></span>";
    }
    
    return output;
}

//Accept form input.
function search() {
    
    var navigation_url = "";
    
    form_results_per_page = $(".results_per_page").val();
    
    if (form_results_per_page == "Select results per page") {
        
        form_results_per_page = "10";
    }
    
    navigation_url += domain + "/cart" + forward_slash_or_html_extension + "?";
    navigation_url += "perpage=" + form_results_per_page;
    navigation_url += "&pagenum=1";
    
    window.location = navigation_url;
}

function change_cart_unresponsive() {
    
    var for_sale_items;
    var all_items;
    var each_item;
    var all_for_sale_items = "";
    var all_selected_items = "";
    var each_selected_item = "";
    var number_of_for_sale_items = 0;
    var number_of_all_items = 0;
    var number_of_selected_items = 0;
    var change_cart_feedback = "";
    var change_cart_message = "";
    
    var xhttp = new XMLHttpRequest();
    
    for_sale_items = document.getElementsByName("each_for_sale_item_unresponsive").length;
    all_items = document.getElementsByName("each_shopping_cart_item_unresponsive").length;
    each_item = document.getElementsByName("item_quantity_unresponsive").length;

    for (var i = 0; i < for_sale_items; i++) {
        
        all_for_sale_items += document.getElementsByName("each_for_sale_item_unresponsive")[i].value + ",";
        number_of_for_sale_items++;
    }
    
    all_for_sale_items += "{}";
    
    if (number_of_for_sale_items > 0) {
       
        all_for_sale_items = all_for_sale_items.replace(/,{}/g, "");
    } else {
        
        all_for_sale_items = all_for_sale_items.replace(/{}/g, "");
    }
    
    for (var i = 0; i < all_items; i++) {
        
        all_selected_items += document.getElementsByName("each_shopping_cart_item_unresponsive")[i].value + ",";
        number_of_all_items++;
    }
    
    all_selected_items += "{}";
    
    if (number_of_all_items > 0) {
       
        all_selected_items = all_selected_items.replace(/,{}/g, "");
    } else {
        
        all_selected_items = all_selected_items.replace(/{}/g, "");
    }
    
    for (var i = 0; i < each_item; i++) {
        
        each_selected_item += document.getElementsByName("item_quantity_unresponsive")[i].value + ",";
        number_of_selected_items++;
    }
    
    each_selected_item += "{}";
    
    if (number_of_selected_items > 0) {
       
        each_selected_item = each_selected_item.replace(/,{}/g, "");
        
        xhttp.onreadystatechange = function() {
            
            if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
                
                change_cart_message = "";
            }
            
            if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
                
                change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                change_cart_message += "</div>";
                change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".change_cart_form").html(change_cart_message);
                
                show_change_cart_form();
            }
            
            if (this.readyState == 4 && this.status == 200) {
                
                change_cart_feedback = this.responseText;
                
                if (change_cart_feedback.indexOf("updated - still has items") >= 0) {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";                     
                } else if (change_cart_feedback.indexOf("updated - shopping cart empty") >= 0) {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";
                    
                    delete_guest_session();
                } else if (change_cart_feedback.indexOf("problem changing item") >= 0) {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "You are attempting to add more items to your cart than the number of items currently available.  Try again.</label></div>";
                } else {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                }
                
                $(".change_cart_form").html(change_cart_message);
                
                show_change_cart_form();
            }
        };
        
        xhttp.open("POST", third_party_domain + "/change-cart");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("row_id=" + all_selected_items + "&quantity=" + each_selected_item + "&guest_session=" + use_guest_session + "&item_id=" + all_for_sale_items + "&change_cart=Update cart");
    } else {
        
        each_selected_item = each_selected_item.replace(/{}/g, "");
        
        change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
        change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
        change_cart_message += "</div>";
        change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                "Your cart was updated!</label></div>";
        
        $(".change_cart_form").html(change_cart_message);
        
        show_change_cart_form();
    }
    
    number_of_all_items = 0;
    number_of_selected_items = 0;
    all_selected_items = "";
    each_selected_item = "";
}

function change_cart_responsive() {
    
    var for_sale_items;
    var all_items;
    var each_item;
    var all_for_sale_items = "";
    var all_selected_items = "";
    var each_selected_item = "";
    var number_of_for_sale_items = 0;
    var number_of_all_items = 0;
    var number_of_selected_items = 0;
    var change_cart_feedback = "";
    var change_cart_message = "";
    
    var xhttp = new XMLHttpRequest();
    
    for_sale_items = document.getElementsByName("each_for_sale_item_responsive").length;
    all_items = document.getElementsByName("each_shopping_cart_item_responsive").length;
    each_item = document.getElementsByName("item_quantity_responsive").length;

    for (var i = 0; i < for_sale_items; i++) {
        
        all_for_sale_items += document.getElementsByName("each_for_sale_item_responsive")[i].value + ",";
        number_of_for_sale_items++;
    }
    
    all_for_sale_items += "{}";
    
    if (number_of_for_sale_items > 0) {
       
        all_for_sale_items = all_for_sale_items.replace(/,{}/g, "");
    } else {
        
        all_for_sale_items = all_for_sale_items.replace(/{}/g, "");
    }
    
    for (var i = 0; i < all_items; i++) {
        
        all_selected_items += document.getElementsByName("each_shopping_cart_item_responsive")[i].value + ",";
        number_of_all_items++;
    }
    
    all_selected_items += "{}";
    
    if (number_of_all_items > 0) {
       
        all_selected_items = all_selected_items.replace(/,{}/g, "");
    } else {
        
        all_selected_items = all_selected_items.replace(/{}/g, "");
    }
    
    for (var i = 0; i < each_item; i++) {
        
        each_selected_item += document.getElementsByName("item_quantity_responsive")[i].value + ",";
        number_of_selected_items++;
    }
    
    each_selected_item += "{}";
    
    if (number_of_selected_items > 0) {
       
        each_selected_item = each_selected_item.replace(/,{}/g, "");
        
        xhttp.onreadystatechange = function() {
            
            if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
                
                change_cart_message = "";
            }
            
            if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
                
                change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                change_cart_message += "</div>";
                change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".change_cart_form").html(change_cart_message);
                
                show_change_cart_form();
            }
            
            if (this.readyState == 4 && this.status == 200) {
                
                change_cart_feedback = this.responseText;
                
                if (change_cart_feedback.indexOf("updated - still has items") >= 0) {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";                     
                } else if (change_cart_feedback.indexOf("updated - shopping cart empty") >= 0) {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";
                    
                    delete_guest_session();
                } else if (change_cart_feedback.indexOf("problem changing item") >= 0) {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "You are attempting to add more items to your cart than the number of items currently available.  Try again.</label></div>";
                } else {
                    
                    change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    change_cart_message += "</div>";
                    change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                }
                
                $(".change_cart_form").html(change_cart_message);
                
                show_change_cart_form();
            }
        };
        
        xhttp.open("POST", third_party_domain + "/change-cart");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("row_id=" + all_selected_items + "&quantity=" + each_selected_item + "&guest_session=" + use_guest_session + "&item_id=" + all_for_sale_items + "&change_cart=Update cart");
    } else {
        
        each_selected_item = each_selected_item.replace(/{}/g, "");
        
        change_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
        change_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
        change_cart_message += "</div>";
        change_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                "Your cart was updated!</label></div>";
        
        $(".change_cart_form").html(change_cart_message);
        
        show_change_cart_form();
    }
    
    number_of_all_items = 0;
    number_of_selected_items = 0;
    all_selected_items = "";
    each_selected_item = "";
}

function delete_from_cart_unresponsive() {
    
    var all_items;
    var all_selected_items = "";
    var number_of_selected_items = 0;
    var delete_from_cart_feedback = "";
    var delete_from_cart_message = "";
    
    var xhttp = new XMLHttpRequest();
    
    all_items = document.getElementsByName("select_shopping_cart_item_unresponsive").length;
    
    for (var i = 0; i < all_items; i++) {
        
        if (document.getElementsByName("select_shopping_cart_item_unresponsive")[i].checked) {
            
            all_selected_items += document.getElementsByName("select_shopping_cart_item_unresponsive")[i].value + ",";
            number_of_selected_items++;
        }
    }
    
    all_selected_items += "{}";
    
    if (number_of_selected_items > 0) {
       
        all_selected_items = all_selected_items.replace(/,{}/g, "");
        
        xhttp.onreadystatechange = function() {
            
            if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
                
                delete_from_cart_message = "";
            }
            
            if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
                
                delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                delete_from_cart_message += "</div>";
                delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".change_cart_form").html(delete_from_cart_message);
                
                show_change_cart_form();
            }
            
            if (this.readyState == 4 && this.status == 200) {
                
                delete_from_cart_feedback = this.responseText;
                
                if (delete_from_cart_feedback.indexOf("updated - still has items") >= 0) {
                    
                    delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    delete_from_cart_message += "</div>";
                    delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";                     
                } else if (delete_from_cart_feedback.indexOf("updated - shopping cart empty") >= 0) {
                    
                    delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    delete_from_cart_message += "</div>";
                    delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";
                    
                    delete_guest_session();
                } else {
                    
                    delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    delete_from_cart_message += "</div>";
                    delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                }
                
                $(".change_cart_form").html(delete_from_cart_message);
                
                show_change_cart_form();
            }
        };
        
        xhttp.open("POST", third_party_domain + "/delete-from-cart");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("row_id=" + all_selected_items + "&delete_from_cart=Delete item(s)");
    } else {
        
        all_selected_items = all_selected_items.replace(/{}/g, "");
        
        delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
        delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
        delete_from_cart_message += "</div>";
        delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                "Please select at least one item to delete.</label></div>";
        
        $(".change_cart_form").html(delete_from_cart_message);
        
        show_change_cart_form();
    }
    
    number_of_selected_items = 0;
    all_selected_items = "";
}

function delete_from_cart_responsive() {

    var all_items;
    var all_selected_items = "";
    var number_of_selected_items = 0;
    var delete_from_cart_feedback = "";
    var delete_from_cart_message = "";
    
    var xhttp = new XMLHttpRequest();
    
    all_items = document.getElementsByName("select_shopping_cart_item_responsive").length;
    
    for (var i = 0; i < all_items; i++) {
        
        if (document.getElementsByName("select_shopping_cart_item_responsive")[i].checked) {
            
            all_selected_items += document.getElementsByName("select_shopping_cart_item_responsive")[i].value + ",";
            number_of_selected_items++;
        }
    }
    
    all_selected_items += "{}";
    
    if (number_of_selected_items > 0) {
       
        all_selected_items = all_selected_items.replace(/,{}/g, "");
        
        xhttp.onreadystatechange = function() {
            
            if (this.readyState == 0 || this.readyState == 1 || this.readyState == 2 || this.readyState == 3) {
                
                delete_from_cart_message = "";
            }
            
            if (this.readyState == 4 && (this.status == 0 || this.status == 500)) {
                
                delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                delete_from_cart_message += "</div>";
                delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                        "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                
                $(".change_cart_form").html(delete_from_cart_message);
                
                show_change_cart_form();
            }
            
            if (this.readyState == 4 && this.status == 200) {
                
                delete_from_cart_feedback = this.responseText;
                
                if (delete_from_cart_feedback.indexOf("updated - still has items") >= 0) {
                    
                    delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    delete_from_cart_message += "</div>";
                    delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";                     
                } else if (delete_from_cart_feedback.indexOf("updated - shopping cart empty") >= 0) {
                    
                    delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    delete_from_cart_message += "</div>";
                    delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "Your cart was updated!</label></div>";
                    
                    delete_guest_session();
                } else {
                    
                    delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
                    delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
                    delete_from_cart_message += "</div>";
                    delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                            "There was a problem submitting your form.  Check you internet connection, and try again.</label></div>";
                }
                
                $(".change_cart_form").html(delete_from_cart_message);
                
                show_change_cart_form();
            }
        };
        
        xhttp.open("POST", third_party_domain + "/delete-from-cart");
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("row_id=" + all_selected_items + "&delete_from_cart=Delete item(s)");
    } else {
        
        all_selected_items = all_selected_items.replace(/{}/g, "");
        
        delete_from_cart_message += "<div style=\"text-align: right; width: 98%; margin-left: 1%; margin-right: 1%; padding-top: 5px\">";
        delete_from_cart_message += "<label><span class=\"close_change_cart_form\" onclick=\"hide_change_cart_form()\" style=\"font-weight: bold; font-size: 32pt\">&times;</span></label>";
        delete_from_cart_message += "</div>";
        delete_from_cart_message += "<div style=\"text-align: left; width: 90%; margin-left: 5%; margin-right: 5%; padding-top: 5px; padding-bottom: 20px\"><label>" +
                "Please select at least one item to delete.</label></div>";
        
        $(".change_cart_form").html(delete_from_cart_message);
        
        show_change_cart_form();
    }
    
    number_of_selected_items = 0;
    all_selected_items = "";
}

function get_raw_minute() {
    
    var today_date = new Date();
    var raw_minute = parseInt((today_date.getTime() / 1000) / 60);
    
    return raw_minute;
}

//Change shopping cart
function show_change_cart_form() {
    
    $(".change_cart_background").slideDown();
    
    document.querySelector("body").style.overflowY = "hidden";
    document.getElementsByClassName("change_cart_background")[0].style.overflowY = "auto";
    document.getElementById("masthead").style.visibility = "hidden";
}

function hide_change_cart_form() {
    
    window.location = document.location.href.replace("#", "");
}
