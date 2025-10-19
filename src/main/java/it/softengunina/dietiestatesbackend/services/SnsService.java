package it.softengunina.dietiestatesbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sns.paginators.ListTopicsIterable;

import java.util.function.Consumer;

@Slf4j
@Service
public class SnsService {
    private static final String TOPIC_ARN = "arn:aws:sns:eu-west-3:340752836075:DietiEstates-Insertions";
    private static final ProfileCredentialsProvider CREDENTIALS_PROVIDER = ProfileCredentialsProvider.create();
    
    public void withClient(Consumer<SnsClient> consumer) {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.EU_WEST_3)
                .credentialsProvider(CREDENTIALS_PROVIDER)
                .build();

        consumer.accept( snsClient );

        snsClient.close();
    }
    
    public void listTopics(SnsClient snsClient) {
        try {
            ListTopicsIterable listTopics = snsClient.listTopicsPaginator();
            listTopics.stream()
                    .flatMap(r -> r.topics().stream())
                    .forEach(content -> log.info(" Topic ARN: {}", content.topicArn()));

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }
    }
    
    public PublishResponse pubTopic(SnsClient snsClient, String message) {
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(SnsService.TOPIC_ARN)
                    .build();

            PublishResponse result = snsClient.publish(request);
            log.info("{} Message sent. Status is {}", result.messageId(), result.sdkHttpResponse().statusCode());
            return result;

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }

    public SubscribeResponse subEmail(SnsClient snsClient, String email) {
        try {
            SubscribeRequest request = SubscribeRequest.builder()
                    .protocol("email")
                    .endpoint(email)
                    .returnSubscriptionArn(true)
                    .topicArn(SnsService.TOPIC_ARN)
                    .build();

            SubscribeResponse result = snsClient.subscribe(request);
            log.info("Subscription ARN: {}\n\n Status is {}", result.subscriptionArn(), result.sdkHttpResponse().statusCode());
            return result;

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
    
    public UnsubscribeResponse unsubEmail(SnsClient snsClient, String subscriptionArn) {
        try {
            UnsubscribeRequest request = UnsubscribeRequest.builder()
                    .subscriptionArn(subscriptionArn)
                    .build();

            UnsubscribeResponse result = snsClient.unsubscribe(request);
            log.info("Unsubscribed: {}", subscriptionArn);
            return result;

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
            throw e;
        }
    }
}
