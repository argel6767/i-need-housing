package com.ineedhousing.new_listings_service.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

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

    @Value("${zillow.base.url}")
    private String zillowBaseUrl;

    @Value("${zillow.host}")
    private String zillowHost;

    @Value("${zillow.api.key}")
    private String zillowApiKey;

    @Value("${keymaster.service.url}")
    private String keymasterServiceUrl;

    @Value("${service.api.token}")
    private String serviceApiToken;

    @Value("${service.name}")
    private String serviceName;

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

    @Bean(name = "Google GeoCode API")
    RestClient googleGeoCodeRestClient() {
        return RestClient.builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .build();
    }

    @Bean(name = "keymaster_service")
    RestClient keymasterRestClient() {
        return  RestClient.builder()
                .baseUrl(keymasterServiceUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Token", serviceApiToken);
                    httpHeaders.set("X-Service-Name",serviceName);
                })
                .build();
    }
}
