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
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final MovieRepository movieRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {
        if (movieRepository.count() == 0) {
            var up = new Movie("Up");
            var citizenKane = new Movie("Citizen Kane");
            var theGrandBudapest = new Movie("The Grand Budapest Hotel");
            var starWars = new Movie("Star Wars");
            movieRepository.saveAll(List.of(up, citizenKane, theGrandBudapest, starWars));

            var me = new User("TheWub", passwordEncoder.encode("secret"));
            var testUser = new User("nn", passwordEncoder.encode("password"));
            userRepository.saveAll(List.of(me, testUser));

            var myRole = new Authority("TheWub", "ROLE_ADMIN");
            var testRole = new Authority("nn", "ROLE_USER");
            authorityRepository.saveAll(List.of(myRole, testRole));

            var myCitizenKaneReview = new Review(citizenKane, me, 2, "famous, but disappointing");
            var myUpReview = new Review(up, me, 5, "touching, surprising, and funny");
            var testGrandBudapestReview = new Review(theGrandBudapest, testUser, 3, "sometimes funny, but mostly mwuh");
            var testUpReview = new Review(up, testUser, 1, "I don't like cartoons");
            reviewRepository.saveAll(List.of(myCitizenKaneReview, myUpReview, testGrandBudapestReview, testUpReview));
        }
    }
}