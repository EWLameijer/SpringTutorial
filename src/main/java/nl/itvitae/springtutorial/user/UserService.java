package nl.itvitae.springtutorial.user;

import lombok.RequiredArgsConstructor;
import nl.itvitae.springtutorial.authority.Authority;
import nl.itvitae.springtutorial.authority.AuthorityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    public User save(String username, String password, UserRole role) {
        authorityRepository.save(new Authority(username, role.toString()));
        return userRepository.save(new User(username, passwordEncoder.encode(password)));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}