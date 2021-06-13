import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        Object[] sources = new Object[2];
        
        sources[0] = Directory_Hider.class;
        sources[1] = apps.Send_Email_To_Vendor.class;
		
        SpringApplication.run(sources, args);
    }
}
