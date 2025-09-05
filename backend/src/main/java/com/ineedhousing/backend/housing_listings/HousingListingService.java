package com.ineedhousing.backend.housing_listings;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.ineedhousing.backend.email.models.ListingsCacheInvalidationEvent;
import com.ineedhousing.backend.housing_listings.dto.responses.ListingsResultsPageDto;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user.UserType;
import lombok.extern.java.Log;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.geometry.PolygonCreator;
import com.ineedhousing.backend.housing_listings.exceptions.NoListingFoundException;
import com.ineedhousing.backend.housing_listings.utils.UserPreferencesFilterer;
import com.ineedhousing.backend.user_search_preferences.UserPreference;
import com.ineedhousing.backend.user_search_preferences.UserPreferenceService;


/**
 * Houses HouseListing business logic
 */
@Log
@Service
public class HousingListingService {
    private final HousingListingRepository housingListingRepository;
    private final UserPreferenceService userPreferenceService;
    private final UserService userService;
    private final CacheManager cacheManager;
    private final int PAGE_SIZE = 50;

    public HousingListingService(HousingListingRepository housingListingRepository, UserPreferenceService userPreferenceService, UserService userService, CacheManager cacheManager) {
        this.housingListingRepository = housingListingRepository;
        this.userPreferenceService = userPreferenceService;
        this.userService = userService;
        this.cacheManager = cacheManager;
    }

    /**
     * Finds all listings in a circle with given radius, and sorts them closest to center
     * where radius is in miles
     * latitude and longitude must reside in the continental US
     * @param longitude
     * @param latitude
     * @param radius
     * @return
     */
    @Cacheable(key = "T(java.lang.String).format('%s:%s:%s', #latitude, #longitude, #radius)", cacheNames ="listings")
    public List<HousingListing> getListingsInArea(double latitude, double longitude, int radius) {
        verifyGetListingsInAreaInputs(latitude, longitude, radius);

        GeometryFactory factory = GeometrySingleton.getInstance();
        Point center = factory.createPoint(new Coordinate(longitude, latitude)); //Point objects have longitude first
        Polygon area = PolygonCreator.createCircle(center, radius, 32);

        List<HousingListing> listings = housingListingRepository.getAllListingsInsideArea(area);
        if (listings.isEmpty()) {
            return listings; //no need to do sorting logic
        }
        listings = listings.stream()
                .sorted((listingOne, listingTwo) -> (int) sortListingsByDistance(listingOne, listingTwo, center)
        ).toList();
        return listings;
    }

    @Cacheable(key = "T(java.lang.String).format('%s:%s:%s:%s', #latitude, #longitude, #radius, #page)", cacheNames = "listings")
    public ListingsResultsPageDto getListingsInArea(double latitude, double longitude, int radius, int page) {
        verifyGetListingsInAreaInputs(latitude, longitude, radius);

        GeometryFactory factory = GeometrySingleton.getInstance();
        Point center = factory.createPoint(new Coordinate(longitude, latitude)); //Point objects have longitude first
        Polygon area = PolygonCreator.createCircle(center, radius, 32);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HousingListing> results = housingListingRepository.getAllListingsInsideArea(area, pageable);
        List <HousingListing> listings = results.get()
                .sorted((listingOne, listingTwo) -> (int) sortListingsByDistance(listingOne, listingTwo, center))
                .toList();
        return new ListingsResultsPageDto(listings, results.getNumber(), results.getTotalPages());
    }

    private static void verifyGetListingsInAreaInputs(double latitude, double longitude, int radius) {
        if (radius <= 0 || radius > 30) {
            throw new IllegalArgumentException("radius must be between 1 and 30!");
        }
        if (latitude < 24.00 || latitude > 49) {
            throw new IllegalArgumentException("latitude value must be in the US!");
        }
        if (longitude < -125.00 || longitude > -67.00) {
            throw new IllegalArgumentException("longitude value must be in the US!");
        }
    }


    private double sortListingsByDistance(HousingListing listingOne, HousingListing listingTwo, Point center) {
        double distanceOne = center.distance(listingOne.getLocation());
        double distanceTwo = center.distance(listingTwo.getLocation());
        return Double.compare(distanceOne, distanceTwo);
    }

    /**
     * Gets listing info
     * @param id
     * @return
     */
    public HousingListing getListing(Long id) {
        HousingListing listing = housingListingRepository.findById(id)
            .orElseThrow(() -> new NoListingFoundException(String.format("No listing found with id: ", id)));
        return listing;
    }

