package nl.itvitae.springtutorial.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String password;

    User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
