package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class for a Real Estate Agent.
 */
@Entity
@Table(name = "real_estate_agents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RealEstateAgent extends RealEstateAbstractUser {
    @Getter
    private static final Role role = new Role(RealEstateAgent.class.getSimpleName());

    public RealEstateAgent(@NonNull BusinessUser businessUser) {
        super(businessUser, role);
    }

    @Override
    public void removeRole() {
        this.removeRole(role);
    }
}
