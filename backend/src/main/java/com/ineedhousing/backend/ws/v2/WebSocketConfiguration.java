package com.ineedhousing.backend.ws.v2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final LogStreamWebSocketHandler logStreamWebSocketHandler;

    @Value("${admin.app.domain}")
    private String adminAppUrl;

    public WebSocketConfiguration(LogStreamWebSocketHandler logStreamWebSocketHandler) {
        this.logStreamWebSocketHandler = logStreamWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(logStreamWebSocketHandler, "cron_job/logs", "keymaster/logs", "email_service/logs", "new_listings/logs").setAllowedOriginPatterns(adminAppUrl);
    }
}
