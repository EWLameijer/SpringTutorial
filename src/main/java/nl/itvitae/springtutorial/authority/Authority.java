package nl.itvitae.springtutorial.authority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    private String username;

    private String authority;
}
