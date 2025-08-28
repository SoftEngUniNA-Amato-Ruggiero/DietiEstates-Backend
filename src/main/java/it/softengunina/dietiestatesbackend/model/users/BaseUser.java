package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import it.softengunina.dietiestatesbackend.strategy.UserPromotionStrategy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.function.Function;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class BaseUser implements User {
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

    public BaseUser(@NonNull String username, @NonNull String cognitoSub) {
        this.username = username;
        this.cognitoSub = cognitoSub;
    }

    @Override
    public RealEstateAgency getAgency() {
        return null;
    }

    @Override
    public String getRole() {
        return this.getClass().getSimpleName();
    }

    public Function<UserPromotionStrategy, UserWithAgency> getPromotionToAgentFunction(RealEstateAgency agency) {
        return service  -> service.promoteUserToAgent(this, agency);
    }

    public Function<UserPromotionStrategy, UserWithAgency> getPromotionToManagerFunction(RealEstateAgency agency) {
        return service -> {
            UserWithAgency agent = getPromotionToAgentFunction(agency).apply(service);
            return service.promoteAgentToManager(agent);
        };
    }
}
