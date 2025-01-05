package com.ineedhousing.backend.housing_listings;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.geometry.PolygonCreator;


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
            throw new NoListingsFoundException(String.format("No listings found in the given radius of %d from point {%.2f, %.2f}", radius, latitude, longitude)) ;
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
            .orElseThrow(() -> new NoListingsFoundException(String.format("No listing found with id: ", id)));
        return listing;
    }

    /**
     * deletes listing
     * @param id
     * @return
     */
    public String deleteListing(Long id) {
        if (!housingListingRepository.existsById(id)) {
            throw new NoListingsFoundException(String.format("No listing found with id: %d", id));
        }
        housingListingRepository.deleteById(id);
        return "Listing successfully deleted.";
    }
}
