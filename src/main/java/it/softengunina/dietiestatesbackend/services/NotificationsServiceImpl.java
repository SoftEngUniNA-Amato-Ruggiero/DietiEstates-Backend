package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.UnsubscribeResponse;

import java.util.Map;

@Service
@Slf4j
public class NotificationsServiceImpl implements NotificationsService {
    private final SnsService snsService;

    public NotificationsServiceImpl(SnsService snsService) {
        this.snsService = snsService;
    }

    @Override
    public void publishMessageToTopic(String message, Map<String, String> attributes) {
        Map<String, MessageAttributeValue> messageAttributes = attributes.entrySet().stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> MessageAttributeValue.builder()
                                        .dataType("String")
                                        .stringValue(e.getValue())
                                        .build()
                        )
                );
        snsService.withClient(client -> snsService.pubTopic(client, message, messageAttributes));
    }

    @Override
    public void enableEmailSubscription(NotificationsPreferences prefs) {
        String email = prefs.getUser().getUsername();

        snsService.withClient(client -> {
            SubscribeResponse res = snsService.subEmail(client, email);
            if (res.sdkHttpResponse().isSuccessful()) {
                prefs.setSubscriptionArn(res.subscriptionArn());
                log.info("User {} subscribed to email notifications", email);
            }
        });
    }

    @Override
    public void disableEmailSubscription(NotificationsPreferences prefs) {
        String subscriptionArn = prefs.getSubscriptionArn();

        snsService.withClient(client -> {
            UnsubscribeResponse res = snsService.unsubEmail(client, subscriptionArn);
            if (res.sdkHttpResponse().isSuccessful()) {
                prefs.setSubscriptionArn(null);
                log.info("User {} unsubscribed from email notifications", prefs.getUser().getUsername());
            }
        });
    }

    @Override
    public void toggleEmailSubscription(NotificationsPreferences prefs) {
        if (!prefs.isEmailNotificationsEnabled()) {
            enableEmailSubscription(prefs);
        } else {
            disableEmailSubscription(prefs);
        }
    }
}
