package it.softengunina.dietiestatesbackend.model.users;
import it.softengunina.dietiestatesbackend.model.RealEstateAgency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

/**
 * Class for a Real Estate Agent.
 */
@Entity
@Table(name = "real_estate_agents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class RealEstateAgent implements UserWithAgency {
    @Id
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @OneToOne
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BusinessUser businessUser;

    public RealEstateAgent(@NonNull BusinessUser businessUser) {
        this.businessUser = businessUser;
        this.businessUser.addRole(this.getClass().getSimpleName());
    }

    @Override
    public String getUsername() {
        return businessUser.getUsername();
    }

    @Override
    public String getCognitoSub() {
        return businessUser.getCognitoSub();
    }

    @Override
    public Set<String> getRoles() {
        return businessUser.getRoles();
    }

    @Override
    public boolean addRole(@NonNull String role) {
        return businessUser.addRole(role);
    }

    @Override
    public boolean removeRole(@NonNull String role) {
        return businessUser.removeRole(role);
    }

    @Override
    public boolean hasRole(@NonNull String role) {
        return businessUser.hasRole(role);
    }

    @Override
    public RealEstateAgency getAgency() {
        return businessUser.getAgency();
    }

    @Override
    public BaseUser getUser() {
        return businessUser.getUser();
    }
}
