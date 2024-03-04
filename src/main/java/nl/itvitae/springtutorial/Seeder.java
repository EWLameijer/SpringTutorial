package nl.itvitae.springtutorial;

import lombok.RequiredArgsConstructor;
import nl.itvitae.springtutorial.authority.Authority;
import nl.itvitae.springtutorial.authority.AuthorityRepository;
import nl.itvitae.springtutorial.movie.Movie;
import nl.itvitae.springtutorial.movie.MovieRepository;
import nl.itvitae.springtutorial.review.Review;
import nl.itvitae.springtutorial.review.ReviewRepository;
import nl.itvitae.springtutorial.user.User;
import nl.itvitae.springtutorial.user.UserRepository;
import nl.itvitae.springtutorial.user.UserRole;
import nl.itvitae.springtutorial.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final MovieRepository movieRepository;

    private final UserService userService;

    private final ReviewRepository reviewRepository;

    @Override
    public void run(String... args) {
        if (movieRepository.count() == 0) {
            var up = new Movie("Up");
            var citizenKane = new Movie("Citizen Kane");
            var theGrandBudapest = new Movie("The Grand Budapest Hotel");
            var starWars = new Movie("Star Wars");
            movieRepository.saveAll(List.of(up, citizenKane, theGrandBudapest, starWars));

            var me = userService.save("TheWub", "secret", UserRole.ADMIN);
            var testUser = userService.save("nn", "password", UserRole.USER);

            var myCitizenKaneReview = new Review(citizenKane, me, 2, "famous, but disappointing");
            var myUpReview = new Review(up, me, 5, "touching, surprising, and funny");
            var testGrandBudapestReview = new Review(theGrandBudapest, testUser, 3, "sometimes funny, but mostly mwuh");
            var testUpReview = new Review(up, testUser, 1, "I don't like cartoons");
            reviewRepository.saveAll(List.of(myCitizenKaneReview, myUpReview, testGrandBudapestReview, testUpReview));
        }
    }
}