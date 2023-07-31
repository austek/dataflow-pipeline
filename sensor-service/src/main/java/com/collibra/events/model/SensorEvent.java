package com.collibra.events.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record SensorEvent(String name, double value, Instant time) {

    @JsonCreator
    public SensorEvent(@JsonProperty("name") String name, @JsonProperty("value") double value, @JsonProperty("time") Instant time) {
        this.name = name;
        this.value = value;
        this.time = time == null ? Instant.now(): time;
    }
}
