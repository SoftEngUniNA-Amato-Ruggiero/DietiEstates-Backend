package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
}
