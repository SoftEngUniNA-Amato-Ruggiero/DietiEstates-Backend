package it.softengunina.dietiestatesbackend.model.users;

import jakarta.persistence.*;
import lombok.*;

/**
 * Class for a User that has made an authenticated request to the system.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class BaseUser implements User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String username;

    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String cognitoSub;

    public BaseUser(@NonNull String username,
                    @NonNull String cognitoSub) {
        this.username = username;
        this.cognitoSub = cognitoSub;
    }
}
