package nl.itvitae.springtutorial;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Iterable<Movie> findByTitleIgnoringCaseContaining(String title);

    Iterable<Movie> findByRating(int rating);
}
