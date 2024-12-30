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

    @Value("${rent.cast.base.url}")
    private String rentCastBaseUrl;

    @Value("${rent.cast.api.key}")
    private String rentCastApiKey;

    @Value("${airbnb.base.url}")
    private String airbnbBaseUrl;

    @Value("${airbnb.host}")
    private String airbnbHost;

    @Value("${airbnb.api.key}")
    private String airbnbApiKey;

    @Value("${craigslist.base.url}")
    private String craigslistBaseUrl;

    @Value("${craigslist.host}")
    private String craigslistHost;

    @Value("${craigslist.api.key}")
    private String craigslistApiKey;

    /**
     * bean for http requests for RentCast api
     * @return RestClient bean for RentCast
     */
    @Bean(name = "RentCast API")
    public RestClient rentCastRestClient() {
        return RestClient.builder()
        .baseUrl(rentCastBaseUrl)
        .defaultHeader("X-Api-Key", rentCastApiKey)
                .build();
    }

    /**
     * bean for http requests for Airbnb api
     * @return RestClient bean for Airbnb
     */
    @Bean(name = "Airbnb API")
    public RestClient airbnbRestClient() {
        return RestClient.builder()
        .baseUrl(airbnbBaseUrl)
        .defaultHeaders(httpHeaders -> {
            httpHeaders.set("x-rapidapi-key", airbnbApiKey);
            httpHeaders.set("x-rapidapi-host", airbnbHost);
        })
        .build();
    }

    /**
     * bean for http requests for Craiglists api
     * @return RestClient bean for Craiglist
     */
    @Bean(name = "Craigslist API")
    public RestClient craigslistRestClient() {
        return RestClient.builder()
        .baseUrl(craigslistBaseUrl)
        .defaultHeaders(httpHeaders -> {
            httpHeaders.set("x-rapidapi-key", craigslistApiKey);
            httpHeaders.set("x-rapidapi-host", craigslistHost);
        })
        .build();
    }

}
