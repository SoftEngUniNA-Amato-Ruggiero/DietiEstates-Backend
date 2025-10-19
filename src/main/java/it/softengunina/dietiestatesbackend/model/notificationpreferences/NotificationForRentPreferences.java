package it.softengunina.dietiestatesbackend.model.notificationpreferences;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "notification_preferences_for_rent")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NotificationForRentPreferences extends BaseNotificationPreferences implements NotificationWithRentPreferences {
    @Getter
    @Setter
    private Double maxRent;

    @Builder(builderMethodName = "notificationForRentPreferencesBuilder")
    public NotificationForRentPreferences(Point center, Double radius, BaseUser user, Double maxRent) {
        super(center, radius, user);
        this.maxRent = maxRent;
    }
}
