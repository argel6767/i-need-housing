package com.ineedhousing.new_listings_service.subscribers;

import com.ineedhousing.new_listings_service.geometry.GeometrySingleton;
import com.ineedhousing.new_listings_service.models.CityCoordinates;
import com.ineedhousing.new_listings_service.models.HousingListing;
import com.ineedhousing.new_listings_service.models.NewListingsEvent;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;
import com.ineedhousing.new_listings_service.services.GoogleAPIService;
import com.ineedhousing.new_listings_service.utils.HousingListingUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ineedhousing.new_listings_service.constants.LocationCoordinates.getCityCoordinates;

@Component
public class RentCastSubscriber {

    Logger logger = LoggerFactory.getLogger(RentCastSubscriber.class);
    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final GoogleAPIService googleAPIService;
    private final String SOURCE = "RentCast";
    private final int LIMIT = 300;
    private final int RADIUS = 30;

    public RentCastSubscriber(@Qualifier("RentCast API") RestClient restClient, HousingListingRepository housingListingRepository, GoogleAPIService googleAPIService) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
        this.googleAPIService = googleAPIService;
    }


    @EventListener
    @Async
    public void handleNewListingsEvent(NewListingsEvent event) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<CityCoordinates> cities = getCityCoordinates();
        List<HousingListing> newListings = cities.parallelStream()
                .map(this::fetchNewListings)
                .map(this::transformRawListingData)
                .flatMap(List::stream)
                .filter(HousingListingUtils.distinctByKey(HousingListing::getLocation))
                .filter(listing -> HousingListingUtils.listingAlreadyExists(housingListingRepository, listing))
                .toList();

        stopWatch.stop();
        logger.info("{} New Listings Created by RentCast. Runtime: {}", newListings.size(), stopWatch.getTotalTimeMillis());
    }

    private List<Map<String, Object>> fetchNewListings(CityCoordinates cityCoordinates) {
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
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        if (response == null) {
            logger.warn("RentCast API request failed for {}. Check usage rates, and make sure latitude and longitude are valid.", cityCoordinates.cityName());
        }
        if (response.isEmpty()) {
            logger.info("No new listings found for {}", cityCoordinates.cityName());
        }
        logger.info("RentCast API response for {} successful.", cityCoordinates.cityName());
        return response;
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
                        logger.error("Failed to transform and build new listing", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
