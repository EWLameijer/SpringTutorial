package nl.itvitae.springtutorial.user;

import nl.itvitae.springtutorial.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
public class UserController {
    private final UserRepository userRepository;

    record UserRegistrationDto(String username, String password) {
    }

    record UserRegistrationResultDto(UUID id, String username) {
    }

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("users/register")
    public ResponseEntity<UserRegistrationResultDto> register(@RequestBody UserRegistrationDto userRegistrationDto, UriComponentsBuilder ucb) {
        var username = userRegistrationDto.username.trim();
        if (username.isEmpty()) throw new BadRequestException("username should not be blank");

        var password = userRegistrationDto.password.trim();
        if (password.isEmpty()) throw new BadRequestException("password should not be blank");

        var possibleUser = userRepository.findByUsername(username);
        if (possibleUser.isPresent()) throw new BadRequestException("username already exists");
        var newUser = new User(username, password);
        userRepository.save(newUser);
        URI locationOfNewUser = ucb
                .path("{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewUser).body(new UserRegistrationResultDto(newUser.getId(), newUser.getUsername()));
    }
}
