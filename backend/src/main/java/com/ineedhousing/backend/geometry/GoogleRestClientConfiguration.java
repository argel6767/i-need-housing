package com.ineedhousing.backend.geometry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestClient;

@Lazy
@Configuration("geometryRestClientConfiguration")
public class GoogleRestClientConfiguration {
    
    /**
     * Bean for defining Geo GeoCode API RestClient
     * @return
     */
    @Bean(name = "Google GeoCode API")
    RestClient googleGeoCodeRestClient() {
        return RestClient.builder()
        .baseUrl("https://maps.googleapis.com/maps/api/geocode/json")
        .build();
    }
}
