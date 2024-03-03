package nl.itvitae.springtutorial.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private String username;

    private String password;

    private boolean enabled = true;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
