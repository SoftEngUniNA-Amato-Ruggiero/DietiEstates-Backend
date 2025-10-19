package it.softengunina.dietiestatesbackend.model;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "notifications_preferences")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NotificationsPreferences implements NotificationsOperations {
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

    @Column(columnDefinition = "geometry(Point, 4326)")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Point center;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Double radius;

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

    @Override
    public void setArea(Point center, Double radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean isEmailNotificationsEnabled() {
        return this.subscriptionArn != null && !this.subscriptionArn.isEmpty();
    }
}
