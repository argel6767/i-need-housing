package com.ineedhousing.backend.apis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;


    /**
     * Houses business logic for RentCast api calls
     */
@Service
public class RentCastAPIService {
    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final String SOURCE = "RentCast";
    private final int LIMIT = 100;

    public RentCastAPIService (@Qualifier("RentCast API") RestClient restClient, HousingListingRepository housingListingRepository) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
    }

    /**
     * makes an GET request to RentCast api with a city and state
     * and creates new HousingListings entities from response
     * @param city
     * @param state
     * @throws FailedApiCallException if response returns null
     * @throws NoListingsFoundException if response is empty
     * @return List
     */
    public List<HousingListing> updateListingsTableViaLocation(String city, String state) {
        List<Map<String, Object>> response = restClient.get()
        .uri(uriBuilder -> uriBuilder
                .path("/listings/rental/long-term")
                .queryParam("city", city)
                .queryParam("state", state)
                .queryParam("status", "Active")
                .queryParam("limit", LIMIT)
                .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        if (response == null) {
            throw new FailedApiCallException("Api call failed to occur. Check usage rates, and make sure your city and state are valid.");
        }
        if (response.isEmpty()) {
            throw new NoListingsFoundException(String.format("No listings found for %s, %s.", city, state));
        }
        List<HousingListing> newListings = createNewListings(response);
        return housingListingRepository.saveAll(newListings);
    }

    /**
     * makes an GET request to RentCast api with a radius, latitude, and longitude
     * and creates new HousingListings entities from response
     * @param radius
     * @param latitude
     * @param longitude
     * @throws FailedApiCallException if response returns null
     * @throws NoListingsFoundException if response is empty
     * @return List<HousingListing>
     */
    public List<HousingListing> updateListingsTableViaArea(Integer radius, Double latitude, Double longitude) {
        List<Map<String, Object>> response = restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/listings/rental/long-term")
            .queryParam("latitude", latitude)
            .queryParam("longitude", longitude)
            .queryParam("radius", radius)
            .queryParam("status", "Active")
            .queryParam("limit", LIMIT)
            .build())
            .retrieve()
            .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        if (response == null) {
            throw new FailedApiCallException("Api call failed to occur. Check usage rates, and make sure your latitude and longitude are valid.");
        }
        if (response.isEmpty()) {
            throw new NoListingsFoundException(String.format("No listings found for coordinates (%.2f, %.2f) within radius: %d.", latitude, longitude, radius));
        }
        List<HousingListing> newListings = createNewListings(response);
        return housingListingRepository.saveAll(newListings);
    }

    /**
     * creates List of HouseListing using the response List from RentCast api call
     * @param response
     * @return
     */
    private List<HousingListing> createNewListings(List<Map<String, Object>> response) {
        List<HousingListing> newListings = new ArrayList<>();
        GeometryFactory factory = GeometrySingleton.getInstance();
        response.stream().forEach(listing -> {
            HousingListing newListing = new HousingListing();
            newListing.setSource(SOURCE);
            newListing.setTitle((String)listing.get("id"));
            newListing.setRate((Double) listing.get("price"));
            Point point = factory.createPoint(
            new Coordinate((Double)listing.get("longitude"), (Double)listing.get("latitude")));
            newListing.setLocation(point);
            newListing.setAddress((String)listing.get("formattedAddress"));
            newListing.setPropertyType((String)listing.get("propertyType"));
            newListing.setNumBeds((Integer)listing.get("bedrooms"));
            newListing.setNumBaths((Double)listing.get("bathrooms"));
            newListings.add(newListing);
        });
        return newListings;
    }

}

