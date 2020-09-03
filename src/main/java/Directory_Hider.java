import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class Directory_Hider {

	@RequestMapping(method = RequestMethod.GET)
    String home() {
		
        return "";
    }
	
    public static void main(String[] args) throws Exception {
		
        SpringApplication.run(Directory_Hider.class, args);
    }
}
