package com.collibra.events.controller;

import com.collibra.events.model.SensorEvent;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.pulsar.reactive.core.ReactivePulsarTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.collibra.events.model.SensorConstants.SENSOR_INGEST_TOPIC_NAME;

@RestController
public class SensorController {
    private final Logger logger = LoggerFactory.getLogger(SensorController.class);
    private final ReactivePulsarTemplate<SensorEvent> pulsarTemplate;

    @Autowired
    public SensorController(ReactivePulsarTemplate<SensorEvent> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    @PostMapping("/sensors")
    Mono<Void> ingest(@RequestBody Flux<SensorEvent> sensorEventFlux) {
        return pulsarTemplate.send(
                SENSOR_INGEST_TOPIC_NAME,
                sensorEventFlux
                        .doOnNext(sensorEvent -> logger.info("About to send sensors entry {}", sensorEvent))
                        .map(sensorEvent ->
                                MessageSpec.builder(sensorEvent).key(sensorEvent.name()).build()
                        ),
                Schema.JSON(SensorEvent.class)
        ).then();
    }

}
