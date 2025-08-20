package com.ineedhousing.new_listings_service.utils;

import com.ineedhousing.new_listings_service.models.CityCoordinates;
import com.ineedhousing.new_listings_service.models.HousingListing;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.ineedhousing.new_listings_service.constants.LocationCoordinates.getCityCoordinates;


public class NewListingsCreationUtils {

    public static  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static boolean listingAlreadyExists(HousingListingRepository repository, HousingListing listing) {
        return repository.existsByLocation(listing.getLocation());
    }

    public static int saveNewListings(HousingListingRepository housingListingRepository, Function<CityCoordinates, List<Map<String, Object>>> fetchListings, Function<List<Map<String, Object>>, List<HousingListing>> transformRawListingData) {
        List<CityCoordinates> cities = getCityCoordinates();
        List<HousingListing> newListings = cities.parallelStream()
                .map(fetchListings)
                .map(transformRawListingData)
                .flatMap(List::stream)
                .filter(distinctByKey(HousingListing::getLocation))
                .filter(listing -> listingAlreadyExists(housingListingRepository, listing))
                .toList();

        housingListingRepository.saveAll(newListings);
        return newListings.size();
    }

}
