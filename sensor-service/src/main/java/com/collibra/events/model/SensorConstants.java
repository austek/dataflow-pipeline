package com.collibra.events.model;

import java.time.Duration;

public class SensorConstants {
    private SensorConstants() {
    }

    public static final String SENSOR_ALARM_TOPIC_NAME = "sensors_alarm";
    public static final String SENSOR_INGEST_TOPIC_NAME = "sensors_ingest";
    public static final String SENSOR_ALARM_DLQ_TOPIC_NAME = SENSOR_INGEST_TOPIC_NAME + "-alarm-dlq";
    public static final String SENSOR_MEDIAN_TOPIC_NAME = "sensors_median";
    public static final String SENSOR_MEDIAN_DLQ_TOPIC_NAME = SENSOR_INGEST_TOPIC_NAME + "-median-dlq";
    public static final String SENSOR_MEAN_TOPIC_NAME = "sensors_mean";
    public static final String SENSOR_MEAN_DLQ_TOPIC_NAME = SENSOR_INGEST_TOPIC_NAME + "-mean-dlq";
    public static final int MAX_GROUPS_IN_FLIGHT = 5000;
    public static final int MAX_GROUP_SIZE = 1000;
    public static final Duration GROUP_WINDOW_DURATION = Duration.ofSeconds(2);
}
