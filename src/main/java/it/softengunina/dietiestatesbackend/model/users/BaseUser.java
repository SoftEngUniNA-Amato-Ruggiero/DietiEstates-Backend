package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.Role;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private final Set<Role> roles = new HashSet<>();

    public BaseUser(@NonNull String username,
                    @NonNull String cognitoSub) {
        this.username = username;
        this.cognitoSub = cognitoSub;
    }

    @Override
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public boolean hasRole(@NonNull Role role) {
        return roles.contains(role);
    }

    @Override
    public boolean hasRoleById(@NonNull Long roleId) {
        return roles.stream().anyMatch(role -> role.getId().equals(roleId));
    }

    @Override
    public boolean hasRoleByName(@NonNull String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    @Override
    public boolean addRole(@NonNull Role role) {
        return roles.add(role);
    }

    @Override
    public boolean removeRole(@NonNull Role role) {
        return roles.remove(role);
    }
}
