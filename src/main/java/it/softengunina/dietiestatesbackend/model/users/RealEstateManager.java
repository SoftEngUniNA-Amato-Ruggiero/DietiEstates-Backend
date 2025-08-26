package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.users.commands.PromotionToManagerCommand;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;

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
    public PromotionToManagerCommand getPromotionToManagerCommand(RealEstateAgency agency) {
        throw new IllegalArgumentException("User cannot be promoted to manager.");
    }
}