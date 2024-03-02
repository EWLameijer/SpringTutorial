package nl.itvitae.springtutorial.user;

import java.util.UUID;

public record UserDto(UUID id, String username) {
    static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}
