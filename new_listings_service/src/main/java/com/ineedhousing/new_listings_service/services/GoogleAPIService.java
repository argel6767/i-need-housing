package com.ineedhousing.new_listings_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GoogleAPIService {

    Logger logger = LoggerFactory.getLogger(GoogleAPIService.class);
    private final RestClient restClient;

    @Value("${google.geo.code.api.key}")
    private String geoCodeApiKey;

    public GoogleAPIService(@Qualifier("Google GeoCode API") RestClient restClient) {
        this.restClient = restClient;
    }

    public double[] getCoordinates(String address) {
        Map<String, Object> response = makeGeoCodeAPICall(address);
        if (response == null) {
            return new double[]{};
        }
        return parseResponseForCoordinates(response);
    }

    private Map<String, Object> makeGeoCodeAPICall(String address) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("Running current Address: {}", address);
        Map<String, Object> response = restClient.get()
                .uri(uriBuilder ->
                        uriBuilder.queryParam("address", address)
                                .queryParam("key", geoCodeApiKey)
                                .build())
                .retrieve()
                .body(Map.class);
        if  (response == null) {
            logger.warn("Failed to fetch GeoCode API response");
            return null;
        }
        stopWatch.stop();
        logger.info("Successful Google GeoCode request! response given: {}. Response time: {}", response, stopWatch.getTotalTimeMillis());
        return response;
    }

    private double[] parseResponseForCoordinates(Map<String, Object> response) {
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        @SuppressWarnings("unchecked")
        Map<String, Object> geometry = (Map<String, Object>) results.getFirst().get("geometry");
        @SuppressWarnings("unchecked")
        Map<String, Double> location = (Map<String, Double>) geometry.get("location");
        return new double[]{location.get("lng"), location.get("lat")}; //switch around due to Point Class format
    }

}
