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

    //TODO THIS WILL BE USED FOR EXTERNAL API CALLS LATER!!!!
    /*
     * bean to that builds the actual RestClient and to be used in the LinkedInService
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .build();
    }

}
