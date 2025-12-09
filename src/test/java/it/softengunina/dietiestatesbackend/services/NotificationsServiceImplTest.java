package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.EmailNotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.UnsubscribeResponse;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static it.softengunina.dietiestatesbackend.services.SnsTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class NotificationsServiceImplTest {
    @Mock
    SnsService snsService;
    @Mock
    SnsClient snsClient;

    AutoCloseable mocks;

    NotificationsServiceImpl notificationsService;

    @BeforeEach
    void setUp() {

        mocks = openMocks(this);

        Mockito.doAnswer(invocation -> {
                    Consumer<SnsClient> consumer = invocation.getArgument(0);
                    consumer.accept(snsClient);
                    return null;
                }).when(snsService).withClient(Mockito.any());

        notificationsService = new NotificationsServiceImpl(snsService);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }


    /* ------------------ WHITE BOX TEST SUITE ------------------ */
    /* ------------------ publishMessageToTopic ------------------ */

    @Test
    void publishMessageToTopic() {
        String message = "message";
        Map<String, String> attributes = Map.of("key", "value");

        AtomicBoolean pubTopicCalled = new AtomicBoolean(false);
        Mockito.when(snsService.pubTopic(Mockito.eq(snsClient), Mockito.any(), Mockito.any()))
                .thenAnswer(i -> {
                    pubTopicCalled.set(true);
                    return null;
                });

        notificationsService.publishMessageToTopic(message, attributes);

        assertTrue(pubTopicCalled.get());
    }

    @Test
    void publishMessageToTopic_WhenAttributesIsNull() {
        String message = "message";

        AtomicBoolean pubTopicCalled = new AtomicBoolean(false);
        Mockito.when(snsService.pubTopic(Mockito.eq(snsClient), Mockito.any(), Mockito.any()))
                .thenAnswer(i -> {
                    pubTopicCalled.set(true);
                    return null;
                });

        notificationsService.publishMessageToTopic(message, null);

        assertTrue(pubTopicCalled.get());
    }


    /* ------------------ WHITE BOX TEST SUITE ------------------ */
    /* ------------------ enableEmailSubscription ------------------ */

    @Test
    void enableEmailSubscription() {
        BaseUser user = new BaseUser("email", "sub");
        NotificationsPreferences prefs = new NotificationsPreferences(user);
        prefs.setSubscriptionArn("");

        SubscribeResponse res = buildSuccesfulSubscribeResponse();

        Mockito.when(snsService.subEmail(snsClient, prefs.getUser().getUsername()))
                .thenReturn(res);

        assertDoesNotThrow(() -> notificationsService.enableEmailSubscription(prefs));

        assertAll(
                () -> assertTrue(prefs.isEmailNotificationsEnabled()),
                () -> assertEquals("subscriptionArn", prefs.getSubscriptionArn())
        );
    }

    @Test
    void enableEmailSubscription_WhenUnsuccesfulResponse() {
        BaseUser user = new BaseUser("email", "sub");
        NotificationsPreferences prefs = new NotificationsPreferences(user);
        prefs.setSubscriptionArn("");

        SubscribeResponse res = buildUnsuccesfulSubscribeResponse();

        Mockito.when(snsService.subEmail(snsClient, prefs.getUser().getUsername()))
                .thenReturn(res);

        assertThrows(EmailNotificationsPreferencesUpdateException.class, () -> notificationsService.enableEmailSubscription(prefs));

        assertAll(
                () -> assertFalse(prefs.isEmailNotificationsEnabled()),
                () -> assertEquals("", prefs.getSubscriptionArn())
        );
    }

    @Test
    void enableEmailSubscription_WhenAlreadyEnabled() {
        BaseUser user = new BaseUser("email", "sub");
        NotificationsPreferences prefs = new NotificationsPreferences(user);
        prefs.setSubscriptionArn("subscriptionArn");

        assertDoesNotThrow(() -> notificationsService.enableEmailSubscription(prefs));

        assertAll(
                () -> assertTrue(prefs.isEmailNotificationsEnabled()),
                () -> assertEquals("subscriptionArn", prefs.getSubscriptionArn())
        );
    }


    /* ------------------ WHITE BOX TEST SUITE ------------------ */
    /* ------------------ enableEmailSubscription ------------------ */

    @Test
    void disableEmailSubscription() {
        BaseUser user = new BaseUser("email", "sub");
        NotificationsPreferences prefs = new NotificationsPreferences(user);
        prefs.setSubscriptionArn("subscriptionArn");

        UnsubscribeResponse res = buildSuccesfulUnsubscribeResponse();

        Mockito.when(snsService.unsubEmail(snsClient, prefs.getSubscriptionArn()))
                .thenReturn(res);

        assertDoesNotThrow(() -> notificationsService.disableEmailSubscription(prefs));

        assertAll(
                () -> assertFalse(prefs.isEmailNotificationsEnabled()),
                () -> assertEquals("", prefs.getSubscriptionArn())
        );
    }

    @Test
    void disableEmailSubscription_WhenUnsuccesfulResponse() {
        BaseUser user = new BaseUser("email", "sub");
        NotificationsPreferences prefs = new NotificationsPreferences(user);
        prefs.setSubscriptionArn("subscriptionArn");

        UnsubscribeResponse res = buildUnsuccesfulUnsubscribeResponse();

        Mockito.when(snsService.unsubEmail(snsClient, prefs.getSubscriptionArn()))
                .thenReturn(res);

        assertThrows(EmailNotificationsPreferencesUpdateException.class, () -> notificationsService.disableEmailSubscription(prefs));

        assertAll(
                () -> assertTrue(prefs.isEmailNotificationsEnabled()),
                () -> assertEquals("subscriptionArn", prefs.getSubscriptionArn())
        );
    }

    @Test
    void disableEmailSubscription_WhenAlreadyDisabled() {
        BaseUser user = new BaseUser("email", "sub");
        NotificationsPreferences prefs = new NotificationsPreferences(user);
        prefs.setSubscriptionArn("");

        assertDoesNotThrow(() -> notificationsService.disableEmailSubscription(prefs));

        assertAll(
                () -> assertFalse(prefs.isEmailNotificationsEnabled()),
                () -> assertEquals("", prefs.getSubscriptionArn())
        );
    }
}