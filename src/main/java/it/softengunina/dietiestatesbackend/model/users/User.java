package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.commands.PromoteUserToAgentCommand;
import it.softengunina.dietiestatesbackend.commands.PromoteUserToManagerCommand;
import it.softengunina.dietiestatesbackend.commands.PromotionCommand;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String username;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String cognitoSub;

    public User(@NonNull String username, @NonNull String cognitoSub) {
        this.username = username;
        this.cognitoSub = cognitoSub;
    }

    public RealEstateAgency getAgency() {
        return null;
    }

    public String getRole() {
        return this.getClass().getSimpleName();
    }

    public PromotionCommand<RealEstateAgent> getPromotionToAgentCommand(RealEstateAgency agency) {
        return new PromoteUserToAgentCommand(this, agency);
    }

    public PromotionCommand<RealEstateManager> getPromotionToManagerCommand(RealEstateAgency agency) {
        return new PromoteUserToManagerCommand(this, agency);
    }
}
