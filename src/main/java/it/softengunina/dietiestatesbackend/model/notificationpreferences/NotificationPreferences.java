package it.softengunina.dietiestatesbackend.model.notificationpreferences;

import it.softengunina.dietiestatesbackend.model.users.User;
import org.locationtech.jts.geom.Point;

public interface NotificationPreferences {
    Point getCenter();
    Double getRadius();
    User getUser();
}
