package com.ineedhousing.backend.apis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.java.Log;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;



/**
 * Houses business logic for Airbnb calls
 */
@Service
@Lazy
@Log
@Deprecated
public class AirbnbApiService {

    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final String SOURCE = "Airbnb";
    private final Integer ADULTS = 1;
    private final Integer CHILDREN = 0;
    private final Integer INFANTS = 0;
    private final String CURRENCY = "USD";
    private final Integer PAGES = 8;

    public AirbnbApiService(@Qualifier("Airbnb API") RestClient restClient, HousingListingRepository housingListingRepository) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
    }

    /**
     * grabs new Listings from Airbnb and makes new HousingListing Entities in DB
     * @param city
     * @param checkIn
     * @param checkOut
     * @param numOfPets
     * @return List<HousingListing>
     */
    public List<HousingListing> updateListingViaLocation(String city, LocalDate checkIn, LocalDate checkOut, Integer numOfPets) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("End date cannot be before the start date!");
        }
        Map response = restClient.get()
        .uri(uriBuilder ->
            applyCommonQueryParams(uriBuilder.path("/search-location").queryParam("location", city), // Unique parameter
            checkIn, checkOut, numOfPets) // Shared parameters
        .build())
        .retrieve()
        .body(Map.class);
        if (response == null) {
            throw new FailedApiCallException("Api call failed to occur. Check usage rates, and make sure your inputs are valid");
        }
        if (response.isEmpty()) {
            throw new NoListingsFoundException(String.format("No listings found within %s, between %s - %s", city, checkIn, checkOut));
        }
        log.info("Response successfully received, parsing data.");
        List<HousingListing> newListings = createNewListings(numOfPets, response);
        List<HousingListing> nonDuplicateListings = removeDuplicateListings(newListings);
        log.info(nonDuplicateListings.size() + " new listings added.");
        return housingListingRepository.saveAll(nonDuplicateListings);
    }

    /**
     * grabs new Listings from Airbnb and makes new HouseListings Entities in DB
     * @param neLat
     * @param neLong
     * @param swLat
     * @param swLong
     * @param checkIn
     * @param checkOut
     * @param numOfPets
     * @return List<HousingListing>
     */
    public List<HousingListing> updateHousingListingsViaGeoCoordinates(Double neLat, Double neLong, Double swLat, Double swLong, LocalDate checkIn, LocalDate checkOut, Integer numOfPets) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("End date cannot be before the start date!");
        }
        Map response = restClient.get()
        .uri(uriBuilder -> applyCommonQueryParams(uriBuilder
        .path("/search-geo")
        .queryParam("ne_lat", neLat)
        .queryParam("ne_lng", neLong)
        .queryParam("sw_lat", swLat)
        .queryParam("sw_lng", swLong), checkIn, checkOut, 0)
        .build())
        .retrieve()
        .body(Map.class);
        if (response == null) {
            throw new FailedApiCallException("Api call failed to occur. Check usage rates, and make sure your inputs are valid");
        }
        if (response.isEmpty()) {
            throw new NoListingsFoundException(String.format("No listings found within coordinates given, between %s - %s", checkIn, checkOut));
        }
        log.info("Response successfully received, parsing data.");
        List<HousingListing> newListings = createNewListings(numOfPets, response);
        List<HousingListing> nonDuplicateListings = removeDuplicateListings(newListings);
        log.info(nonDuplicateListings.size() + " new listings added.");
        return housingListingRepository.saveAll(nonDuplicateListings);
    }

    /**
     * contains the abstracted logic of mapping response values to new HouseListing Entities
     * @param numOfPets
     * @param response
     * @return
     */
    private List<HousingListing> createNewListings(Integer numOfPets, Map response) {
        GeometryFactory factory = GeometrySingleton.getInstance();
        List<Map<String, Object>> listings = (List<Map<String, Object>>) response.get("results");
        List<HousingListing> newListings = new ArrayList<>();
        listings.stream().forEach(listing -> {
            HousingListing housingListing = new HousingListing();
            //source and title
            housingListing.setSource(SOURCE);
            housingListing.setTitle((String) listing.get("name"));
            
            //rate
            Map<String, Object> priceInfo = (Map<String, Object>) listing.get("price");
            Integer rate = (Integer) priceInfo.get("rate");
            housingListing.setRate(rate.doubleValue());

            //property coordinates
            Point point = factory.createPoint(
                new Coordinate((Double)listing.get("lng"), (Double)listing.get("lat"))
            );
            housingListing.setLocation(point);

            housingListing.setAddress((String)listing.get("address"));
            housingListing.setListingUrl((String)listing.get("deeplink"));
            housingListing.setImageUrls((List<String>)listing.get("images"));
            housingListing.setPropertyType((String)listing.get("type"));
            housingListing.setNumBeds((Integer)listing.get("bedrooms"));
            Double bathrooms = ((Number)listing.get("bathrooms")).doubleValue();
            housingListing.setNumBaths(bathrooms);
            if (numOfPets > 0) {
                housingListing.setIsPetFriendly(true);
            }
            else {
                housingListing.setIsPetFriendly(false);
            }
            housingListing.setIsFurnished(true);
            newListings.add(housingListing);
        });
        return newListings;
    }

    /**
     * contains the abstracted logic of common query parameters between api calls
     * @param uriBuilder
     * @param checkIn
     * @param checkOut
     * @param numOfPets
     * @return uriBuilder
     */
    private UriBuilder applyCommonQueryParams(UriBuilder uriBuilder, LocalDate checkIn, LocalDate checkOut, int numOfPets) {
    return uriBuilder
            .queryParam("checkin", checkIn)
            .queryParam("checkout", checkOut)
            .queryParam("adults", ADULTS)
            .queryParam("children", CHILDREN)
            .queryParam("infants", INFANTS)
            .queryParam("pets", numOfPets)
            .queryParam("page", PAGES)
            .queryParam("currency", CURRENCY);
}

    /**
     * removes all duplicate listings from newListings that are already in the db
     * @param newListings
     * @return List<HousingListing>
     */
    private List<HousingListing> removeDuplicateListings(List<HousingListing> newListings) {
        return newListings.stream()
        .filter(listing -> !housingListingRepository.existsByLocation(listing.getLocation()))
        .collect(Collectors.toList());
    }

}
