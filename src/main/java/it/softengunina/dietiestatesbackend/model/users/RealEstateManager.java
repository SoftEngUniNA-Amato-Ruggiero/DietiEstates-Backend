package it.softengunina.dietiestatesbackend.model.users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Class for a manager of a Real Estate Agency.
 */
@Entity
@Table(name = "real_estate_managers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class RealEstateManager implements UserWithAgency {
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
    @Delegate(types = UserWithAgency.class)
    private BusinessUser businessUser;

    public RealEstateManager(@NonNull BusinessUser businessUser) {
        this.businessUser = businessUser;
        this.businessUser.addRole(this.getClass().getSimpleName());
    }
}