package nl.itvitae.springtutorial.movie;

import lombok.RequiredArgsConstructor;
import nl.itvitae.springtutorial.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("movies")
public class MovieController {
    private final MovieRepository movieRepository;

    @GetMapping("{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable long id) {
        return movieRepository.findById(id).map(MovieDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Iterable<MovieDto> getAll(Pageable pageable) {
        return movieRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 3),
                pageable.getSortOr(Sort.by(Sort.Direction.DESC, "title"))
        )).map(MovieDto::from);
    }

    @PostMapping
    public ResponseEntity<MovieDto> add(@RequestBody Movie movie, UriComponentsBuilder ucb) {
        if (movie.getId() != null)
            throw new BadRequestException("the body of this POST request should not contain an id value, as that is assigned by the database");
        throwAtAbsentOrAlreadyTakenTitle(movie);

        movieRepository.save(movie);
        URI locationOfNewMovie = ucb.path("{id}").buildAndExpand(movie.getId()).toUri();
        return ResponseEntity.created(locationOfNewMovie).body(MovieDto.from(movie));
    }

    private void throwAtAbsentOrAlreadyTakenTitle(Movie movie) {
        if (movie.getTitle() == null) throw new BadRequestException("A movie needs a title!");
        var title = movie.getTitle().trim();
        if (title.isEmpty()) throw new BadRequestException("A movie needs a non-blank title!");
        if (movieRepository.existsByTitleIgnoringCase(title))
            throw new BadRequestException("A movie with this title already exists");
    }

    @GetMapping("search/titles/{title}")
    public Iterable<MovieDto> findByTitle(@PathVariable String title) {
        return movieRepository.findByTitleIgnoringCaseContaining(title).stream().map(MovieDto::from).toList();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (movieRepository.findById(id).isPresent()) {
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<MovieDto> patch(@RequestBody Movie changedMovie, @PathVariable long id) {
        if (changedMovie.getId() != null)
            throw new BadRequestException("Id cannot be changed, please pass only changeable fields in the body.");
        var possibleOriginalMovie = movieRepository.findById(id);
        if (possibleOriginalMovie.isEmpty()) return ResponseEntity.notFound().build();
        var movie = possibleOriginalMovie.get();
        if (changedMovie.getTitle() == null) throw new BadRequestException("No updates requested.");
        throwAtAbsentOrAlreadyTakenTitle(changedMovie);
        var newTitle = changedMovie.getTitle().trim();
        movie.setTitle(newTitle);
        movieRepository.save(movie);
        return ResponseEntity.ok(MovieDto.from(movie));
    }
}
