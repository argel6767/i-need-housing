package com.ineedhousing.backend.housing_listings;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.housing_listings.exceptions.NoListingFoundException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.requests.ExactPreferencesDto;
import com.ineedhousing.backend.housing_listings.requests.GetListingsByPreferenceRequest;
import com.ineedhousing.backend.housing_listings.requests.GetListingsBySpecificPreferenceRequest;
import com.ineedhousing.backend.housing_listings.requests.GetListingsInAreaRequest;
import com.ineedhousing.backend.housing_listings.utils.UserPreferencesFilterer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * get listings in area
     * @param request
     * @return
     */
    @GetMapping("/area")
    public ResponseEntity<?> getListingsInArea(@RequestParam double latitude, @RequestParam double longitude, @RequestParam int radius ) {
        try {
            List<HousingListing> listings = housingListingService.getListingsInArea(latitude, longitude, radius);
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
        catch (NoListingFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * delete listing
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListing(@PathVariable Long id) {
        try {
            String message = housingListingService.deleteListing(id);
            return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
        }
        catch (NoListingFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * get listings by user preferences
     * @param request
     * @return
     */
    @PostMapping("/filter/exact")
    public ResponseEntity<?> getListingWithExactPreferences(@RequestBody ExactPreferencesDto request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsByPreferences(request.getId(), request.getListings(), UserPreferencesFilterer::findByExactPreferences);
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get listings by non strict
     * @param request
     * @return
     */
    @PostMapping("/filter/non-strict")
    public ResponseEntity<?> getListingWithNonStrictPreferences(@RequestBody GetListingsByPreferenceRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsByPreferences(request.getLatitude(), request.getLongitude(), request.getRadius(), request.getPreferences(), UserPreferencesFilterer::findByNonStrictPreferences);
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get listings with specific preference
     * @param request
     * @return
     */
    @PostMapping("/filter/specific")
    public ResponseEntity<?> getListingWithSpecificPreference(@RequestBody GetListingsBySpecificPreferenceRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsBySpecificPreference(request.getLatitude(), request.getLongitude(), request.getRadius(), request.getSpecificPreference());
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get listings by multiple preferences
     * @param request
     * @return
     */
    @PostMapping("/preferences/multi")
    public ResponseEntity<?> getListingsWithManyPreferences(@RequestBody GetListingsBySpecificPreferenceRequest request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsByMultiplePreferences(request.getLatitude(), request.getLongitude(), request.getRadius(), request.getSpecificPreference());
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
    
}
