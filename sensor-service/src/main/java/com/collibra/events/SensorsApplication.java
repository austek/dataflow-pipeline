package com.collibra.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.pulsar.reactive.config.annotation.EnableReactivePulsar;

@SpringBootApplication
@EnableReactivePulsar
public class SensorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorsApplication.class, args);
    }

}
