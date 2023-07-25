package com.collibra.events.config;

import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.pulsar.reactive.core.ReactiveMessageConsumerBuilderCustomizer;

import static com.collibra.events.model.SensorConstants.SENSOR_ALARM_DLQ_TOPIC_NAME;
import static com.collibra.events.model.SensorConstants.SENSOR_MEAN_DLQ_TOPIC_NAME;
import static com.collibra.events.model.SensorConstants.SENSOR_MEDIAN_DLQ_TOPIC_NAME;

@Configuration
public class AppConfig {

    @Bean
    public DeadLetterPolicy sensorMedianDeadLetterPolicy() {
        return DeadLetterPolicy.builder()
                .maxRedeliverCount(1)
                .deadLetterTopic(SENSOR_MEDIAN_DLQ_TOPIC_NAME)
                .build();
    }

    @Bean
    public ReactiveMessageConsumerBuilderCustomizer<String> sensorMedianConsumerCustomizer() {
        return b -> b.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest);
    }

    @Bean
    public DeadLetterPolicy sensorMeanDeadLetterPolicy() {
        return DeadLetterPolicy.builder()
                .maxRedeliverCount(1)
                .deadLetterTopic(SENSOR_MEAN_DLQ_TOPIC_NAME)
                .build();
    }

    @Bean
    public ReactiveMessageConsumerBuilderCustomizer<String> sensorMeanConsumerCustomizer() {
        return b -> b.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest);
    }

    @Bean
    public DeadLetterPolicy sensorAlarmDeadLetterPolicy() {
        return DeadLetterPolicy.builder()
                .maxRedeliverCount(3)
                .deadLetterTopic(SENSOR_ALARM_DLQ_TOPIC_NAME)
                .build();
    }

    @Bean
    public ReactiveMessageConsumerBuilderCustomizer<String> sensorAlarmConsumerCustomizer() {
        return b -> b.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                .property("negativeAckRedeliveryDelay", "10ms")
                .property("ackTimeout", "60s");
    }
}
