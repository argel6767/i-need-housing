package com.ineedhousing.backend.apis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Houses the multiple RestClient beans for each external API
 */
@Configuration("housingGatheringRestClientConfiguration")
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

    @Value("${zillow.base.url}")
    private String zillowBaseUrl;

    @Value("${zillow.host}")
    private String zillowHost;

    @Value("${zillow.api.key}")
    private String zillowApiKey;

    /**
     * bean for http requests for RentCast api
     * @return RestClient bean for RentCast
     */
    @Bean(name = "RentCast API")
    RestClient rentCastRestClient() {
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
    RestClient airbnbRestClient() {
        return RestClient.builder()
        .baseUrl(airbnbBaseUrl)
        .defaultHeaders(httpHeaders -> {
            httpHeaders.set("x-rapidapi-key", airbnbApiKey);
            httpHeaders.set("x-rapidapi-host", airbnbHost);
        })
        .build();
    }

    /**
     * bean for http requests for Craigslist api
     * @return RestClient bean for Craigslist
     */
    @Bean(name = "Craigslist API")
    RestClient craigslistRestClient() {
        return RestClient.builder()
        .baseUrl(craigslistBaseUrl)
        .defaultHeaders(httpHeaders -> {
            httpHeaders.set("x-rapidapi-key", craigslistApiKey);
            httpHeaders.set("x-rapidapi-host", craigslistHost);
        })
        .build();
    }

    /**
     * bean for http requests for Zillow api
     * @return RestClient bean for Zillow
     */
    @Bean(name = "Zillow API")
    RestClient zillowRestClient() {
        return RestClient.builder()
        .baseUrl(zillowBaseUrl)
        .defaultHeaders(httpHeaders -> {
            httpHeaders.set("x-rapidapi-key", zillowApiKey);
            httpHeaders.set("x-rapidapi-host", zillowHost);
        })
        .build();
    }


}
