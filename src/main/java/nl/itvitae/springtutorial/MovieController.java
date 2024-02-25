package nl.itvitae.springtutorial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    record Movie(String title, int rating) {}

    @GetMapping
    public Movie getMovie() {
        return new Movie("Up", 5);
    }
}
