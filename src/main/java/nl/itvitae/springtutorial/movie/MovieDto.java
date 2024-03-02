package nl.itvitae.springtutorial.movie;

import nl.itvitae.springtutorial.review.ReviewDto;

import java.util.List;
import java.util.OptionalDouble;

public record MovieDto(String title, OptionalDouble averageRating, List<ReviewDto> reviews) {
    public static MovieDto from(Movie movie) {
        List<ReviewDto> reviewDtos = movie.getReviews().stream().map(ReviewDto::from).toList();
        OptionalDouble averageRating = reviewDtos.stream().mapToDouble(ReviewDto::rating).average();
        return new MovieDto(movie.getTitle(), averageRating, reviewDtos);
    }
}