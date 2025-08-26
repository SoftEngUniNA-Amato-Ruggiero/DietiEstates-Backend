package it.softengunina.dietiestatesbackend.model.users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import it.softengunina.dietiestatesbackend.commands.PromoteAgentToManagerCommand;
import it.softengunina.dietiestatesbackend.commands.PromotionCommand;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "real_estate_agents")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@ToString
public class RealEstateAgent extends User {
    @ManyToOne(fetch = FetchType.LAZY)
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
    public PromotionCommand<RealEstateAgent> getPromotionToAgentCommand(RealEstateAgency agency) {
        throw new IllegalArgumentException("User cannot be promoted to agent.");
    }

    @Override
    public PromotionCommand<RealEstateManager> getPromotionToManagerCommand(RealEstateAgency agency) {
        if (agency.equals(this.agency)) {
            return new PromoteAgentToManagerCommand(this);
        } else {
            throw new IllegalArgumentException("User belongs to another agency");
        }
    }
}
