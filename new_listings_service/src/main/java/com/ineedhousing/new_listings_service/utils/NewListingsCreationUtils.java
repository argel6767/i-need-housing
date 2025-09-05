package com.ineedhousing.new_listings_service.utils;

import com.ineedhousing.new_listings_service.models.CityCoordinates;
import com.ineedhousing.new_listings_service.models.HousingListing;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;
import com.ineedhousing.new_listings_service.subscribers.RentCastSubscriber;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.ineedhousing.new_listings_service.constants.LocationCoordinates.getCityCoordinates;


public class NewListingsCreationUtils {

    private static int BATCH_SIZE = 500;
    private static final Logger logger = LoggerFactory.getLogger(NewListingsCreationUtils.class);


    public static  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static List<HousingListing> filterDuplicateListings(HousingListingRepository repository, List<HousingListing> candidates) {
        if (candidates.isEmpty()) {
            return List.of();
        }

        Set<Point> existingLocations = new HashSet<>(
                repository.findExistingLocations(candidates.stream().map(HousingListing::getLocation).toList()));

        // Filter out existing ones in-memory
        return candidates.stream()
                .filter(listing -> !existingLocations.contains(listing.getLocation()))
                .toList();
    }

    public static int saveNewListingsAsync(HousingListingRepository housingListingRepository, Function<CityCoordinates, List<Map<String, Object>>> fetchListings, Function<List<Map<String, Object>>, List<HousingListing>> transformRawListingData) {
        List<CityCoordinates> cities = getCityCoordinates();
       try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // Submit one task per city
            List<Future<List<HousingListing>>> futures = cities.stream()
                 .map(city -> executor.submit(() -> {
                      List<Map<String, Object>> raw = fetchListings.apply(city);
                            return transformRawListingData.apply(raw);
                        }))
                        .toList();

                // Collect results from all futures
                List<HousingListing> newListings = futures.stream()
                        .flatMap(future -> {
                            try {
                                return future.get().stream();
                            } catch (Exception e) {
                                // Log and skip failed city
                                e.printStackTrace();
                                return Stream.empty();
                            }
                        })
                        .filter(distinctByKey(HousingListing::getLocation))
                        .toList();

               List<HousingListing> nonDuplicateListings = filterDuplicateListings(housingListingRepository, newListings);
               int successfulSaves = batchSaveNewListings(housingListingRepository, nonDuplicateListings);
               return successfulSaves;
            }
    }

    public static int  saveNewListing(HousingListingRepository housingListingRepository, Function<CityCoordinates, List<Map<String, Object>>> fetchListings, Function<List<Map<String, Object>>, List<HousingListing>> transformRawListingData) {
        List<CityCoordinates> cities = getCityCoordinates();
        List<HousingListing> newListings = cities.stream()
                .map(fetchListings)
                .map(transformRawListingData)
                .flatMap(List::stream)
                .filter(distinctByKey(HousingListing::getLocation))
                .toList();

        List<HousingListing> nonDuplicateListings = filterDuplicateListings(housingListingRepository, newListings);

        int successfulSaves = batchSaveNewListings(housingListingRepository, nonDuplicateListings);
        return successfulSaves;
    }

    private static int batchSaveNewListings(HousingListingRepository repository, List<HousingListing> newListings) {
        int successSaves = 0;
        for (int i = 0; i < newListings.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, newListings.size());
            try {
                successSaves += repository.saveAll(newListings.subList(i, end)).size();
            }
            catch (Exception e) {
                logger.error("Failed to save new listings {} to {} when batch saving. Error message: {}", i, end, e.getMessage());
            }
        }
        return successSaves;
    }

}
