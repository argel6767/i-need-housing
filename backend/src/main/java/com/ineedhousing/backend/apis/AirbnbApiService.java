package com.ineedhousing.backend.apis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;

/**
 * Houses business logic for Airbnb calls
 */
@Service
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

    public List<HousingListing> updateListingViaLocation(String city, LocalDate checkIn, LocalDate checkOut, Integer numOfPets) {
        Map response = restClient.get()
        .uri(uriBuilder -> uriBuilder
        .path("/search-location")
        .queryParam("location", city)
        .queryParam("checkin", checkIn)
        .queryParam("checkout", checkOut)
        .queryParam("adults", ADULTS)
        .queryParam("children", CHILDREN)
        .queryParam("infants", INFANTS)
        .queryParam("pets", numOfPets)
        .queryParam("page", PAGES)
        .queryParam("currency", CURRENCY)
        .build())
        .retrieve()
        .body(Map.class);
        if (response == null) {
            throw new FailedApiCallException("Api call failed to occur. Check usage rates, and make sure your inputs are valid");
        }
        if (response.isEmpty()) {
            throw new NoListingsFoundException(String.format("No listings found within %s, between %s - %s", city, checkIn, checkOut));
        }
        List<HousingListing> newListings = createNewListings(numOfPets, response);
        return housingListingRepository.saveAll(newListings);
    }

    private List<HousingListing> createNewListings(Integer numOfPets, Map response) {
        GeometryFactory factory = GeometrySingleton.getInstance();
        List<Map<String, Object>> listings = (List<Map<String, Object>>) response.get("results");
        List<HousingListing> newListings = new ArrayList<>();
        listings.stream().forEach(listing -> {
            HousingListing housingListing = new HousingListing();
            housingListing.setSource(SOURCE);
            housingListing.setTitle((String) listing.get("name"));
            Map<String, Object> priceInfo = (Map<String, Object>) listing.get("price");
            housingListing.setRate((Double) priceInfo.get("rate"));
            Point point = factory.createPoint(
                new Coordinate((Double)listing.get("lng"), (Double)listing.get("lat"))
            );
            housingListing.setLocation(point);
            housingListing.setAddress((String)listing.get("address"));
            housingListing.setListingUrl((String)listing.get("deeplink"));
            housingListing.setImageUrls((List<String>)listing.get("images"));
            housingListing.setPropertyType((String)listing.get("type"));
            housingListing.setNumBeds((Integer)listing.get("bedrooms"));
            housingListing.setNumBaths((Double)listing.get("bathrooms"));
            if (numOfPets > 0) {
                housingListing.setIsPetFriendly(true);
            }
            else {
                housingListing.setIsPetFriendly(false);
            }
            housingListing.setIsFurnished(true);
        });
        return newListings;
    }

}
