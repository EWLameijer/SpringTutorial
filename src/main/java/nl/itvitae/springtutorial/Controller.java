package nl.itvitae.springtutorial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping
    public String message() {
        return "Welcome to this beautiful REST controller!";
    }
}
