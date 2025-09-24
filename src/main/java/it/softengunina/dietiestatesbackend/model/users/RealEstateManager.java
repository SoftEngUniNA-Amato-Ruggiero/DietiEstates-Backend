package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class for a manager of a Real Estate Agency.
 */
@Entity
@Table(name = "real_estate_managers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RealEstateManager extends RealEstateAbstractUser {
    @Getter
    private static final Role role = new Role(RealEstateManager.class.getSimpleName());

    public RealEstateManager(@NonNull BusinessUser businessUser) {
        super(businessUser, role);
    }

    @Override
    public void removeRole() {
        this.removeRole(role);
    }
}