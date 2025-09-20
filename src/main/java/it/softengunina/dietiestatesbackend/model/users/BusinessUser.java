package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

/**
 * Abstract class for a user who works for a Real Estate Agency.
 */
@Entity
@Table(name = "business_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class BusinessUser implements UserWithAgency {
    @Id
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @OneToOne
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BaseUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "agency_id", nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private RealEstateAgency agency;

    public BusinessUser(@NonNull BaseUser user,
                        @NonNull RealEstateAgency agency) {
        this.user = user;
        this.agency = agency;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getCognitoSub() {
        return user.getCognitoSub();
    }

    @Override
    public Set<String> getRoles() {
        return user.getRoles();
    }

    @Override
    public boolean addRole(@NonNull String role) {
        return user.addRole(role);
    }

    @Override
    public boolean removeRole(@NonNull String role) {
        return user.removeRole(role);
    }

    @Override
    public boolean hasRole(@NonNull String role) {
        return user.hasRole(role);
    }
}
