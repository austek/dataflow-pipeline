package com.collibra.webhooktarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebhookTargetController {
    private final Logger logger = LoggerFactory.getLogger(WebhookTargetController.class);


    @PostMapping("/webhook")
    Mono<Void> handleWebhook(@RequestBody Mono<String> bodyMono, ServerHttpRequest request) {
        return bodyMono
            .doOnNext(body -> logger.info("Received webhook call with content '{}'", body))
            .then();
    }
}
