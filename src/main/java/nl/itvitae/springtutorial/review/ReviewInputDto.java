package nl.itvitae.springtutorial.review;

public record ReviewInputDto(Long movieId, int rating, String text) {
}
