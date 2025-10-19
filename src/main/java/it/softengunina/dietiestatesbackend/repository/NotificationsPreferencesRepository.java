package it.softengunina.dietiestatesbackend.repository;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationsPreferencesRepository extends JpaRepository<NotificationsPreferences, Long> {
    Optional<NotificationsPreferences> getByUser_Id(Long userId);
    Optional<NotificationsPreferences> getByUser_CognitoSub(String cognitoSub);
    Optional<NotificationsPreferences> getByUser_Username(String username);
}
