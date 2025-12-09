package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.EmailNotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.exceptions.NotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;

import java.util.Map;

public interface NotificationsService {
    void publishMessageToTopic(String message, Map<String, String> attributes);
    void enableEmailSubscription(NotificationsPreferences prefs) throws EmailNotificationsPreferencesUpdateException;
    void disableEmailSubscription(NotificationsPreferences prefs) throws EmailNotificationsPreferencesUpdateException;
    void toggleEmailSubscription(NotificationsPreferences prefs) throws EmailNotificationsPreferencesUpdateException;
    void applyFilterPolicy(NotificationsPreferences prefs) throws NotificationsPreferencesUpdateException;
}
