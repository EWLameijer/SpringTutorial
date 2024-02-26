package nl.itvitae.springtutorial.movie;

import nl.itvitae.springtutorial.BadRequestException;
import nl.itvitae.springtutorial.review.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable long id) {
        return movieRepository.findById(id).map(MovieDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("reviews/{id}")
    public ResponseEntity<Set<Review>> getReviews(@PathVariable long id) {
        Optional<Movie> possibleMovie = movieRepository.findById(id);
        if (possibleMovie.isEmpty()) return ResponseEntity.notFound().build();
        var movie = possibleMovie.get();
        var movieReviews = movie.getReviews();
        return ResponseEntity.ok(movieReviews);
    }

    @GetMapping
    public Iterable<Movie> getAll(Pageable pageable) {
        return movieRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 3),
                pageable.getSortOr(Sort.by(Sort.Direction.DESC, "title"))
        ));
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
        if (idFromBody != null)
            throw new BadRequestException("id cannot be changed, please pass only changeable fields in the body");
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();
        var movie = possibleOriginalMovie.get();
        var newTitle = changedMovie.getTitle();
        if (newTitle != null) movie.setTitle(newTitle);
        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }
}
