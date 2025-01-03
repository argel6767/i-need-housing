package com.ineedhousing.backend.housing_listings;

import java.util.List;

import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import com.ineedhousing.backend.housing_listings.exceptions.ListingNotFoundException;

/**
 * Houses HouseListing business logic
 */
@Service
public class HousingListingService {
    private final HousingListingRepository housingListingRepository;

    public HousingListingService(HousingListingRepository housingListingRepository) {
        this.housingListingRepository = housingListingRepository;
    }

    public List<HousingListing> getListingsInArea(Polygon area) {
        return housingListingRepository.findAllListingsInArea(area);
    }

    public HousingListing getListing(Long id) {
        HousingListing listing = housingListingRepository.findById(id)
            .orElseThrow(() -> new ListingNotFoundException(String.format("No listing found with id: ", id)));
        return listing;
    }

    public String deleteListing(Long id) {
        if (!housingListingRepository.existsById(id)) {
            throw new ListingNotFoundException(String.format("No listing found with id: ", id));
        }
        housingListingRepository.deleteById(id);
        return "Listing successfully deleted.";
    }
}
