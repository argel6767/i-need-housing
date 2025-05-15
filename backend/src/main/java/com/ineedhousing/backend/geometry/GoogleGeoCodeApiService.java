package com.ineedhousing.backend.geometry;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.geometry.dto.LocationAndCoordinatesDto;
import com.ineedhousing.backend.geometry.exceptions.ErroredGeoCodeAPICallException;

import lombok.extern.java.Log;

/**
 * Houses business logic for various location values that can found utilizing the Google Geo Code API
 * Such as location name and coordinates
 */
@Log
@Service
@Lazy
public class GoogleGeoCodeApiService {
    
    private final RestClient restClient;

    @Value("${google.geo.code.api.key}")
    private String geoCodeApiKey;

    public GoogleGeoCodeApiService(@Qualifier("Google GeoCode API") RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Makes an external API call to Google Geo Coding API based off the address given
     * helpful for listings with no coordinates given and finding geo coords based of addresses
     * @param address
     * @return
     */
    public double[] getCoordinates(String address) {
        Map response = makeGeoCodeAPICall(address);
        return grabLocationCoordsFromResponse(response);
    }

    /**
     * Parses through API response to find the coordinate data then creates a double array to hold them
     * @param response
     * @return
     */
    private double[] grabLocationCoordsFromResponse(Map response) {
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        @SuppressWarnings("unchecked")
        Map<String, Object> geometry = (Map<String, Object>) results.get(0).get("geometry");
        @SuppressWarnings("unchecked")
        Map<String, Double> location = (Map<String, Double>) geometry.get("location");
        return new double[]{location.get("lng"), location.get("lat")}; //switch around due to Point Class format
    }

    /**
     * Makes an external API call to Google Geo Coding API based off the address given
     * helpful for listings/addresses with no location name given
     * @param address
     * @return
     */
    public String getLocationName(String address) {
        Map response = makeGeoCodeAPICall(address);
        @SuppressWarnings("unchecked")
        String locationName = grabNameFromResponse(response);
        return locationName;
    }

    /**
     * Parses through API response to find the locations name then returns it
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    private String grabNameFromResponse(Map response) {
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        List<Map<String, Object>> address_components = (List<Map<String, Object>>) results.get(0).get("address_components");
        String locationName = (String) address_components.get(0).get("short_name");
        return locationName;
    }

    public LocationAndCoordinatesDto getNameAndCoordinates(String address) {
        Map response = makeGeoCodeAPICall(address);
        String locationName = grabNameFromResponse(response);
        double[] locationCoords = grabLocationCoordsFromResponse(response);
        return new LocationAndCoordinatesDto(locationName, locationCoords);
    }
    

    @SuppressWarnings("unchecked")
    private Map<String, Object> makeGeoCodeAPICall(String address) {
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
        String statusCode = (String)response.get("status");
        //api call went through but there was an error
        if (!statusCode.equals("OK")) {
            String errorMessage = (String) response.get("error_message");
            throw new ErroredGeoCodeAPICallException(errorMessage);
        }
        log.info("response given" + response);
        return response;
    }
}
