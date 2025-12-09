package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.exceptions.EmailNotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.exceptions.NotificationsPreferencesUpdateException;
import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationsServiceImpl implements NotificationsService {
    private final SnsService snsService;

    public NotificationsServiceImpl(SnsService snsService) {
        this.snsService = snsService;
    }

    @Override
    public void publishMessageToTopic(@NonNull String message,
                                      Map<String, String> attributes) {
        if (attributes == null) {
            attributes = Collections.emptyMap();
        }

        Map<String, MessageAttributeValue> messageAttributes = attributes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, NotificationsServiceImpl::buildMessageAttribute));

        snsService.withClient(client -> snsService.pubTopic(client, message, messageAttributes));
    }

    @Override
    public void enableEmailSubscription(@NonNull NotificationsPreferences prefs) throws EmailNotificationsPreferencesUpdateException {
        if (prefs.isEmailNotificationsEnabled()) {
            return;
        }
        String email = prefs.getUser().getUsername();

        snsService.withClient(client -> {
            SubscribeResponse res = snsService.subEmail(client, email);
            if (res.sdkHttpResponse().isSuccessful()) {
                prefs.setSubscriptionArn(res.subscriptionArn());
                log.info("User {} subscribed to email notifications", email);
            } else {
                log.error("Error subscribing to email notifications: {}", res.sdkHttpResponse());
                throw new EmailNotificationsPreferencesUpdateException("Error subscribing to email notifications");
            }
        });
    }

    @Override
    public void disableEmailSubscription(@NonNull NotificationsPreferences prefs) throws EmailNotificationsPreferencesUpdateException {
        if (!prefs.isEmailNotificationsEnabled()) {
            return;
        }
        String subscriptionArn = prefs.getSubscriptionArn();

        snsService.withClient(client -> {
            UnsubscribeResponse res = snsService.unsubEmail(client, subscriptionArn);
            if (res.sdkHttpResponse().isSuccessful()) {
                prefs.setSubscriptionArn("");
                log.info("User {} unsubscribed from email notifications", prefs.getUser().getUsername());
            } else {
                log.error("Error unsubscribing from email notifications: {}", res.sdkHttpResponse());
                throw new EmailNotificationsPreferencesUpdateException("Error unsubscribing from email notifications");
            }
        });
    }

    @Override
    public void toggleEmailSubscription(@NonNull NotificationsPreferences prefs) throws EmailNotificationsPreferencesUpdateException {
        if (!prefs.isEmailNotificationsEnabled()) {
            enableEmailSubscription(prefs);
        } else {
            disableEmailSubscription(prefs);
        }
    }

    @Override
    public void applyFilterPolicy(@NonNull NotificationsPreferences prefs) throws NotificationsPreferencesUpdateException {
        String subscriptionArn = prefs.getSubscriptionArn();

        snsService.withClient(client -> {
            SetSubscriptionAttributesResponse res = snsService.setFilterPolicy(client, subscriptionArn, prefs.toFilterPolicyJson());
            if (res.sdkHttpResponse().isSuccessful()) {
                log.info("Applied filter policy to subscription ARN {}", subscriptionArn);
            } else {
                log.error("Error applying filter policy to subscription ARN {}: {}", subscriptionArn, res.sdkHttpResponse());
                throw new NotificationsPreferencesUpdateException("Error applying filter policy to email subscription");
            }
        });
    }

    private static MessageAttributeValue buildMessageAttribute(@NonNull Map.Entry<String, String> entry) {
        return MessageAttributeValue.builder()
                .dataType("String")
                .stringValue(entry.getValue())
                .build();
    }
}
