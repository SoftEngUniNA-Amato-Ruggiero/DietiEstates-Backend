package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
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

    @Builder
    public RealEstateManager(@NonNull String username,
                             @NonNull String cognitoSub,
                             @NonNull RealEstateAgency agency) {
        super(username, cognitoSub, agency);
    }

    @Override
    public String getSpecificRoleName() {
        return this.getClass().getSimpleName();
    }
}