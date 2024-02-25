package nl.itvitae.springtutorial;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
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

    @PostMapping
    public ResponseEntity<Movie> add(@RequestBody Movie movie, UriComponentsBuilder ucb) {
        if (movie.getId() != null) return ResponseEntity.badRequest().build();
        movieRepository.save(movie);
        URI locationOfNewMovie = ucb
                .path("{id}")
                .buildAndExpand(movie.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewMovie).body(movie);
    }

    @GetMapping("search/titles/{title}")
    public Iterable<Movie> findByTitle(@PathVariable String title) {
        return movieRepository.findByTitleIgnoringCaseContaining(title);
    }

    @GetMapping("search/ratings/{rating}")
    public Iterable<Movie> findByRating(@PathVariable int rating) {
        return movieRepository.findByRating(rating);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (movieRepository.findById(id).isPresent()) {
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }
}
