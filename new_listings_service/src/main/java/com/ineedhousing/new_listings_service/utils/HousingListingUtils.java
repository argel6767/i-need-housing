package com.ineedhousing.new_listings_service.utils;

import com.ineedhousing.new_listings_service.models.HousingListing;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


public class HousingListingUtils {



    public static  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static boolean listingAlreadyExists(HousingListingRepository repository, HousingListing listing){
        return repository.existsByLocation(listing.getLocation());
    }
}
