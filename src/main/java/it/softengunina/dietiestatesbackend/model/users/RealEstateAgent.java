package it.softengunina.dietiestatesbackend.model.users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Class for a Real Estate Agent.
 */
@Entity
@Table(name = "real_estate_agents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class RealEstateAgent implements UserWithAgency {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    @Delegate(types = UserWithAgency.class)
    private BusinessUser businessUser;

    public RealEstateAgent(@NonNull BusinessUser businessUser) {
        this.businessUser = businessUser;
    }
}
