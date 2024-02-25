package nl.itvitae.springtutorial;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {

    record Movie(String title, int rating) {}

    private List<Movie> movies = new ArrayList<>(List.of(
            new Movie("Up", 5),
            new Movie("Citizen Kane", 2),
            new Movie("The Grand Budapest Hotel", 3)));

    @GetMapping("{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        if (id>=0 && id < movies.size()) return ResponseEntity.ok(movies.get(id));
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public Iterable<Movie> getAllMovies() {
        return movies;
    }

    @PostMapping
    public void addMovie(@RequestBody Movie movie) {
        movies.add(movie);
    }
}
