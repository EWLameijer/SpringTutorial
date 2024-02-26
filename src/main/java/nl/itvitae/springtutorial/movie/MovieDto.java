package nl.itvitae.springtutorial.movie;

import nl.itvitae.springtutorial.review.Review;
import nl.itvitae.springtutorial.review.ReviewDto;

import java.util.List;
import java.util.OptionalDouble;

public record MovieDto(String title, OptionalDouble averageRating, List<ReviewDto> reviews) {
    public static MovieDto from(Movie movie) {
        OptionalDouble averageRating = movie.getReviews().stream()
                .mapToDouble(Review::getRating)
                .average();
        List<ReviewDto> reviewDtos = movie.getReviews().stream().map(ReviewDto::from).toList();
        return new MovieDto(movie.getTitle(), averageRating, reviewDtos);
    }
}