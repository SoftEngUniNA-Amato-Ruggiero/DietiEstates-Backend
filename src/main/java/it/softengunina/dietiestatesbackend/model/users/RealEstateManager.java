package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.services.UserPromotionService;
import jakarta.persistence.*;
import lombok.*;

import java.util.function.Function;

@Entity
@Table(name = "real_estate_managers")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RealEstateManager extends RealEstateAgent {
    public RealEstateManager(@NonNull String username, @NonNull String cognitoSub, @NonNull RealEstateAgency agency) {
        super(username, cognitoSub, agency);
    }

    @Override
    public boolean isManager() {
        return true;
    }

    @Override
    public Function<RealEstateAgency, UserWithAgency> getPromotionToManagerFunction(UserPromotionService service) {
        throw new IllegalArgumentException("User cannot be promoted to manager.");
    }
}