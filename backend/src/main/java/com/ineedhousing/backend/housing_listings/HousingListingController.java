package com.ineedhousing.backend.housing_listings;

import com.ineedhousing.backend.housing_listings.dto.responses.ListingsResultsPageDto;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.housing_listings.exceptions.NoListingFoundException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.dto.requests.ExactPreferencesDto;
import com.ineedhousing.backend.housing_listings.dto.requests.GetListingsByPreferenceRequest;
import com.ineedhousing.backend.housing_listings.dto.requests.GetListingsBySpecificPreferenceRequest;
import com.ineedhousing.backend.housing_listings.utils.UserPreferencesFilterer;
import com.ineedhousing.backend.jwt.JwtUtils;


import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * HousingListing endpoints
 */
@Log
@RestController
@RequestMapping("/listings")
public class HousingListingController {

    private final HousingListingService housingListingService;

    public HousingListingController(HousingListingService housingListingService) {
        this.housingListingService = housingListingService;
    }

    /**
     * get listings in area
     * @param latitude
     * @param longitude
     * @param radius
     * @return listings
     */
    @GetMapping("/area")
    @RateLimiter(name = "housing")
    public ResponseEntity<?> getListingsInArea(@RequestParam double latitude, @RequestParam double longitude, @RequestParam int radius ) {
        try {
            List<HousingListing> listings = housingListingService.getListingsInArea(latitude, longitude, radius);
            return ResponseEntity.ok(listings);
        }
        catch (IllegalArgumentException iae) {
            Map<String, String> response = new HashMap<>();
            response.put("error", iae.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/v2/area")
    @RateLimiter(name = "housing")
    public ResponseEntity<ListingsResultsPageDto> getListingsInAreaV2(@RequestParam double latitude, @RequestParam double longitude, @RequestParam int radius, @RequestParam int page) {
        ListingsResultsPageDto dto =  housingListingService.getListingsInArea(latitude, longitude, radius, page);
        return ResponseEntity.ok(dto);
    }

    /**
     * get HousingListing info
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @RateLimiter(name = "housing")
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
    @RateLimiter(name = "housing")
    public ResponseEntity<?> deleteListing(@PathVariable Long id) {
        if (!JwtUtils.isUserAdmin()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "user is not an admin, cannot delete listing");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
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
    @RateLimiter(name = "housing")
    public ResponseEntity<?> getListingWithExactPreferences(@RequestBody ExactPreferencesDto request) {
        try {
            List<HousingListing> listings = housingListingService.getListingsByPreferences(request.getId(), request.getListings(), UserPreferencesFilterer::findByExactPreferences);
            log.info("Listings found: " + listings.size());
            return ResponseEntity.ok(listings);
        }
        catch(NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/filter/v2/exact")
    @RateLimiter(name = "housing")
    public ResponseEntity<ListingsResultsPageDto> getListingsWithExactPreferencesV2(@RequestParam int page) {
        Long id = JwtUtils.getCurrentUserId();
        ListingsResultsPageDto dto = housingListingService.getListingsByPreference(id, page);
        return ResponseEntity.ok(dto);
    }

    /**
     * get listings by non strict
     * @param request
     * @return
     */
    @PostMapping("/filter/non-strict")
    @RateLimiter(name = "housing")
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
    @RateLimiter(name = "housing")
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
    @RateLimiter(name = "housing")
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
