package it.softengunina.dietiestatesbackend.model.users;
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
    public RealEstateManager(@NonNull BusinessUser businessUser) {
        super(businessUser);
    }
}