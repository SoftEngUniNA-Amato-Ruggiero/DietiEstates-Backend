package it.softengunina.dietiestatesbackend.model.users;
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
    public RealEstateAgent(@NonNull BusinessUser businessUser) {
        super(businessUser);
    }
}
