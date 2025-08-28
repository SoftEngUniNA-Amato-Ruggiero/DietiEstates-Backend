package it.softengunina.dietiestatesbackend.model.users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.dietiestatesbackend.exceptions.ImpossiblePromotionException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.function.Function;

@Entity
@Table(name = "real_estate_agents")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RealEstateAgent extends BaseUser implements UserWithAgency {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id")
    @JsonBackReference
    @NotNull
    @Getter
    @Setter
    protected RealEstateAgency agency;

    public RealEstateAgent(@NonNull String username, @NonNull String cognitoSub, @NonNull RealEstateAgency agency) {
        super(username, cognitoSub);
        this.agency = agency;
    }

    @Override
    public boolean isManager() {
        return false;
    }

    @Override
    public Function<UserPromotionStrategy, UserWithAgency> getPromotionToAgentFunction(RealEstateAgency inputAgency) {
        throw new ImpossiblePromotionException("User cannot be promoted to agent.");
    }

    @Override
    public Function<UserPromotionStrategy, UserWithAgency> getPromotionToManagerFunction(RealEstateAgency inputAgency) {
        if (inputAgency.equals(this.agency)) {
            return service -> service.promoteAgentToManager(this);
        } else {
            throw new ImpossiblePromotionException("User belongs to another agency");
        }
    }
}
