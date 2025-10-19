package it.softengunina.dietiestatesbackend.repository.notificationpreferencesrepository;

import it.softengunina.dietiestatesbackend.model.notificationpreferences.BaseNotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseNotificationPreferencesRepository<T extends  BaseNotificationPreferences> extends JpaRepository<T, Long> {
    T findByUser_Id(Long userId);
    T findByUser_Username(String username);
    T findByUser_CognitoSub(String cognitoSub);
}
