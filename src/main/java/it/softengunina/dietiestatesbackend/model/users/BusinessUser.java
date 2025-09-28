package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Class for a Business User.
 */
@Entity
@Table(name = "business_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class BusinessUser implements UserWithAgency {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    @Delegate(types = User.class)
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

    @Builder
    public BusinessUser(@NonNull String username,
                        @NonNull String cognitoSub,
                        @NonNull RealEstateAgency agency) {
        this.user = new BaseUser(username, cognitoSub);
        this.agency = agency;
    }
}
