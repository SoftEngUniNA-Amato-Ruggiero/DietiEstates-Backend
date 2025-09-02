package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class for a Real Estate Agent.
 */
@Entity
@Table(name = "real_estate_agents")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RealEstateAgent extends UserWithAgency {
    public RealEstateAgent(@NonNull BaseUser user,
                           RealEstateAgency agency) {
        super(user, agency);
        getUser().addRole(this.getClass().getSimpleName());
    }
}
