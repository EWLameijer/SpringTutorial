package nl.itvitae.springtutorial.review;

import lombok.RequiredArgsConstructor;
import nl.itvitae.springtutorial.BadRequestException;
import nl.itvitae.springtutorial.movie.MovieRepository;
import nl.itvitae.springtutorial.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final MovieRepository movieRepository;

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ReviewDto> postReview(
            @RequestBody ReviewInputDto reviewInputDto,
            Principal principal,
            UriComponentsBuilder ucb) {
        var movieId = reviewInputDto.movieId();
        if (movieId == null) throw new BadRequestException("Creating a movie review requires the id of the movie.");
        var movie = movieRepository.findById(movieId).orElseThrow(() -> new BadRequestException("Movie with id '" + movieId + "' not found!"));
        var user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("User does not seem to exist!"));
        var possiblyExistingReview = reviewRepository.findByUserAndMovie(user, movie);
        if (possiblyExistingReview.isPresent())
            throw new BadRequestException("This user has already written a review for this movie.");
        var rating = reviewInputDto.rating();
        throwAtInvalidRating(rating);
        var completeReview = new Review(movie, user, rating, reviewInputDto.text());
        reviewRepository.save(completeReview);
        URI locationOfNewReview = ucb.path("reviews/{id}").buildAndExpand(completeReview.getId()).toUri();
        return ResponseEntity.created(locationOfNewReview).body(ReviewDto.from(completeReview));
    }

    private static void throwAtInvalidRating(int rating) {
        if (rating > 5 || rating < 1) throw new BadRequestException("Rating should be at least 1 and at most 5!");
    }

    @PatchMapping("{id}")
    public ResponseEntity<ReviewDto> patchReview(@RequestBody ReviewInputDto reviewInputDto, @PathVariable long id, Principal principal) {
        var possiblyExistingReview = reviewRepository.findById(id);
        if (possiblyExistingReview.isEmpty()) return ResponseEntity.notFound().build();
        var currentReview = possiblyExistingReview.get();
        if (reviewInputDto.movieId() != null)
            throw new BadRequestException("You cannot reassign a review to a different movie");
        var newText = reviewInputDto.text();
        if (newText != null) currentReview.setText(newText);
        var newRating = reviewInputDto.rating();
        if (newRating != null) {
            throwAtInvalidRating(newRating);
            currentReview.setRating(newRating);
        }
        return ResponseEntity.ok(ReviewDto.from(reviewRepository.save(currentReview)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewDto> get(@PathVariable long id) {
        return reviewRepository.findById(id).map(review -> ResponseEntity.ok(ReviewDto.from(review)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
