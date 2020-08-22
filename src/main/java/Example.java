import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class Example {

	@RequestMapping(method = RequestMethod.GET)
    String home(@RequestParam(value = "person", defaultValue = "") String personName) {
        return "<h1>Hello " + personName + "!</h1>" ;
    }
	
	@RequestMapping(method = RequestMethod.GET)
    String home(@RequestParam(value = "thing", defaultValue = "") String thisthing) {
        return "<h1>It is " + thisthing + "!</h1>" ;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }

}
