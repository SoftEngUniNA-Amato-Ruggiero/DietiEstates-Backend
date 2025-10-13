package it.softengunina.dietiestatesbackend.listeners;

import it.softengunina.dietiestatesbackend.model.users.User;
import it.softengunina.dietiestatesbackend.services.NotificationsService;
import jakarta.persistence.PostPersist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserListener {
    NotificationsService notificationsService;

    public UserListener(@Lazy NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @PostPersist
    public void afterUserPersist(User user) {
        log.info("User created: {}", user.getUsername());
        this.notificationsService.subscribeUserToEmailNotifications(user.getUsername());
        log.info("User {} subscribed to email notifications", user.getUsername());
    }
}
