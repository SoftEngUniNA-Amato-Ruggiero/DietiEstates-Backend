package it.softengunina.dietiestatesbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
@Slf4j
public class NotificationsPublishingService {
    private static final String TOPIC_ARN = "arn:aws:sns:eu-west-3:340752836075:DietiEstates-Insertions";
    private static final ProfileCredentialsProvider CREDENTIALS_PROVIDER = ProfileCredentialsProvider.create();

    public void sendNotification(String message) {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(CREDENTIALS_PROVIDER)
                .build();
        pubTopic(snsClient, message);
        snsClient.close();
    }

    private static void pubTopic(SnsClient snsClient, String message) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(NotificationsPublishingService.TOPIC_ARN)
                    .build();

            PublishResponse result = snsClient.publish(request);
            log.info("{} Message sent. Status is {}", result.messageId(), result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }
}
