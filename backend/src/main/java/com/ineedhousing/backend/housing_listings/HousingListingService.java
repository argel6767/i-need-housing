package com.ineedhousing.backend.housing_listings;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.geometry.PolygonCreator;
import com.ineedhousing.backend.housing_listings.exceptions.NoListingFoundException;
import com.ineedhousing.backend.housing_listings.utils.UserPreferencesFilterer;
import com.ineedhousing.backend.user_search_preferences.UserPreference;


/**
 * Houses HouseListing business logic
 */
@Service
public class HousingListingService {
    private final HousingListingRepository housingListingRepository;

    public HousingListingService(HousingListingRepository housingListingRepository) {
        this.housingListingRepository = housingListingRepository;
    }

    /**
     * Finds all listings in a circle with given radius
     * @param longitude
     * @param latitude
     * @param radius
     * @return
     */
    public List<HousingListing> getListingsInArea(double latitude, double longitude, int radius) {
        GeometryFactory factory = GeometrySingleton.getInstance();
        Point center = factory.createPoint(new Coordinate(longitude, latitude)); //Point objects have longitude first
        Polygon area = PolygonCreator.createCircle(center, radius, 32);
        List<HousingListing> listings = housingListingRepository.getAllListingsInsideArea(area);
        if (listings.isEmpty()) {
            throw new NoListingFoundException(String.format("No listings found in the given radius of %d from point {%.2f, %.2f}", radius, latitude, longitude)) ;
        }
        return listings;
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

    /**
     * returns listings filter by given preference only
     */
    public List<HousingListing> getListingsBySpecificPreference(double latitude, double longitude, int radius, Map<String, Object> preference) {
        List<HousingListing> listings = getListingsInArea(latitude, longitude, radius);
        List<HousingListing> filteredListings = UserPreferencesFilterer.findBySpecificPreference(preference, listings);
        if (filteredListings.isEmpty()) {
            throw new NoListingsFoundException("No Listings found for given preference: " + preference.keySet().stream().findFirst().get());
        }
        return filteredListings;
    }

}
