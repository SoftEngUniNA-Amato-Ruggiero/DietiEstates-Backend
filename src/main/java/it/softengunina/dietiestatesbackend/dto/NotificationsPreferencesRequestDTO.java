package it.softengunina.dietiestatesbackend.dto;

import lombok.Data;

@Data
public class NotificationsPreferencesRequestDTO {
    private String city;
    private boolean emailNotificationsEnabled;
    private boolean notificationsForSaleEnabled;
    private boolean notificationsForRentEnabled;
}
