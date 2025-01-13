package com.ineedhousing.backend.housing_listings;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.requests.GetListingsByPreferenceRequest;
import com.ineedhousing.backend.housing_listings.requests.GetListingsBySpecificPreferenceRequest;
import com.ineedhousing.backend.housing_listings.requests.GetListingsInAreaRequest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * HousingListing endpoints
 */
@RestController
@RequestMapping("/listings")
public class HousingListingController {

    private final HousingListingService housingListingService;

    public HousingListingController(HousingListingService housingListingService) {
        this.housingListingService = housingListingService;
    }

    @GetMapping("/area")
    public ResponseEntity<?> getListingsInArea(@RequestBody GetListingsInAreaRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsInArea(request.getLatitude(), request.getLongitude(), request.getRadius());
            return ResponseEntity.ok(listings);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get HousingListing info
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getListing(@PathVariable Long id) {
        try {
            HousingListing housingListing = housingListingService.getListing(id);
            return ResponseEntity.ok(housingListing);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable Long id) {
        try {
            String message = housingListingService.deleteListing(id);
            return ResponseEntity.ok(message);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/preferences/exact")
    public ResponseEntity<?> getListingWithExactPreferences(@RequestBody GetListingsByPreferenceRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsByPreferences(request.getLatitude(), request.getLongitude(), request.getRadius(), request.getPreferences(), UserPreferencesFilterer::findByExactPreferences);
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/preferences/non-strict")
    public ResponseEntity<?> getListingWithNonStrictPreferences(@RequestBody GetListingsByPreferenceRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsByPreferences(request.getLatitude(), request.getLongitude(), request.getRadius(), request.getPreferences(), UserPreferencesFilterer::findByNonStrictPreferences);
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/preferences/specific")
    public ResponseEntity<?> getListingWithSpecificPreference(@RequestBody GetListingsBySpecificPreferenceRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsBySpecificPreference(request.getLatitude(), request.getLongitude(), request.getRadius(), request.getSpecificPreference());
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe, HttpStatus.NO_CONTENT);
        }
    }
}
