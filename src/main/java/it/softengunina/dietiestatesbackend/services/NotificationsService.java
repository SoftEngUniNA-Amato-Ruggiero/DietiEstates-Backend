package it.softengunina.dietiestatesbackend.services;

import it.softengunina.dietiestatesbackend.model.NotificationsPreferences;
import it.softengunina.dietiestatesbackend.model.users.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sns.paginators.ListTopicsIterable;

@Service
@Slf4j
public class NotificationsService {
    private static final String TOPIC_ARN = "arn:aws:sns:eu-west-3:340752836075:DietiEstates-Insertions";
    private static final ProfileCredentialsProvider CREDENTIALS_PROVIDER = ProfileCredentialsProvider.create();

    public static void listSNSTopics(SnsClient snsClient) {
        try {
            ListTopicsIterable listTopics = snsClient.listTopicsPaginator();
            listTopics.stream()
                    .flatMap(r -> r.topics().stream())
                    .forEach(content -> log.info(" Topic ARN: {}", content.topicArn()));

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }

    public void publishNotificationToSNSTopic(String message) {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(CREDENTIALS_PROVIDER)
                .build();
        pubTopic(snsClient, message);
        snsClient.close();
    }

    public void updateEmailSubscription(NotificationsPreferences prefs) {
        BaseUser user = prefs.getUser();
        if (prefs.isEmailNotificationsEnabled()) {
            subscribeUserToEmailNotifications(user.getUsername());
            log.info("User {} subscribed to email notifications", user.getUsername());
        } else {
            unsubscribeUserFromEmailNotifications(user.getUsername());
            log.info("User {} unsubscribed to email notifications", user.getUsername());
        }
    }

    private void subscribeUserToEmailNotifications(String email) {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(CREDENTIALS_PROVIDER)
                .build();
        subEmail(snsClient, email);
        snsClient.close();
    }

    private void unsubscribeUserFromEmailNotifications(String subscriptionArn) {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(CREDENTIALS_PROVIDER)
                .build();
        unsubEmail(snsClient, subscriptionArn);
        snsClient.close();
    }

    private static void pubTopic(SnsClient snsClient, String message) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(NotificationsService.TOPIC_ARN)
                    .build();

            PublishResponse result = snsClient.publish(request);
            log.info("{} Message sent. Status is {}", result.messageId(), result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }

    private static void subEmail(SnsClient snsClient, String email) {
        try {
            SubscribeRequest request = SubscribeRequest.builder()
                    .protocol("email")
                    .endpoint(email)
                    .returnSubscriptionArn(true)
                    .topicArn(NotificationsService.TOPIC_ARN)
                    .build();

            SubscribeResponse result = snsClient.subscribe(request);
            log.info("Subscription ARN: {}\n\n Status is {}", result.subscriptionArn(), result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }

    private static void unsubEmail(SnsClient snsClient, String subscriptionArn) {
        try {
            UnsubscribeRequest request = UnsubscribeRequest.builder()
                    .subscriptionArn(subscriptionArn)
                    .build();

            snsClient.unsubscribe(request);
            log.info("Unsubscribed: {}", subscriptionArn);

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }
}
