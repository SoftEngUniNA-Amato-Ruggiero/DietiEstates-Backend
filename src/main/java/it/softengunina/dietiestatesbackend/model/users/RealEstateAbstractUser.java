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

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    @Delegate(types = UserWithAgency.class)
    private BusinessUser businessUser;

    protected RealEstateAbstractUser(@NonNull BusinessUser businessUser, Role role) {
        this.businessUser = businessUser;
        this.addRole(role);
    }

    @PreRemove
    public abstract void removeRole();
}
