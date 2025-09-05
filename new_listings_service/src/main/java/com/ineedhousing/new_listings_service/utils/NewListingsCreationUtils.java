package com.ineedhousing.new_listings_service.utils;

import com.ineedhousing.new_listings_service.models.CityCoordinates;
import com.ineedhousing.new_listings_service.models.HousingListing;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;
import org.locationtech.jts.geom.Point;


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
                housingListingRepository.saveAll(nonDuplicateListings);
                return newListings.size();
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

        housingListingRepository.saveAll(nonDuplicateListings);
        return newListings.size();
    }

}
