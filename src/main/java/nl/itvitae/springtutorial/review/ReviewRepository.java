package nl.itvitae.springtutorial.review;

import nl.itvitae.springtutorial.movie.Movie;
import nl.itvitae.springtutorial.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndMovie(User user, Movie movie);
}
