package it.softengunina.dietiestatesbackend.model;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "notifications_preferences")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class NotificationsPreferences {
    @Id
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private BaseUser user;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String subscriptionArn = null;

    @Getter
    @Setter
    private boolean notificationsForSaleEnabled = true;

    @Getter
    @Setter
    private boolean notificationsForRentEnabled = true;

    public NotificationsPreferences(BaseUser user) {
        this.user = user;
    }

    public boolean isEmailNotificationsEnabled() {
        return this.subscriptionArn != null && !this.subscriptionArn.isEmpty();
    }
}
