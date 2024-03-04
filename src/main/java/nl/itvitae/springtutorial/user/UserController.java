package nl.itvitae.springtutorial.user;

import lombok.RequiredArgsConstructor;
import nl.itvitae.springtutorial.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username).map(UserDto::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody UserRegistrationDto userRegistrationDto, UriComponentsBuilder ucb) {
        var username = getValidInputOrThrow(userRegistrationDto.username(), "username");
        var password = getValidInputOrThrow(userRegistrationDto.password(), "password");

        var possibleUser = userService.findByUsername(username);
        if (possibleUser.isPresent()) throw new BadRequestException("username already exists");
        var newUser = userService.save(username, password, UserRole.USER);
        URI locationOfNewUser = ucb
                .path("users/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();
        return ResponseEntity.created(locationOfNewUser).body(UserDto.from(newUser));
    }

    private static String getValidInputOrThrow(String rawText, String valueName) {
        var trimmedText = rawText.trim();
        if (trimmedText.isEmpty()) throw new BadRequestException(valueName + " should not be blank");
        return trimmedText;
    }
}
