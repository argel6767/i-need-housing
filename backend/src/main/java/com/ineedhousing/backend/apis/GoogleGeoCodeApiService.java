package com.ineedhousing.backend.apis;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;

import lombok.extern.java.Log;

@Log
@Service
public class GoogleGeoCodeApiService {
    
    private final RestClient restClient;

    @Value("${google.geo.code.api.key}")
    private String geoCodeApiKey;

    public GoogleGeoCodeApiService(@Qualifier("Google GeoCode API") RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Makes an external API call to Google Geo Coding API based off the address given
     * helpful for listings with no coordinates given
     * @param address
     * @return
     */
    public double[] getCoordinates(String address) {
        log.info("Running current Address" + address);
        Map response = restClient.get()
        .uri(uriBuilder -> 
        uriBuilder.queryParam("address", address)
        .queryParam("key", geoCodeApiKey)
        .build())
        .retrieve()
        .body(Map.class);
        if(response == null) {
            throw new FailedApiCallException("Google Geo Encoding API Failed to be Called!!! Check Usage Rates");
        }
        log.info("response given" + response);

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
        Map<String, Double> location = (Map<String, Double>) geometry.get("location");

        return new double[]{location.get("lng"), location.get("lat")}; //switch around due to Point Class format
    }

}
