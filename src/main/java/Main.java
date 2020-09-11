import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        Object[] sources = new Object[28];
        
        sources[0] = Directory_Hider.class;
        sources[1] = apps.Add_To_Cart.class;
        sources[2] = apps.Admin_Add_Item_For_Sale.class;
        sources[3] = apps.Admin_Add_Sale_Item_Pictures.class;
        sources[4] = apps.Admin_Change_For_Sale_Item_Categories.class;
        sources[5] = apps.Admin_Change_Item_For_Sale.class;
        sources[6] = apps.Admin_Delete_Item_For_Sale.class;
        sources[7] = apps.Admin_Delete_Receipts.class;
        sources[8] = apps.Admin_Delete_Sale_Item_Pictures.class;
        sources[9] = apps.Admin_Extract_Receipts.class;
        sources[10] = apps.Admin_For_Sale_Items_Interface.class;
        sources[11] = apps.Admin_Product_Details_Interface.class;
        sources[12] = apps.Change_Cart.class;
        sources[13] = apps.Change_Review.class;
        sources[14] = apps.Clear_All_Carts.class;
        sources[15] = apps.Create_Receipt.class;
        sources[16] = apps.Create_Review.class;
        sources[17] = apps.Delete_From_Cart.class;
        sources[18] = apps.Delete_Review.class;
        sources[19] = apps.Enable_Review_Deletion.class;
        sources[20] = apps.Enable_Review_Editing.class;
        sources[21] = apps.For_Sale_Items_Interface.class;
        sources[22] = apps.Make_Review_Public.class;
        sources[23] = apps.Product_Details_Interface.class;
        sources[24] = apps.Product_Reviews_Interface.class;
        sources[25] = apps.Search_Shopping_Cart.class;
        sources[26] = apps.Send_Review_Change_Email_Request.class;
        sources[27] = apps.Send_Review_Deletion_Email_Request.class;
		
        SpringApplication.run(sources, args);
    }
}
