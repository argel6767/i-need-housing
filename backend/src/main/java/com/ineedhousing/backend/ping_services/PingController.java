package com.ineedhousing.backend.ping_services;

import com.ineedhousing.backend.ping_services.models.models.PingEvent;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ping")
public class PingController {

    private final ApplicationEventPublisher publisher;

    public PingController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping()
    @RateLimiter(name = "ping")
    public String pingServer() {
        publisher.publishEvent(new PingEvent("ping servers", LocalDateTime.now()));
        return "All services pinged. Pong";
    }
}
