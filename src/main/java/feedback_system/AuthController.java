package feedback_system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // Route for the Login Page
    @GetMapping("/login")
    public String login() {
        System.out.println("Hello");
        return "login";  // Resolves to src/main/resources/templates/login.html
    }

    // Route for the Registration Page
    @GetMapping("/register")
    public String register() {
        System.out.println("Hi");
        return "register";  // Resolves to src/main/resources/templates/register.html
    }
}
