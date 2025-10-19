package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;

import java.util.Map;

public interface NotificationsService {
    void publishMessageToTopic(String message, Map<String, String> attributes);
    void enableEmailSubscription(NotificationsPreferences prefs);
    void disableEmailSubscription(NotificationsPreferences prefs);
    void toggleEmailSubscription(NotificationsPreferences prefs);
}
