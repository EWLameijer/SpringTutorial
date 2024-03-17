package nl.itvitae.springtutorial.review;

public record ReviewInputDto(String username, Long movieId, int rating, String text) {
}
