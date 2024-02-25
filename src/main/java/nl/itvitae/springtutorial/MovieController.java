package nl.itvitae.springtutorial;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<Movie> getById(@PathVariable long id) {
        Optional<Movie> possibleMovie = movieRepository.findById(id);
        return possibleMovie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public Iterable<Movie> getAll() {
        return movieRepository.findAll();
    }
}
