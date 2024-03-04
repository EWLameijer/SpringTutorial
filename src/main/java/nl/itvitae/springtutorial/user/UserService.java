package nl.itvitae.springtutorial.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public User save(String username, String password) {
        return userRepository.save(new User(username, passwordEncoder.encode(password)));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}