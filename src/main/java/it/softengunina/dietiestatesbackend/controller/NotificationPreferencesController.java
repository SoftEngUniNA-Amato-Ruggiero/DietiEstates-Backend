package it.softengunina.dietiestatesbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.NotificationsPreferencesRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsService;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/notification-preferences")
public class NotificationPreferencesController {
    private final NotificationsService notificationsService;
    private final NotificationsPreferencesRepository repository;

    public NotificationPreferencesController(NotificationsService notificationsService,
                                             NotificationsPreferencesRepository repository) {
        this.notificationsService = notificationsService;
        this.repository = repository;
    }

    @PutMapping("/email/unsubscribe")
    public void unsubscribeEmail(@RequestAttribute(name = "user") BaseUser user) {
        NotificationsPreferences prefs = repository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Notification preferences not found for user: " + user.getUsername()));

        prefs.disableEmailNotifications();
        repository.save(prefs);
        notificationsService.updateEmailSubscription(prefs);
    }

    @PutMapping("/email/subscribe")
    public void subscribeEmail(@RequestAttribute(name = "user") BaseUser user) {
        NotificationsPreferences prefs = repository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Notification preferences not found for user: " + user.getUsername()));

        prefs.enableEmailNotifications();
        repository.save(prefs);
        notificationsService.updateEmailSubscription(prefs);
    }
}
