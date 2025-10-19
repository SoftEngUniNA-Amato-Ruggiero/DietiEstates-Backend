package it.softengunina.dietiestatesbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.NotificationsPreferencesRequestDTO;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.NotificationsPreferencesRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PutMapping
    public NotificationsPreferences updatePreferences(@RequestAttribute(name = "user") BaseUser user,
                                                      @RequestBody NotificationsPreferencesRequestDTO req) {
        NotificationsPreferences prefs = repository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Notification preferences not found for user: " + user.getUsername()));

        prefs.setCity(req.getCity());
        prefs.setNotificationsForSaleEnabled(req.isNotificationsForSaleEnabled());
        prefs.setNotificationsForRentEnabled(req.isNotificationsForRentEnabled());

        if (req.isEmailNotificationsEnabled() != prefs.isEmailNotificationsEnabled()) {
            try {
                notificationsService.toggleEmailSubscription(prefs);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to toggle email subscription: " + e.getMessage());
            }
        }

        return repository.save(prefs);
    }
}
