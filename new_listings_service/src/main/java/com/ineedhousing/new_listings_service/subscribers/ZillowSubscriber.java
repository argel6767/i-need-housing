package com.ineedhousing.new_listings_service.subscribers;

import com.ineedhousing.new_listings_service.geometry.GeometrySingleton;
import com.ineedhousing.new_listings_service.models.CityCoordinates;
import com.ineedhousing.new_listings_service.models.data.HousingListing;
import com.ineedhousing.new_listings_service.models.events.NewDataSuccessfullyFetchedEvent;
import com.ineedhousing.new_listings_service.models.events.NewListingsEvent;
import com.ineedhousing.new_listings_service.models.events.new_listings.ZillowCollectionEvent;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;
import com.ineedhousing.new_listings_service.services.GoogleAPIService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static ch.qos.logback.core.util.StringUtil.capitalizeFirstLetter;
import static com.ineedhousing.new_listings_service.utils.NewListingsCreationUtils.saveNewListing;

@Component
public class ZillowSubscriber {

    Logger logger = LoggerFactory.getLogger(ZillowSubscriber.class);

    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final GoogleAPIService googleGeoCodeApiService;
    private final ApplicationEventPublisher eventPublisher;
    private final String SOURCE = "Zillow";
    private final String SUCCESS_MESSAGE = "New listings successfully added by Zillow Service! Total runtime was: ";

