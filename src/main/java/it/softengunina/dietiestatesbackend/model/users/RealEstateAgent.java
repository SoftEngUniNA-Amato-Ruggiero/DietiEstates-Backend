package it.softengunina.dietiestatesbackend.model.users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Class for a user who works for a Real Estate Agency.
 */
@Entity
@Table(name = "real_estate_agents")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class RealEstateAgent implements UserWithAgency {
    @Id
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) //save a user when saving an agent
    @OnDelete(action = OnDeleteAction.CASCADE) //delete the agent when deleting the user
    @MapsId
    @JoinColumn(name = "id", nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BaseUser user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {}) //do nothing to the agency when saving/deleting an agent
    @OnDelete(action = OnDeleteAction.CASCADE) //delete the agent when deleting the agency
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonBackReference
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private RealEstateAgency agency;

    public RealEstateAgent(@NonNull BaseUser user,
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
}
