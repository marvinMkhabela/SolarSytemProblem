package za.co.discovery.assignment.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {

    public RootController() {

    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

}
