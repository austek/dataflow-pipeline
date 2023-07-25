package com.collibra.events.components;

import com.collibra.events.model.SensorEvent;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.pulsar.reactive.config.annotation.ReactivePulsarListener;
import org.springframework.pulsar.reactive.core.ReactivePulsarTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Function;

import static com.collibra.events.model.SensorConstants.GROUP_WINDOW_DURATION;
import static com.collibra.events.model.SensorConstants.MAX_GROUPS_IN_FLIGHT;
import static com.collibra.events.model.SensorConstants.MAX_GROUP_SIZE;
import static com.collibra.events.model.SensorConstants.SENSOR_INGEST_TOPIC_NAME;
import static com.collibra.events.model.SensorConstants.SENSOR_MEDIAN_TOPIC_NAME;

@Component
public class SensorMedianProcessor {
    private final Logger logger = LoggerFactory.getLogger(SensorMedianProcessor.class);
    private final ReactivePulsarTemplate<SensorEvent> pulsarTemplate;

    @Autowired
    public SensorMedianProcessor(ReactivePulsarTemplate<SensorEvent> pulsarTemplate) {
        super();
        this.pulsarTemplate = pulsarTemplate;
    }

    private static MessageResult<Void> acknowledgeMessage(Message<SensorEvent> sensorEventMessage) {
        return MessageResult.acknowledge(sensorEventMessage.getMessageId());
    }

    @ReactivePulsarListener(
            subscriptionName = SENSOR_INGEST_TOPIC_NAME + "-median-sub",
            topics = SENSOR_INGEST_TOPIC_NAME,
            stream = true,
            schemaType = SchemaType.JSON,
            subscriptionType = SubscriptionType.Key_Shared,
            deadLetterPolicy = "sensorMedianDeadLetterPolicy",
            consumerCustomizer = "sensorMedianConsumerCustomizer")
    public Flux<MessageResult<Void>> consumeMessages(Flux<Message<SensorEvent>> messages) {
        return messages.groupBy(m -> m.getValue().name(), MAX_GROUPS_IN_FLIGHT)
                .flatMap(
                        group -> group.publishOn(Schedulers.parallel())
                                .take(GROUP_WINDOW_DURATION)
                                .take(MAX_GROUP_SIZE)
                                .collectList()
                                .delayUntil(entriesForWindow -> processSensorWindow(group.key(), entriesForWindow))
                                .flatMapIterable(Function.identity())
                                .map(SensorMedianProcessor::acknowledgeMessage),
                        MAX_GROUPS_IN_FLIGHT);
    }

    private Publisher<?> processSensorWindow(String n, List<Message<SensorEvent>> entriesForWindow) {
        SensorEvent medianEvent =
                entriesForWindow.get(entriesForWindow.size() / 2).getValue();
        SensorEvent medianEntry = new SensorEvent(n, medianEvent.value(), medianEvent.time());
        logger.info("Sending Median: {}", medianEntry);
        return pulsarTemplate.send(
                SENSOR_MEDIAN_TOPIC_NAME,
                Mono.just(
                        MessageSpec.builder(medianEntry).key(medianEntry.name()).build()),
                Schema.JSON(SensorEvent.class));
    }
}
