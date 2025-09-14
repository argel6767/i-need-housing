package com.ineedhousing.backend.ping_services;

import com.ineedhousing.backend.ping_services.models.models.PingAllServicesEvent;
import com.ineedhousing.backend.ping_services.models.models.PingServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingCronJobServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingEmailServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingKeymasterServiceEvent;
import com.ineedhousing.backend.ping_services.models.models.service_pings.PingNewListingsServiceEvent;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ping")
public class PingController {

    private final ApplicationEventPublisher publisher;

    public PingController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping()
    @RateLimiter(name = "ping")
    public String pingServer() {
        publisher.publishEvent(new PingAllServicesEvent("ping servers", LocalDateTime.now()));
        return "All services pinged. Pong";
    }

    @PostMapping("/{serviceName}")
    @RateLimiter(name = "ping")
    public String pingService(@PathVariable String serviceName) {
        PingServiceEvent event = switch (serviceName) {
            case "keymaster" -> new PingKeymasterServiceEvent();
            case "cron" -> new PingCronJobServiceEvent();
            case "email" -> new PingEmailServiceEvent();
            case "new-listings" -> new PingNewListingsServiceEvent();
            default -> throw new IllegalArgumentException("Invalid service name: " + serviceName);
        };
        publisher.publishEvent(event);
        return String.format("%s services pinged. Pong", serviceName);
    }
}
