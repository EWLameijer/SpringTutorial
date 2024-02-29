package nl.itvitae.springtutorial.user;

import nl.itvitae.springtutorial.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserRepository userRepository;

    record UserRegistrationDto(String username, String password) {
    }

    record UserDto(UUID id, String username) {
        static UserDto from(User user) {
            return new UserDto(user.getId(), user.getUsername());
        }
    }

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return userRepository.findById(id).map(UserDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegistrationDto userRegistrationDto, UriComponentsBuilder ucb) {
        var username = userRegistrationDto.username.trim();
        if (username.isEmpty()) throw new BadRequestException("username should not be blank");

        var password = userRegistrationDto.password.trim();
        if (password.isEmpty()) throw new BadRequestException("password should not be blank");

        var possibleUser = userRepository.findByUsername(username);
        if (possibleUser.isPresent()) throw new BadRequestException("username already exists");
        var newUser = new User(username, password);
        userRepository.save(newUser);
        URI locationOfNewUser = ucb
                .path("users/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewUser).body(UserDto.from(newUser));
    }
}
