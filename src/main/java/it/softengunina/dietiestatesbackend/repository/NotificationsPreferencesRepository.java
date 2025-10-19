package it.softengunina.dietiestatesbackend.repository;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationsPreferencesRepository extends JpaRepository<NotificationsPreferences, Long> {
    Optional<NotificationsPreferences> findByUser(BaseUser user);
    Optional<NotificationsPreferences> findByUser_Id(Long userId);
    Optional<NotificationsPreferences> findByUser_CognitoSub(String cognitoSub);
    Optional<NotificationsPreferences> findByUser_Username(String username);
}
