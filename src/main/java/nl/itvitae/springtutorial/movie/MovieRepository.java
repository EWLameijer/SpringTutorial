package nl.itvitae.springtutorial.movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Iterable<Movie> findByTitleIgnoringCaseContaining(String title);
}
