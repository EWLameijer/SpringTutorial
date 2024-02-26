package nl.itvitae.springtutorial.review;

public record ReviewDto(String username, int rating, String text) {

    public static ReviewDto from(Review review) {
        return new ReviewDto(review.getUser().getUsername(), review.getRating(), review.getText());
    }
}
