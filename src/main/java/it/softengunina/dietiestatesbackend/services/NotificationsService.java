package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;

public interface NotificationsService {
    void publishMessageToTopic(String message);
    void enableEmailSubscription(NotificationsPreferences prefs);
    void disableEmailSubscription(NotificationsPreferences prefs);
    void toggleEmailSubscription(NotificationsPreferences prefs);
}
