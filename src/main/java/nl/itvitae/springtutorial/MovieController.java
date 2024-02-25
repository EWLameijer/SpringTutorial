package nl.itvitae.springtutorial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    record Movie(String title, int rating) {}

    private List<Movie> movies = List.of(
            new Movie("Up", 5),
            new Movie("Citizen Kane", 2),
            new Movie("The Grand Budapest Hotel", 3));

    @GetMapping("{id}")
    public Movie getMovieById(@PathVariable int id) {
        return movies.get(id);
    }
}
