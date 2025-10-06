package com.ineedhousing.backend.ai;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class ClientConfiguration {

    @Value("${google.gemini.api.key}")
    private String apiKey;

    @Bean
    Client client() {
        return Client.builder().apiKey(apiKey).build();
    }
}
