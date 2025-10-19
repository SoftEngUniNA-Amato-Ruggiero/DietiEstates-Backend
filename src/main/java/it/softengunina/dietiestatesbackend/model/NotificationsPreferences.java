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
    @Setter(AccessLevel.PROTECTED)
    private boolean emailNotificationsEnabled = true;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean notificationsForSaleEnabled = true;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private boolean notificationsForRentEnabled = true;

    public NotificationsPreferences(BaseUser user) {
        this.user = user;
    }

    @Override
    public void setArea(Point center, Double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void enableEmailNotifications() {
        this.emailNotificationsEnabled = true;
    }

    @Override
    public void disableEmailNotifications() {
        this.emailNotificationsEnabled = false;
    }

    @Override
    public void enableNotificationsForSale() {
        this.notificationsForSaleEnabled = true;
    }

    @Override
    public void disableNotificationsForSale() {
        this.notificationsForSaleEnabled = false;
    }

    @Override
    public void enableNotificationsForRent() {
        this.notificationsForRentEnabled = true;
    }

    @Override
    public void disableNotificationsForRent() {
        this.notificationsForRentEnabled = false;
    }
}
