package com.collibra.events.components;

import com.collibra.events.model.SensorEvent;
import com.collibra.events.model.SensorLastSent;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.pulsar.reactive.config.annotation.ReactivePulsarListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import static com.collibra.events.model.SensorConstants.SENSOR_MEAN_TOPIC_NAME;

@Component
public class SensorAlarmProcessor {
    private static final Double ALARM_THRESHOLD = 80.0d;
    private final ConcurrentHashMap<String, SensorLastSent> lastSentState = new ConcurrentHashMap<>();
    private final WebClient webhookWebclient;

    @Autowired
    public SensorAlarmProcessor(
            WebClient.Builder webClientBuilder,
            @Value("${alarmwebhook.url:http://localhost:8082/webhook}") String alarmWebhookUrl) {
        super();
        this.webhookWebclient = webClientBuilder.baseUrl(alarmWebhookUrl).build();
    }

    @ReactivePulsarListener(
            subscriptionName = SENSOR_MEAN_TOPIC_NAME + "-alarm-sub",
            topics = SENSOR_MEAN_TOPIC_NAME,
            stream = true,
            schemaType = SchemaType.JSON,
            useKeyOrderedProcessing = "true",
            subscriptionType = SubscriptionType.Key_Shared,
            deadLetterPolicy = "sensorAlarmDeadLetterPolicy",
            consumerCustomizer = "sensorAlarmConsumerCustomizer")
    public Flux<MessageResult<Void>> consumeMessages(Flux<Message<SensorEvent>> messages) {
        return messages.doOnNext(this::processMessage).map(MessageResult::acknowledge);
    }

    @SuppressWarnings("UnusedReturnValue")
    private Mono<Void> processMessage(Message<SensorEvent> sensorEventMessage) {
        if (hasLastSentStateChanged(sensorEventMessage)) {
            return sendStateToWebhook(sensorEventMessage);
        } else {
            return Mono.empty();
        }
    }

    private Mono<Void> sendStateToWebhook(Message<SensorEvent> sensorEventMessage) {
        return webhookWebclient
                .post()
                .bodyValue(sensorEventMessage.getValue())
                .retrieve()
                .toBodilessEntity()
                .then(updateLastSentState(sensorEventMessage))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }

    private boolean hasLastSentStateChanged(Message<SensorEvent> sensorEventMessage) {
        SensorEvent sensorEvent = sensorEventMessage.getValue();
        SensorLastSent lastSent = lastSentState.get(sensorEvent.name());
        return ((lastSent == null && sensorEvent.value() > ALARM_THRESHOLD)
                || (lastSent != null
                        && lastSent.messageId().compareTo(sensorEventMessage.getMessageId()) < 0
                        && ALARM_THRESHOLD.compareTo(lastSent.sensorEvent().value())
                                != ALARM_THRESHOLD.compareTo(sensorEvent.value())));
    }

    private Mono<Void> updateLastSentState(Message<SensorEvent> sensorEventMessage) {
        return Mono.fromRunnable(() -> {
            SensorEvent sensorEvent = sensorEventMessage.getValue();
            lastSentState.put(sensorEvent.name(), new SensorLastSent(sensorEventMessage.getMessageId(), sensorEvent));
        });
    }
}
