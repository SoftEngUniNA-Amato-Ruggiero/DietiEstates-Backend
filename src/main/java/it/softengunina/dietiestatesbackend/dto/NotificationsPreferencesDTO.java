package it.softengunina.dietiestatesbackend.dto;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NotificationsPreferencesDTO {
    private String city;
    private Boolean emailNotificationsEnabled;
    private Boolean notificationsForSaleEnabled;
    private Boolean notificationsForRentEnabled;

    public NotificationsPreferencesDTO(NotificationsPreferences prefs) {
        this.city = prefs.getCity();
        this.emailNotificationsEnabled = prefs.isEmailNotificationsEnabled();
        this.notificationsForSaleEnabled = prefs.isNotificationsForSaleEnabled();
        this.notificationsForRentEnabled = prefs.isNotificationsForRentEnabled();
    }
}
