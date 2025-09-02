package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class for a manager of a Real Estate Agency.
 */
@Entity
@Table(name = "real_estate_managers")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RealEstateManager extends UserWithAgency {
    public RealEstateManager(@NonNull BaseUser user,
                             @NonNull RealEstateAgency agency) {
        super(user, agency);
        getUser().addRole(this.getClass().getSimpleName());
    }
}