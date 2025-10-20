package it.softengunina.dietiestatesbackend.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SnsServiceTest {
    SnsService snsService;
    SnsClient snsClient;

    @BeforeEach
    void setUp() {
        snsService = new SnsService();
        snsClient = Mockito.mock(SnsClient.class);
    }

    @Test
    void withClient() {
        snsService.withClient(Assertions::assertNotNull);
    }

    @Test
    void withClient_WhenThrowsSnsException() {
        Consumer<SnsClient> consumer = client -> {
            throw SnsException.builder()
                    .build();
        };
        assertDoesNotThrow(() -> snsService.withClient(consumer));
    }

    @Test
    void pubTopic() {
        Map<String, MessageAttributeValue> messageAttributes = getMessageAttributes();
        PublishResponse result = getPublishResponse();
        Mockito.when(snsClient.publish(Mockito.any(PublishRequest.class)))
                .thenReturn(result);

        PublishResponse res = snsService.pubTopic(snsClient, "message", messageAttributes);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("messageId", res.messageId()),
                () -> assertEquals(200, res.sdkHttpResponse().statusCode())
        );
    }

    @Test
    void subEmail() {
        SubscribeResponse result = getSubscribeResponse();
        Mockito.when(snsClient.subscribe(Mockito.any(SubscribeRequest.class)))
                .thenReturn(result);

        SubscribeResponse res = snsService.subEmail(snsClient, "email@example.com");

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("subscriptionArn", res.subscriptionArn()),
                () -> assertEquals(200, res.sdkHttpResponse().statusCode())
        );
    }

    @Test
    void unsubEmail() {
        UnsubscribeResponse result = UnsubscribeResponse.builder().build();
        Mockito.when(snsClient.unsubscribe(Mockito.any(UnsubscribeRequest.class)))
                .thenReturn(result);

        UnsubscribeResponse res = snsService.unsubEmail(snsClient, "subscriptionArn");

        assertNotNull(res);
    }

    @Test
    void setFilterPolicy() {
        SetSubscriptionAttributesResponse result = SetSubscriptionAttributesResponse.builder().build();
        Mockito.when(snsClient.setSubscriptionAttributes(Mockito.any(SetSubscriptionAttributesRequest.class)))
                .thenReturn(result);

        SetSubscriptionAttributesResponse res = snsService.setFilterPolicy(snsClient, "subscriptionArn", "{}");

        assertNotNull(res);
    }

    private static Map<String, MessageAttributeValue> getMessageAttributes() {
        return Map.of(
                "attribute1", MessageAttributeValue.builder()
                        .dataType("String")
                        .stringValue("value1")
                        .build(),
                "attribute2", MessageAttributeValue.builder()
                        .dataType("Number")
                        .stringValue("123")
                        .build()
        );
    }

    private static PublishResponse getPublishResponse() {
        SdkHttpResponse httpResponse = SdkHttpResponse.builder()
                .statusCode(200)
                .build();

        return (PublishResponse) PublishResponse.builder()
                .messageId("messageId")
                .sdkHttpResponse(httpResponse)
                .build();
    }

    private static SubscribeResponse getSubscribeResponse() {
        return (SubscribeResponse) SubscribeResponse.builder()
                .subscriptionArn("subscriptionArn")
                .sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(200)
                        .build())
                .build();
    }
}