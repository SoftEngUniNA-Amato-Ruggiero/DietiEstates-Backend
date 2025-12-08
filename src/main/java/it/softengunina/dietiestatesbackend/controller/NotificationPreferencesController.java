package it.softengunina.dietiestatesbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.softengunina.dietiestatesbackend.dto.NotificationsPreferencesDTO;
import it.softengunina.dietiestatesbackend.exceptions.EmailNotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.exceptions.NotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.NotificationsPreferencesRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/notification-preferences")
@Slf4j
public class NotificationPreferencesController {
    private final NotificationsService notificationsService;
    private final NotificationsPreferencesRepository repository;

    public NotificationPreferencesController(NotificationsService notificationsService,
                                             NotificationsPreferencesRepository repository) {
        this.notificationsService = notificationsService;
        this.repository = repository;
    }

    /**
     * Get notification preferences for the authenticated user.
     * @param user Authenticated user
     * @param req Request body containing updated preferences
     * @return NotificationsPreferences of the user
     * @throws ResponseStatusException if preferences not found or email subscription toggle fails
     */
    @PutMapping
    public NotificationsPreferencesDTO updatePreferences(@RequestAttribute(name = "user") BaseUser user,
                                                      @RequestBody NotificationsPreferencesDTO req) {
        NotificationsPreferences prefs = repository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification preferences not found for user: " + user.getUsername()));

        log.info("received update preferences request: {}", req);

        try {
            if (req.getEmailNotificationsEnabled() != null && req.getEmailNotificationsEnabled() != prefs.isEmailNotificationsEnabled()) {
                notificationsService.toggleEmailSubscription(prefs);
            }

            if (req.getCity() != null) {
                prefs.setCity(req.getCity());
            }
            if (req.getNotificationsForSaleEnabled() != null) {
                prefs.setNotificationsForSaleEnabled(req.getNotificationsForSaleEnabled());
            }
            if (req.getNotificationsForRentEnabled() != null) {
                prefs.setNotificationsForRentEnabled(req.getNotificationsForRentEnabled());
            }
            notificationsService.applyFilterPolicy(prefs);
        } catch (EmailNotificationsPreferencesUpdateException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Please verify your email to change this setting");
        } catch (NotificationsPreferencesUpdateException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update filter policy for notifications");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to change notification preferences");
        }
        return new NotificationsPreferencesDTO(repository.save(prefs));
    }
}
