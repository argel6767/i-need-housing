package com.ineedhousing.backend.apis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;

import lombok.extern.java.Log;


@Service
@Log
public class ZillowApiService {

    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final String SOURCE = "Zillow";
    
    public ZillowApiService(@Qualifier("Zillow API") RestClient restClient, HousingListingRepository housingListingRepository) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
    }

    /**
     * makes a GET request to Zillow API with given latitude and longitude with radius of 5
     * and creates new HousingListings entities from response
     * @param latitude
     * @param longitude
     * @return
     */
    public List<HousingListing> updateListingsTableViaCoordinates(Double latitude, Double longitude) {
        Map response = restClient.get()
        .uri(uriBuilder -> uriBuilder
        .path("/search/bycoordinates")
        .queryParam("latitude", latitude.toString())
        .queryParam("longitude", longitude.toString())
        .queryParam("radius", "5")
        .queryParam("page", "1")
        .queryParam("listingStatus", "For_Rent")
        .queryParam("homeType", "Houses, Townhomes, Multi-family, Condos/Co-ops, Lots-Land, Apartments, Manufactured")
        .queryParam("listingTypeOptions", "Agent listed,New Construction,Fore-closures,Auctions")
        .build())
        .retrieve()
        .body(Map.class);
        List<HousingListing> newListings = createNewListings(response);
        if (response == null) {
            throw new FailedApiCallException("Api call failed to occur. Check usage rates, and make sure your latitude and longitude are valid.");
        }
        if (response.isEmpty()) {
            throw new NoListingsFoundException(String.format("No listings found for coordinates (%.2f, %.2f) within radius: 5.", latitude, longitude));
        }
        return removeDuplicateListings(newListings);
    }

    private List<HousingListing> createNewListings(Map response) {
        List<HousingListing> newListings = new ArrayList<>();
        GeometryFactory factory = GeometrySingleton.getInstance();
        List<Map<String,Object>> results = (ArrayList<Map<String,Object>>) response.get("searchResults");
        results.forEach(result -> {
            Map<String, Object> property = (Map<String, Object>)result.get("property");

            if (result.get("resultType").equals("propertyGroup")) {
                List<HousingListing> units = new ArrayList<>(); //multiple listings for a single result
                List<Map<String,Object>> unitsGroup = (ArrayList<Map<String,Object>>) property.get("unitsGroup");

                unitsGroup.forEach(unit -> {
                    HousingListing newListing = populateSharedFields(factory, property);

                    //unit rate
                    double rate = ((Number) unit.get("minPrice")).doubleValue();
                    newListing.setRate(rate);

                    int numBeds = ((Number) unit.get("bedrooms")).intValue();
                    newListing.setNumBeds(numBeds);

                    // property type (property will be in camel case form)
                    String groupType = (String) property.get("groupType");
                    StringBuilder propertyTypeBuilder = new StringBuilder(groupType);
                    int upperCaseIndex = 0;
                    while (!Character.isUpperCase(groupType.charAt(upperCaseIndex))) {
                        upperCaseIndex++;
                    }
                    propertyTypeBuilder.insert(upperCaseIndex, " ");
                    propertyTypeBuilder.setCharAt(0, Character.toUpperCase(propertyTypeBuilder.charAt(0)));
                    newListing.setPropertyType(propertyTypeBuilder.toString());
                    units.add(newListing);
                });

                newListings.addAll(units);
            }

            else { //singular unit in listing
                HousingListing newListing = populateSharedFields(factory, property);

                //rate
                Map<String, Object> price = (Map<String, Object>) property.get("price");
                double rate = ((Number) price.get("value")).doubleValue();
                newListing.setRate(rate);

                int numBeds = ((Number) property.get("bedrooms")).intValue();
                double numBaths = ((Number) property.get("bathrooms")).doubleValue();
                newListing.setNumBeds(numBeds);
                newListing.setNumBaths(numBaths);

                //property type
                String propertyType = (String) property.get("propertyType");
                StringBuilder propertyTypeBuilder = new StringBuilder(propertyType);
                propertyTypeBuilder.setCharAt(0, Character.toUpperCase(propertyTypeBuilder.charAt(0)));
                newListing.setPropertyType(propertyTypeBuilder.toString());
    
                newListings.add(newListing);
            }
        });
        return newListings;
    }

    /**
     * Populates the fields of a new Listing where the method of retrieval for the data is the
     * same between propertyGroup and property objects
     * @param factory
     * @param property
     * @return
     */
    private HousingListing populateSharedFields(GeometryFactory factory, Map<String, Object> property) {
        HousingListing newListing = new HousingListing();
        // source and title
        newListing.setSource(SOURCE);
        newListing.setTitle((String)property.get("title"));
        
        //property coordinates
        Map<String, Double> coordinates = (Map<String, Double>) property.get("location");
        if (coordinates != null) {
            Point point = factory.createPoint(
            new Coordinate(coordinates.get("longitude"), coordinates.get("latitude")));
            newListing.setLocation(point);
        }
        newListing.setLocation(null);

        //address built using various fields
        Map<String, String> addressDetails = (Map<String, String>) property.get("address");
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(addressDetails.get("streetAddress") + ", ");
        addressBuilder.append(addressDetails.get("city") + ", ");
        addressBuilder.append(addressDetails.get("state") + " ");
        addressBuilder.append(addressDetails.get("zipcode"));
        newListing.setAddress(addressBuilder.toString());

        //listing url built using the zpid field
        String zillowId = String.valueOf(property.get("zpid")) ;
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.zillow.com/homedetails/");
        urlBuilder.append(zillowId);
        urlBuilder.append("_zpid/");
        newListing.setListingUrl(urlBuilder.toString());

        //imageUrls
        Map<String, Object> media = (Map<String,Object>) property.get("media");
        Map<String, List<String>> allPropertyPhotos = (Map<String, List<String>>) media.get("allPropertyPhotos");
        newListing.setImageUrls(allPropertyPhotos.get("highResolution"));

        newListing.setIsFurnished(false);
        return newListing;
    }

    /**
     * remove already present listings from created list
     * @param newListings
     * @return
     */
    private List<HousingListing> removeDuplicateListings(List<HousingListing> newListings) {
        return newListings.stream()
        .filter(listing -> !housingListingRepository.existsByLocation(listing.getLocation()))
        .collect(Collectors.toList());
    }
}
