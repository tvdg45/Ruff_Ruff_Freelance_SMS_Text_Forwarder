//Author: Timothy van der Graaff
package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import views.Show_Shopping_Cart_Items_Update;

public class Control_Change_Shopping_Cart_Items extends models.Change_Shopping_Cart_Items {
    
    //global variables
    public static Connection use_connection;
    public static String[] row_id;
    public static String item;
    public static String thumbnail;
    public static String category;
    public static String description;
    public static String price;
    public static String[] quantity;
    public static String raw_time_received;
    public static String guest_session;
    public static String[] item_id;
    public static String date_received;
    public static String time_received;
    public static String add_to_cart;
    public static String change_cart;
    public static String delete_from_cart;
    public static String create_receipt;
    public static String clear_all_carts;
    public static String delete_receipts;
    public static ArrayList<Integer> search_shopping_cart_quantity_per_item;
    public static ArrayList<Integer> search_for_sales_items_inventory;
    public static ArrayList<ArrayList<String>> search_guest_sessions;
    
    public static String control_add_to_cart() {
        
        String output = "";
        
        if (add_to_cart.equals("Add to cart")) {
            
            connection = use_connection;
            
            set_row_id(item_id);
            set_item_id(item_id);
            set_guest_session(guest_session);
            
            try {
                
                search_shopping_cart_quantity_per_item = search_shopping_cart_quantity_per_item();
                search_for_sales_items_inventory = search_for_sales_items_inventory();
                
                for (int i = 0; i < search_for_sales_items_inventory.size(); i++) {
                    
                    if (Integer.valueOf(quantity[i]) < 0
                            || Integer.valueOf(quantity[i]) >
                            (search_for_sales_items_inventory.get(i) - search_shopping_cart_quantity_per_item.get(i))) {
                        
                        output += Show_Shopping_Cart_Items_Update.add_item_error_message();
                        
                        break;
                    }
                }
                
                if (!(output.equals(Show_Shopping_Cart_Items_Update.add_item_error_message()))) {
                    
                    set_item(item);
                    set_thumbnail(thumbnail);
                    set_category(category);
                    set_description(description);
                    set_price(price);
                    set_quantity(quantity);
                    set_raw_time_received(raw_time_received);
                    set_date_received(date_received);
                    set_time_received(time_received);
                    
                    if (add_to_cart().equals("success")) {
                        
                        output += Show_Shopping_Cart_Items_Update.item_added_message();
                    }
                }
            } catch (Exception e) {
                
                output += Show_Shopping_Cart_Items_Update.add_item_error_message();
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    public static String control_create_receipt() {
        
        String output = "";
        int new_inventory;
        ArrayList<Integer> new_inventory_values = new ArrayList<>();
        ArrayList<String> use_row_id = new ArrayList<>();
        ArrayList<String> use_item = new ArrayList<>();
        ArrayList<String> use_thumbnail = new ArrayList<>();
        ArrayList<String> item_category = new ArrayList<>();
        ArrayList<String> use_description = new ArrayList<>();
        ArrayList<String> use_price = new ArrayList<>();
        ArrayList<String> use_quantity = new ArrayList<>();
        String create_receipt_feedback;
        
        if (create_receipt.equals("Create receipt")) {
            
            connection = use_connection;
            
            set_row_id(item_id);
            set_item_id(item_id);
            set_guest_session(guest_session);
            set_date_received(date_received);
            set_time_received(time_received);
            
            try {
                
                search_shopping_cart_quantity_per_item = search_shopping_cart_quantity_per_item();
                search_for_sales_items_inventory = search_for_sales_items_inventory();
                
                //The following loop helps to update the item inventory.
                for (int i = 0; i < search_for_sales_items_inventory.size(); i++) {
                    
                    new_inventory = search_for_sales_items_inventory.get(i) - search_shopping_cart_quantity_per_item.get(i);
                    
                    if (new_inventory < 0) {
                        
                        new_inventory = 0;
                    }
                    
                    new_inventory_values.add(new_inventory);
                    
                    inventory_values = new_inventory_values;
                }
                
                //Update the inventory for certain items.
                if (change_item_inventories().equals("success")) {
                    
                    if (search_for_guest_shopping_cart_total_price() != 0) {
                        
                        set_total_price(search_for_guest_shopping_cart_total_price());
                        
                        //Create a receipt.
                        if (create_receipt().equals("success")) {
                            
                            if (!(search_shopping_cart_items().get(0).get(0).equals("0"))) {
                                
                                for (int i = 0; i < search_shopping_cart_items().get(0).size(); i++) {
                                    
                                    use_row_id.add(search_shopping_cart_items().get(0).get(i));
                                    use_item.add(search_shopping_cart_items().get(1).get(i));
                                    use_thumbnail.add(search_shopping_cart_items().get(2).get(i));
                                    item_category.add(search_shopping_cart_items().get(3).get(i));
                                    use_description.add(search_shopping_cart_items().get(4).get(i));
                                    use_price.add(search_shopping_cart_items().get(5).get(i));
                                    use_quantity.add(search_shopping_cart_items().get(6).get(i));
                                }
                                
                                //Add the sold items to that receipt.
                                create_receipt_feedback = add_to_receipt(use_row_id, use_item, use_thumbnail,
                                        item_category, use_description, use_price, use_quantity);
                                
                                if (create_receipt_feedback.equals("success")) {
                                    
                                    //Empty the customer's cart.
                                    if (clear_cart().equals("success")) {
                                        
                                        output += "success";
                                    } else {
                                        
                                        output += "fail";
                                    }
                                } else {
                                    
                                    output += "fail";
                                }
                            } else {
                                
                                output += "fail";
                            }
                        } else {
                            
                            output += "fail";
                        }
                    } else {
                        
                        output += "fail";
                    }
                } else {
                
                    output += "fail";
                }
            } catch (Exception e) {
                
                output += "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    public static String control_change_cart() {
        
        String output = "";
        String[] update_row_id = new String[row_id.length];
        String[] update_quantity = new String[quantity.length];
        String[] delete_row_id = new String[row_id.length];
        boolean items_changed_in_shopping_cart = false;
        boolean items_deleted_from_shopping_cart = false; 
        
        if (change_cart.equals("Update cart")) {
            
            connection = use_connection;
            
            set_row_id(item_id);
            set_item_id(item_id);
            set_guest_session(guest_session);
            
            try {
                
                search_shopping_cart_quantity_per_item = search_shopping_cart_quantity_per_item();
                search_for_sales_items_inventory = search_for_sales_items_inventory();
                
                for (int i = 0; i < search_for_sales_items_inventory.size(); i++) {
                    
                    if (Integer.valueOf(quantity[i]) < 0
                            || Integer.valueOf(quantity[i]) >
                            (search_for_sales_items_inventory.get(i) - search_shopping_cart_quantity_per_item.get(i))) {
                        
                        output += Show_Shopping_Cart_Items_Update.change_item_error_message();
                        
                        break;
                    }
                }
                
                if (!(output.equals(Show_Shopping_Cart_Items_Update.change_item_error_message()))) {
                    
                    for (int i = 0; i < quantity.length; i++) {
                        
                        if (Integer.valueOf(quantity[i]) > 0) {
                            
                            update_row_id[i] = row_id[i];
                            update_quantity[i] = quantity[i];
                        } else {
                            
                            update_row_id[i] = "0";
                            update_quantity[i] = "0";                           
                        }
                    }
                    
                    set_row_id(update_row_id);
                    set_quantity(update_quantity);
                    
                    if (change_cart_quantity().equals("success")) {
                        
                        items_changed_in_shopping_cart = true;
                    }
                    
                    for (int i = 0; i < quantity.length; i++) {
                        
                        if (Integer.valueOf(quantity[i]) == 0) {
                            
                            delete_row_id[i] = row_id[i];
                        } else {
                            
                            delete_row_id[i] = "0";
                        }
                    }
                    
                    set_row_id(delete_row_id);
                    
                    if (delete_from_cart().equals("success")) {
                        
                        items_deleted_from_shopping_cart = true;
                    }
                    
                    if (items_changed_in_shopping_cart || items_deleted_from_shopping_cart) {
                        
                        set_guest_session(guest_session);
                        
                        Show_Shopping_Cart_Items_Update.shopping_cart_total_quantity = search_for_guest_shopping_cart_total_quantity();
                        
                        output += Show_Shopping_Cart_Items_Update.item_changed_message();
                    }
                }
            } catch (Exception e) {
                
                output += Show_Shopping_Cart_Items_Update.change_item_error_message();
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    public static String control_delete_from_cart() {
        
        String output = "";
        
        if (delete_from_cart.equals("Delete item(s)")) {
            
            connection = use_connection;
            
            set_row_id(row_id);
            
            if (delete_from_cart().equals("success")) {
                
                set_guest_session(guest_session);
                
                Show_Shopping_Cart_Items_Update.shopping_cart_total_quantity = search_for_guest_shopping_cart_total_quantity();
                
                output += Show_Shopping_Cart_Items_Update.item_changed_message();
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;
    }
    
    //A cron operation will delete browsing sessions of at least one day old.
    public static String control_clear_all_carts() {
        
        String output;
        ArrayList<String> guest_sessions = new ArrayList<>();
        int number_of_minutes;
        
        if (clear_all_carts.equals("Clear all carts")) {
            
            connection = use_connection;
            
            search_guest_sessions = search_guest_sessions();
            
            try {
                
                for (int i = 0; i < search_guest_sessions.get(0).size(); i++) {

                    //The system will delete guest sessions of at least 1440 minutes, which is at least one day.
                    number_of_minutes = Integer.valueOf(raw_time_received) - Integer.valueOf(search_guest_sessions.get(1).get(i));
                    
                    if (!(search_guest_sessions.get(0).get(i).equals(""))
                            && number_of_minutes >= 1440) {
                        
                        guest_sessions.add(search_guest_sessions.get(0).get(i));
                    }
                }
                
                set_guest_sessions(guest_sessions);
                
                if (clear_carts().equals("success")) {
                    
                    if (clear_guest_sessions().equals("success")) {
                        
                        output = "success";
                    } else {
                        
                        output = "fail";
                    }
                } else {
                    
                    output = "fail";
                }
            } catch (Exception e) {
                
                output = "fail";
            }
        } else {
            
            output = "fail";
        }
        
        try {
            
            use_connection.close();
        } catch (SQLException e) {
        }
        
        return output;
    }
    
    //Guest browsing sessions are validated.
    public static String control_search_guest_session() {
        
        String output;
        
        connection = use_connection;
        
        set_guest_session(guest_session);
        
        if (search_guest_session().equals("")) {
            
            set_raw_time_received(raw_time_received);
            set_date_received(date_received);
            set_time_received(time_received);
            
            if (add_guest_session().equals("success")) {
                
                output = "success";
            } else {
                
                output = "fail";
            }
        } else {
            
            output = "success";
        }
        
        return output;
    }
    
    public static String control_search_receipts() {
        
        String output;
        
        connection = use_connection;
        
        Show_Shopping_Cart_Items_Update.receipts = search_receipts();
        
        output = Show_Shopping_Cart_Items_Update.show_receipts();
        
        return output;
    }
    
    public static String control_search_items_sold() {
        
        String output;
        
        connection = use_connection;
        
        Show_Shopping_Cart_Items_Update.items_sold = search_items_sold();
        
        output = Show_Shopping_Cart_Items_Update.show_items_sold();
        
        return output;
    }
    
    public static String control_delete_receipts() {
        
        String output = "";
        
        if (delete_receipts.equals("Delete receipts")) {
            
            connection = use_connection;
            
            if (delete_receipts().equals("success")) {
                
                if (delete_items_sold().equals("success")) {
                    
                    output = "success";
                } else {
                    
                    output = "fail";
                }
            } else {
                
                output = "fail";
            }
            
            try {
                
                use_connection.close();
            } catch (SQLException e) {
            }
        }
        
        return output;         
    }
}