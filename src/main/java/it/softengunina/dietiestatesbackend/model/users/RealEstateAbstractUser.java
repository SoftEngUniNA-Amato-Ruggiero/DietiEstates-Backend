package it.softengunina.dietiestatesbackend.model.users;

import it.softengunina.dietiestatesbackend.listeners.UserRoleListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@MappedSuperclass
@EntityListeners(UserRoleListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public abstract class RealEstateAbstractUser implements UserWithAgency, UserWithSpecificRole {
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

    protected RealEstateAbstractUser(@NonNull BusinessUser businessUser) {
        this.businessUser = businessUser;
    }
}
