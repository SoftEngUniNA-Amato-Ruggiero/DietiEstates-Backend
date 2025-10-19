package it.softengunina.dietiestatesbackend.model.notificationpreferences;

import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "notification_preferences_for_sale")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NotificationForSalePreferences extends BaseNotificationPreferences implements NotificationWithPricePreferences {
    @Getter
    @Setter
    private Double maxPrice;

    @Builder(builderMethodName = "notificationForSalePreferencesBuilder")
    public NotificationForSalePreferences(Point center, Double radius, BaseUser user, Double maxPrice) {
        super(center, radius, user);
        this.maxPrice = maxPrice;
    }
}
