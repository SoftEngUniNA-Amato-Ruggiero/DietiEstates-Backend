package it.softengunina.dietiestatesbackend.model.users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for a User that has made an authenticated request to the system.
 */
@Entity
@Table(name = "users")
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "role")
    @EqualsAndHashCode.Exclude
    private final Set<String> roles = new HashSet<>();

    public BaseUser(@NonNull String username,
                    @NonNull String cognitoSub) {
        this.username = username;
        this.cognitoSub = cognitoSub;
    }

    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public boolean addRole(@NonNull String role) {
        return roles.add(role);
    }

    public boolean removeRole(@NonNull String role) {
        return roles.remove(role);
    }

    public boolean hasRole(@NonNull String role) {
        return roles.contains(role);
    }
}
