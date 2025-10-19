package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import it.softengunina.dietiestatesbackend.repository.NotificationsPreferencesRepository;
import it.softengunina.dietiestatesbackend.services.NotificationsServiceImpl;
import jakarta.persistence.PostPersist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserListener {
    NotificationsServiceImpl notificationsService;
    NotificationsPreferencesRepository notificationsPreferencesRepository;

    public UserListener(@Lazy NotificationsServiceImpl notificationsService,
                        @Lazy NotificationsPreferencesRepository notificationsPreferencesRepository) {
        this.notificationsService = notificationsService;
        this.notificationsPreferencesRepository = notificationsPreferencesRepository;
    }

    @PostPersist
    public void afterUserPersist(BaseUser user) {
        log.info("User created: {}", user.getUsername());
        NotificationsPreferences prefs = this.notificationsPreferencesRepository.save(new NotificationsPreferences(user));
        notificationsService.toggleEmailSubscription(prefs);
    }

}
