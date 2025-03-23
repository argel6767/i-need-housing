package com.ineedhousing.backend.apis;

import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;


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

        Map<String, Object> geometry = (Map<String, Object>) response.get("geometry");
        Map<String, Double> location = (Map<String, Double>) geometry.get("location");

        return new double[]{location.get("lng"), location.get("lat")}; //switch around due to Point Class format
    }

}
