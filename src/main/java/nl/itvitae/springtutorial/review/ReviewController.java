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

    @PostMapping("{movieId}")
    public ResponseEntity<ReviewDto> postReview(
            @RequestBody Review review,
            @PathVariable long movieId,
            Principal principal,
            UriComponentsBuilder ucb) {
        var movie = movieRepository.findById(movieId).orElseThrow(() -> new BadRequestException("Movie with id '" + movieId + "' not found!"));
        var user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("User does not seem to exist!"));
        var possiblyExistingReview = reviewRepository.findByUserAndMovie(user, movie);
        if (possiblyExistingReview.isPresent())
            throw new BadRequestException("This user has already written a review for this movie.");
        var rating = review.getRating();
        if (rating > 5 || rating < 1) throw new BadRequestException("Rating should be at least 1 and at most 5!");
        var completeReview = new Review(movie, user, review.getRating(), review.getText());
        reviewRepository.save(completeReview);
        URI locationOfNewReview = ucb.path("reviews/{id}").buildAndExpand(completeReview.getId()).toUri();
        return ResponseEntity.created(locationOfNewReview).body(ReviewDto.from(completeReview));
    }
}
