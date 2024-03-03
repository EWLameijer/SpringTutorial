package nl.itvitae.springtutorial.user;

public record UserDto(String username) {
    static UserDto from(User user) {
        return new UserDto(user.getUsername());
    }
}
