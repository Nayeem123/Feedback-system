package feedback_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AdminController {
    @GetMapping("/health")
    public String test(){
        return " APP Running ... " + new Date();
    }

}
