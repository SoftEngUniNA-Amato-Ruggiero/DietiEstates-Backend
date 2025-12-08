package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.services.NotificationsService;
import jakarta.persistence.PreUpdate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class NotificationsPreferencesListener {
    NotificationsService notificationsService;

    public NotificationsPreferencesListener(@Lazy NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @PreUpdate
    public void updateNotificationsPreferences(NotificationsPreferences prefs) {
        this.notificationsService.applyFilterPolicy(prefs);
    }
}
