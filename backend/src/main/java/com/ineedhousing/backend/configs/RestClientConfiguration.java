package com.ineedhousing.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * configures the RestClient object used for the third-party LinkedIn scraper API
 */
@Configuration
public class RestClientConfiguration {
    @Value("${base.url}")
    private String baseUrl;

    @Value("${rapid.api.key}")
    private String rapidApiKey;

    @Value("${rapid.api.host}")
    private String rapidApiHost;

    /*
     * bean to that builds the actual RestClient and to be used in the LinkedInService
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("x-rapidapi-key", rapidApiKey);
                    httpHeaders.set("x-rapidapi-host", rapidApiHost);
                })
                .build();
    }

    /*
     * package private, only for testing purposes
     */
     void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    String getBaseUrl() {
        return baseUrl;
    }

     void setRapidApiKey(String rapidApiKey) {
        this.rapidApiKey = rapidApiKey;
    }

     void setRapidApiHost(String rapidApiHost) {
        this.rapidApiHost = rapidApiHost;
    }
}