    /**
     * deletes listing
     * @param id
     * @return
     */
    public String deleteListing(Long id) {
        if (!housingListingRepository.existsById(id)) {
            throw new NoListingFoundException(String.format("No listing found with id: %d", id));
        }
        housingListingRepository.deleteById(id);
        return "Listing successfully deleted.";
    }

    /**
     * returns List<HousingListing> that has been filtered by given method
     * @param latitude
     * @param longitude
     * @param radius
     * @param userPreference
     * @param filterMethod
     * @return
     */

    public List<HousingListing> getListingsByPreferences(double latitude, double longitude, int radius, UserPreference userPreference, BiFunction<UserPreference, List<HousingListing>, List<HousingListing>> filterMethod) {
        List<HousingListing> listings = getListingsInArea(latitude, longitude, radius);
        List<HousingListing> filteredListings = filterMethod.apply(userPreference, listings);
        if (filteredListings.isEmpty()) {
            throw new NoListingsFoundException("No Listings found for given preferences: " + userPreference.toString());
        }
        return filteredListings;
    }


    public List<HousingListing> getListingsByPreferences(Long preferenceId, List<HousingListing> listings, BiFunction<UserPreference, List<HousingListing>, List<HousingListing>> filterMethod) {
        log.info("Filtering listings by preference: " + preferenceId);
        UserPreference userPreference = userPreferenceService.findUserPreferencesId(preferenceId);
        List<HousingListing> filteredListings = filterMethod.apply(userPreference, listings);
        if (filteredListings.isEmpty()) {
            log.info("No matching listings found for given preferences");
            throw new NoListingsFoundException("No Listings found for given preference: " + preferenceId);
        }
        log.info(filteredListings.size() + " matching listings found");
        return filteredListings;
    }

    /**
     * New implementation using
     * @param userId
     * @param page
     * @return
     */
    @Cacheable(key = "T(java.lang.String).format('%s:%s', #userId, #page)", cacheNames = "listings")
    public ListingsResultsPageDto getListingsByPreference(Long userId, int page) {
        log.info("Getting listings for user: " + userId);
        User user = userService.getUserById(userId);
        UserPreference preferences = user.getUserPreferences();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HousingListing> listingsFound;
        Polygon area = preferences.getDesiredArea();
        if (user.getUserType().equals(UserType.NEW_GRAD)) {
            listingsFound = housingListingRepository.filterListingByPreferences(pageable, area, preferences.getMaxRent(), preferences.getMinNumberOfBedrooms(), preferences.getMinNumberOfBathrooms());
        }
        else {
            listingsFound = housingListingRepository.filterListingByPreferences(pageable, area, preferences.getMaxRent(), preferences.getMinNumberOfBedrooms(), preferences.getMinNumberOfBathrooms(),
                    preferences.getInternshipStart(), preferences.getInternshipEnd());
        }
        List <HousingListing> listingsSorted = listingsFound.get()
                .sorted((listingOne, listingTwo) -> (int) sortListingsByDistance(listingOne, listingTwo, area.getCentroid()))
                .toList();
        return new ListingsResultsPageDto(listingsSorted, listingsFound.getNumber(), listingsFound.getTotalPages());
    }

    /**
     * returns listings filtered by given preference only
     */

    public List<HousingListing> getListingsBySpecificPreference(double latitude, double longitude, int radius, Map<String, Object> preference) {
        List<HousingListing> listings = getListingsInArea(latitude, longitude, radius);
        List<HousingListing> filteredListings = UserPreferencesFilterer.findBySpecificPreference(preference, listings);
        if (filteredListings.isEmpty()) {
            throw new NoListingsFoundException("No Listings found for given preference: " + preference.keySet().stream().findFirst().get());
        }
        return filteredListings;
    }

    /**
     * returns listings filtered by given preferences only
     * @param latitude
     * @param longitude
     * @param radius
     * @param preferences
     * @return
     */
    public List<HousingListing> getListingsByMultiplePreferences(double latitude, double longitude, int radius, Map<String, Object> preferences) {
        List<HousingListing> listings = getListingsInArea(latitude, longitude, radius);
        List<HousingListing> filteredListings = UserPreferencesFilterer.findByMultiplePreferences(preferences, listings);
        if (filteredListings.isEmpty()) {
            throw new NoListingsFoundException("No listings found for given preferences" + preferences.toString());
        }
        return filteredListings;
    }

    @EventListener(ListingsCacheInvalidationEvent.class)
    public void onHouseListingUpdated(ListingsCacheInvalidationEvent event) {
        log.info("Clearing listings cache to reflect new listings");
        Cache cache = cacheManager.getCache("listings");

        if (cache != null) {
            cache.clear();
        }
    }

}
