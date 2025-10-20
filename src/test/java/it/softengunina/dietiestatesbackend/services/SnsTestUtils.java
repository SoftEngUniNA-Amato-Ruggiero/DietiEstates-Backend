package it.softengunina.dietiestatesbackend.services;

import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.services.sns.model.UnsubscribeResponse;

import java.util.Map;

public class SnsTestUtils {
    public static Map<String, MessageAttributeValue> getMessageAttributes() {
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

    public static PublishResponse getPublishResponse() {
        SdkHttpResponse httpResponse = SdkHttpResponse.builder()
                .statusCode(200)
                .build();

        return (PublishResponse) PublishResponse.builder()
                .messageId("messageId")
                .sdkHttpResponse(httpResponse)
                .build();
    }

    public static SubscribeResponse buildSuccesfulSubscribeResponse() {
        return (SubscribeResponse) SubscribeResponse.builder()
                .subscriptionArn("subscriptionArn")
                .sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(200)
                        .build())
                .build();
    }

    public static SubscribeResponse buildUnsuccesfulSubscribeResponse() {
        return (SubscribeResponse) SubscribeResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(500)
                        .build())
                .build();
    }

    public static UnsubscribeResponse buildSuccesfulUnsubscribeResponse() {
        return (UnsubscribeResponse) UnsubscribeResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(200)
                        .build())
                .build();
    }
}
