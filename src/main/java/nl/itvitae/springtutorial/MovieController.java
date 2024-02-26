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

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody Movie movie) {
        var id = movie.getId();
        if (id == null) return ResponseEntity.badRequest().build();
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();
        movieRepository.save(movie);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> patch(@RequestBody Movie changedMovie, @PathVariable long id) {
        var idFromBody = changedMovie.getId();
        if (idFromBody != null) {
            var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "id cannot be changed, please give only changeable fields in the body");
            return ResponseEntity.badRequest().body(problemDetail);
        }
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();
        var movie = possibleOriginalMovie.get();
        var newTitle = changedMovie.getTitle();
        if (newTitle != null) movie.setTitle(newTitle);
        var newRating = changedMovie.getRating();
        if (newRating != 0) movie.setRating(newRating);
        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }
}
