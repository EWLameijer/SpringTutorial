package nl.itvitae.springtutorial.user;

import lombok.RequiredArgsConstructor;
import nl.itvitae.springtutorial.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
        return userRepository.findById(id).map(UserDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegistrationDto userRegistrationDto, UriComponentsBuilder ucb) {
        var username = userRegistrationDto.username().trim();
        if (username.isEmpty()) throw new BadRequestException("username should not be blank");

        var password = userRegistrationDto.password().trim();
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
