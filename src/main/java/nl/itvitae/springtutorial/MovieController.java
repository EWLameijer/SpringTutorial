package nl.itvitae.springtutorial;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    public ResponseEntity<?> add(@RequestBody Movie movie, UriComponentsBuilder ucb) {
        if (movie.getId() != null) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "the body of this POST request should not contain an id value, as that is assigned by the database");
            return ResponseEntity.badRequest().body(problemDetail);
        }
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

    @PutMapping("{id}")
    public ResponseEntity<?> replace(@RequestBody Movie movie, @PathVariable long id) {
        var idFromBody = movie.getId();
        if (idFromBody != null && idFromBody != id) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "ids given by path and body are inconsistent, and the id of an item should not be changed");
            return ResponseEntity.badRequest().body(problemDetail);
        }
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();
        movie.setId(id);
        movieRepository.save(movie);
        return ResponseEntity.noContent().build();
    }
}
