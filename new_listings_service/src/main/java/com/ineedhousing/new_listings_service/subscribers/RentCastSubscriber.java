package com.ineedhousing.new_listings_service.subscribers;

import com.ineedhousing.new_listings_service.geometry.GeometrySingleton;
import com.ineedhousing.new_listings_service.models.CityCoordinates;
import com.ineedhousing.new_listings_service.models.HousingListing;
import com.ineedhousing.new_listings_service.models.events.NewListingsEvent;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ineedhousing.new_listings_service.utils.NewListingsCreationUtils.saveNewListingsAsync;

@Component
public class RentCastSubscriber {

    Logger logger = LoggerFactory.getLogger(RentCastSubscriber.class);
    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final GoogleAPIService googleAPIService;
    private final ApplicationEventPublisher eventPublisher;
    private final String SOURCE = "RentCast";
    private final int LIMIT = 500;
    private final int RADIUS = 30;

    public RentCastSubscriber(@Qualifier("RentCast API") RestClient restClient, HousingListingRepository housingListingRepository, GoogleAPIService googleAPIService, ApplicationEventPublisher eventPublisher) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
        this.googleAPIService = googleAPIService;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    @Async
    public void handleNewListingsEvent(NewListingsEvent event) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int size = saveNewListingsAsync(housingListingRepository, this::fetchNewListings, this::transformRawListingData);
        stopWatch.stop();
        logger.info("{} New Listings Created by RentCast. Runtime: {}", size, stopWatch.getTotalTimeMillis());
    }

    private List<Map<String, Object>> fetchNewListings(CityCoordinates cityCoordinates) {
        try {
            List<Map<String, Object>> response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/listings/rental/long-term")
                            .queryParam("latitude", cityCoordinates.latitude())
                            .queryParam("longitude", cityCoordinates.longitude())
                            .queryParam("radius", RADIUS)
                            .queryParam("limit", LIMIT)
                            .queryParam("status", "Active")
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });
            if (response == null) {
                logger.warn("RentCast API request failed for {}. Check usage rates, and make sure latitude and longitude are valid.", cityCoordinates.cityName());
                return new ArrayList<>();
            }
            if (response.isEmpty()) {
                logger.info("No new listings found for {}", cityCoordinates.cityName());
            }
            logger.info("RentCast API response for {} successful.", cityCoordinates.cityName());
            return response;
        }
        catch (Exception e) {
            logger.error("Failed to fetch new listings for {} from RentCast API. Error message: {}", cityCoordinates.cityName(), e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<HousingListing> transformRawListingData(List<Map<String, Object>> response) {
        GeometryFactory factory = GeometrySingleton.getInstance();
        return response.stream()
                .map(listing -> {
                    HousingListing.HousingListingBuilder builder = new HousingListing.HousingListingBuilder();
                    double[] coords;
                    try {
                        if (listing.get("longitude") == null || listing.get("latitude") == null) {
                            logger.info("No coordinates found for listing being processed, calling Google Service");
                            coords = googleAPIService.getCoordinates((String)listing.get("formattedAddress"));
                        }
                        else {
                            coords = new double[]{(Double)listing.get("longitude"), (Double)listing.get("latitude")};
                        }
                        Point point = factory.createPoint(new Coordinate(coords[0], coords[1]));

                        return builder.source(SOURCE)
                                .title((String)listing.get("id"))
                                .rate(((Number) listing.get("price")).doubleValue())
                                .location(point)
                                .address((String)listing.get("formattedAddress"))
                                .propertyType((String)listing.get("propertyType"))
                                .numBeds((Integer)listing.get("bedrooms"))
                                .numBaths(((Number)listing.get("bathrooms")).doubleValue())
                                .build();
                    }
                    catch (Exception e) {
                        logger.error("Failed to transform and build new listing. Error Message: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
