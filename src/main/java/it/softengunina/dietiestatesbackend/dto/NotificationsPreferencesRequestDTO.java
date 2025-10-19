package it.softengunina.dietiestatesbackend.dto;

import lombok.Data;

@Data
public class NotificationsPreferencesRequestDTO {
    private Double centerLat;
    private Double centerLng;
    private Double radius;
    private boolean emailNotificationsEnabled;
    private boolean notificationsForSaleEnabled;
    private boolean notificationsForRentEnabled;
}
