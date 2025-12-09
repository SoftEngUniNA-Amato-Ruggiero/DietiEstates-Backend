package it.softengunina.dietiestatesbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationsPreferencesTest {
    NotificationsPreferences prefs;

    @BeforeEach
    void setUp() {
        prefs = new NotificationsPreferences();
    }

    @Test
    void isEmailNotificationsEnabled_False() {
        prefs.setSubscriptionArn("");
        assertFalse(prefs.isEmailNotificationsEnabled());
    }

    @Test
    void isEmailNotificationsEnabled_True() {
        prefs.setSubscriptionArn("aws:arn:example");
        assertTrue(prefs.isEmailNotificationsEnabled());
    }

    @Test
    void toFilterPolicyJson() {
        prefs.setCity("NewYork");
        prefs.setNotificationsForSaleEnabled(true);
        prefs.setNotificationsForRentEnabled(false);

        String expectedJson = "{\"city\":[\"NewYork\"],\"type\":[\"InsertionForSale\"]}";
        assertEquals(expectedJson, prefs.toFilterPolicyJson());
    }
}