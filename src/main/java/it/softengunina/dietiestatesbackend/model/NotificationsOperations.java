package it.softengunina.dietiestatesbackend.model;

import org.locationtech.jts.geom.Point;

public interface NotificationsOperations {
    void setArea(Point center, Double radius);
    boolean isEmailNotificationsEnabled();
    boolean isNotificationsForSaleEnabled();
    boolean isNotificationsForRentEnabled();

    void setNotificationsForSaleEnabled(boolean enabled);
    void setNotificationsForRentEnabled(boolean enabled);
}
