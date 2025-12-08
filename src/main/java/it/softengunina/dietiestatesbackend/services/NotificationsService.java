package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.NotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;

import java.util.Map;

public interface NotificationsService {
    void publishMessageToTopic(String message, Map<String, String> attributes);
    void enableEmailSubscription(NotificationsPreferences prefs) throws NotificationsPreferencesUpdateException;
    void disableEmailSubscription(NotificationsPreferences prefs) throws NotificationsPreferencesUpdateException;
    void toggleEmailSubscription(NotificationsPreferences prefs) throws NotificationsPreferencesUpdateException;
    void applyFilterPolicy(NotificationsPreferences prefs) throws NotificationsPreferencesUpdateException;
}
