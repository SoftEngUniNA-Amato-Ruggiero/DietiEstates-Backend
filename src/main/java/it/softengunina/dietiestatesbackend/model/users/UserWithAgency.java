package it.softengunina.dietiestatesbackend.model.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "users_with_agency", uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "agency_id", "role_type"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class UserWithAgency implements User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BaseUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonBackReference
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private RealEstateAgency agency;

    protected UserWithAgency(@NonNull BaseUser user,
                             RealEstateAgency agency) {
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
}
