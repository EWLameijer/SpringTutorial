package nl.itvitae.springtutorial.review;

public record ReviewInputDto(Long movieId, Integer rating, String text) {
}
