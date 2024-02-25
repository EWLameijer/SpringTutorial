package nl.itvitae.springtutorial;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private int rating;

    Movie() {}

    public Movie(String title, int rating) {
        this.title = title;
        this.rating = rating;
    }
}