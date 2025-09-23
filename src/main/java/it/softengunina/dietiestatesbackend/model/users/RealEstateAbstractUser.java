package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.model.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class RealEstateAbstractUser implements UserWithAgency {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    @Delegate(types = UserWithAgency.class)
    private BusinessUser businessUser;

    @Transient
    private final Role role = new Role(this.getClass().getSimpleName());

    protected RealEstateAbstractUser(@NonNull BusinessUser businessUser) {
        this.businessUser = businessUser;
        this.addRole(role);
    }
}
