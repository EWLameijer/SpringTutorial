package nl.itvitae.springtutorial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    record Movie(String title, int rating) {}

    @GetMapping("up")
    public Movie getUp() {
        return new Movie("Up", 5);
    }

    @GetMapping("citizen-kane")
    public Movie getCitizenKane() {
        return new Movie("Citizen Kane", 2);
    }
}
