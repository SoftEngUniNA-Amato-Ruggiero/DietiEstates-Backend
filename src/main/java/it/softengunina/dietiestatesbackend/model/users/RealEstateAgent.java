package it.softengunina.dietiestatesbackend.model.users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.dietiestatesbackend.exceptions.ImpossiblePromotionException;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategy;
import jakarta.persistence.*;
import lombok.*;

import java.util.function.Function;

@Entity
@Table(name = "real_estate_agents")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RealEstateAgent extends BaseUser implements UserWithAgency {
    /** With CascadeType.REMOVE, when the RealEstateAgency is deleted, all its RealEstateAgents are deleted too
     * But so are the BaseUsers corresponding to the RealEstateAgents, because of how the ORM works
     * So when an agency is deleted, the agents will be considered new users on their next login
     * And they will be assigned a new id
     * So we will need to not use CascadeType.REMOVE and handle the deletion of an agency manually (running the demoteToUser method)
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonBackReference
    @Getter
    @Setter
    protected RealEstateAgency agency;

    public RealEstateAgent(@NonNull String username,
                           @NonNull String cognitoSub,
                           @NonNull RealEstateAgency agency) {
        super(username, cognitoSub);
        this.agency = agency;
    }

    @Override
    public Function<UserPromotionStrategy, UserWithAgency> getPromotionToAgentFunction(@NonNull RealEstateAgency inputAgency) {
        throw new ImpossiblePromotionException("User cannot be promoted to agent.");
    }

    @Override
    public Function<UserPromotionStrategy, UserWithAgency> getPromotionToManagerFunction(@NonNull RealEstateAgency inputAgency) {
        if (inputAgency.equals(this.agency)) {
            return service -> service.promoteAgentToManager(this);
        } else {
            throw new ImpossiblePromotionException("User belongs to another agency");
        }
    }
}
