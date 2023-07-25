package com.collibra.events.model;

import org.apache.pulsar.client.api.MessageId;

public record SensorLastSent(MessageId messageId, SensorEvent sensorEvent) {
}