    public ZillowSubscriber(@Qualifier("Zillow API") RestClient restClient, HousingListingRepository housingListingRepository, GoogleAPIService googleGeoCodeApiService, ApplicationEventPublisher eventPublisher) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
        this.googleGeoCodeApiService = googleGeoCodeApiService;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    @Async
    public void handleNewListingsEvent(ZillowCollectionEvent event) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int size = saveNewListing(housingListingRepository, this::fetchNewListings, this::transformRawListingData);
        stopWatch.stop();
        long runtime = stopWatch.getTotalTimeMillis()/60000;
        logger.info("{} New Listings Created by Zillow. Runtime: {}", size, runtime);
        eventPublisher.publishEvent(new NewDataSuccessfullyFetchedEvent(SOURCE, SUCCESS_MESSAGE + runtime, size, LocalDateTime.now()));

    }

    private List<Map<String, Object>> fetchNewListings(CityCoordinates cityCoordinates) {
        try {
            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/bycoordinates")
                            .queryParam("latitude", cityCoordinates.latitude())
                            .queryParam("longitude", cityCoordinates.longitude())
                            .queryParam("radius", "20")
                            .queryParam("page", "1")
                            .queryParam("listingStatus", "For_Rent")
                            .queryParam("homeType", "Houses, Townhomes, Multi-family, Condos/Co-ops, Lots-Land, Apartments, Manufactured")
                            .queryParam("listingTypeOptions", "Agent listed,New Construction,Fore-closures,Auctions")
                            .build())
                    .retrieve()
                    .body(Map.class);
            if (response == null) {
                logger.warn("Zillow API request failed for {}. Check usage rates, and make sure latitude and longitude are valid.", cityCoordinates.cityName());
                return new ArrayList<>();
            }
            if (response.isEmpty()) {
                logger.info("No new listings found for {}", cityCoordinates.cityName());
            }
            logger.info("Zillow API response for {} successful.", cityCoordinates.cityName());

            List<Map<String, Object>> newListings = (List<Map<String, Object>>) response.get("searchResults");

            if (newListings == null) {
                logger.info("\"property\" field should not be null, check official documentation!");
                return new ArrayList<>();
            }

            if (newListings.isEmpty()) {
                logger.info("No new listings found for {}", cityCoordinates.cityName());
            }
            return newListings;
        }
        catch (Exception e) {
            logger.error("Failed to fetch new listings for {} from Zillow API. Error message: {}", cityCoordinates.cityName(), e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<HousingListing> transformRawListingData(List<Map<String, Object>> response) {
        GeometryFactory factory = GeometrySingleton.getInstance();
        return response.stream()
                .flatMap(listing -> {
                    Map<String, Object> property = (Map<String, Object>)listing.get("property");

                    if (listing.get("resultType").equals("propertyGroup")) {
                        List<Map<String, Object>> listingProperties = (List<Map<String, Object>>) property.get("unitsGroup");

                        return listingProperties.stream()
                                .map(unit -> createMultipleListings(factory, property, unit));
                    }
                    return Stream.of(createSingleListing(factory, property));
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private HousingListing  createSingleListing(GeometryFactory factory, Map<String, Object> property) {
        try {
            HousingListing.HousingListingBuilder builder = populateSharedFields(factory, property);

            Map<String, Object> price = (Map<String, Object>) property.get("price");
            double rate = ((Number) price.get("value")).doubleValue();

            int numBeds = ((Number) property.get("bedrooms")).intValue();
            double numBaths = ((Number) property.get("bathrooms")).doubleValue();

            String propertyType = (String) property.get("propertyType");
            String formattedPropertyType = capitalizeFirstLetter(propertyType);

            return builder.rate(rate)
                    .numBeds(numBeds)
                    .numBaths(numBaths)
                    .propertyType(formattedPropertyType)
                    .build();
        }
        catch (Exception e) {
            logger.error("Error while creating listing for {}. Error message: {}", property.get("cityName"), e.getMessage());
            return null;
        }
    }

    private HousingListing  createMultipleListings(GeometryFactory factory, Map<String, Object> property, Map<String, Object> unit) {
        try {
            HousingListing.HousingListingBuilder builder = populateSharedFields(factory, property);

            double rate = ((Number) unit.get("minPrice")).doubleValue();
            int numBeds = ((Number) unit.get("bedrooms")).intValue();

            String groupType = (String) property.get("groupType");
            StringBuilder propertyTypeBuilder = new StringBuilder(groupType);
            int upperCaseIndex = 0;
            while (!Character.isUpperCase(groupType.charAt(upperCaseIndex))) {
                upperCaseIndex++;
            }
            propertyTypeBuilder.insert(upperCaseIndex, " ");
            propertyTypeBuilder.setCharAt(0, Character.toUpperCase(propertyTypeBuilder.charAt(0)));

            builder.rate(rate)
                    .numBeds(numBeds)
                    .propertyType(propertyTypeBuilder.toString());

            return builder.build();
        }
        catch (Exception e) {
            logger.error("Error while creating listing from a mutiple unit data value {}. Error message: {}", property.get("cityName"), e.getMessage());
            return null;
        }
    }

    private HousingListing.HousingListingBuilder populateSharedFields(GeometryFactory factory, Map<String, Object> property) {
        HousingListing.HousingListingBuilder builder =  new HousingListing.HousingListingBuilder();

        Map<String, String> addressDetails = (Map<String, String>) property.get("address");
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(addressDetails.get("streetAddress") + ", ");
        addressBuilder.append(addressDetails.get("city") + ", ");
        addressBuilder.append(addressDetails.get("state") + " ");
        addressBuilder.append(addressDetails.get("zipcode"));
        String address = addressBuilder.toString();

        String title = (String)property.get("title");
        if (title == null) {
            title = address;
        }

        Map<String, Double> coordinates = (Map<String, Double>) property.get("location");
        Point point;
        if (coordinates != null) {
            point = factory.createPoint(new Coordinate(coordinates.get("longitude"), coordinates.get("latitude")));
        }
        else {
            logger.info("No coordinates found for {}, utilizing Google API", address);
            double[] coords = googleGeoCodeApiService.getCoordinates(address);
            point = factory.createPoint(new Coordinate(coords[0], coords[1]));
        }

        //listing url built using the zpid field
        String zillowId = String.valueOf(property.get("zpid")) ;
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.zillow.com/homedetails/");
        urlBuilder.append(zillowId);
        urlBuilder.append("_zpid/");

        Map<String, Object> media = (Map<String,Object>) property.get("media");

        Map<String, List<String>> allPropertyPhotos = (Map<String, List<String>>) media.get("allPropertyPhotos");
        List<String> photos;

        if (allPropertyPhotos != null) {
            photos = allPropertyPhotos.get("highResolution");
        }
        else {
            photos = new ArrayList<>();
        }

        return builder.source(SOURCE)
                .address(address)
                .title(title)
                .location(point)
                .listingUrl(urlBuilder.toString())
                .imageUrls(photos);
    }
}
