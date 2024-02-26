package nl.itvitae.springtutorial;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private int rating;

    private Set<Review> reviews = new HashSet<>();

    Movie() {
    }

    public Movie(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}