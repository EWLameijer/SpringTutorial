package nl.itvitae.springtutorial.review;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.itvitae.springtutorial.movie.Movie;
import nl.itvitae.springtutorial.user.User;

@Entity
@NoArgsConstructor
@Getter
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Movie movie;

    private int rating;

    private String text;

    public Review(Movie movie, User user, int rating, String text) {
        this.movie = movie;
        this.user = user;
        this.rating = rating;
        this.text = text;
    }
}
