package it.softengunina.dietiestatesbackend.model.notificationpreferences;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "notification_preferences")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BaseNotificationPreferences implements NotificationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @Column(columnDefinition = "geometry(Geometry, 4326)", nullable = false)
    @Getter
    @Setter
    private Point center;

    @Column(nullable = false)
    @Getter
    @Setter
    private Double radius;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @Getter
    @Setter
    private BaseUser user;

    @Builder
    public BaseNotificationPreferences(Point center, Double radius, BaseUser user) {
        this.center = center;
        this.radius = radius;
        this.user = user;
    }
}
