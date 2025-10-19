package it.softengunina.dietiestatesbackend.model;

import org.locationtech.jts.geom.Point;

public interface NotificationsOperations {
    void setArea(Point center, Double radius);

    void enableEmailNotifications();
    void disableEmailNotifications();

    void enableNotificationsForSale();
    void disableNotificationsForSale();

    void enableNotificationsForRent();
    void disableNotificationsForRent();
}
