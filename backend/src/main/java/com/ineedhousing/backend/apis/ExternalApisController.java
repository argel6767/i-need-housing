package com.ineedhousing.backend.apis;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.apis.requests.AirbnbGeoCoordinatesDto;
import com.ineedhousing.backend.apis.requests.AirbnbLocationDto;
import com.ineedhousing.backend.apis.requests.AreaDto;
import com.ineedhousing.backend.apis.requests.CityAndStateDto;
import com.ineedhousing.backend.apis.requests.ZillowGeoCoordinatesDto;
import com.ineedhousing.backend.housing_listings.HousingListing;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Houses endpoints for external apis call to gather new housing information
 */
@RestController
@RequestMapping("/gather-housings")
public class ExternalApisController {

    private final AirbnbApiService airbnbApiService;
    private final RentCastAPIService rentCastAPIService;
    private final ZillowApiService zillowApiService;

    public ExternalApisController (AirbnbApiService airbnbApiService, RentCastAPIService rentCastAPIService, ZillowApiService zillowApiService) {
        this.airbnbApiService = airbnbApiService;
        this.rentCastAPIService = rentCastAPIService;
        this.zillowApiService = zillowApiService;
    }

    /**
     * calls Airbnb location endpoint
     * @param request
     * @return
     */
    @PostMapping("/airbnb/location")
    public ResponseEntity<?> callAirbnbViaLocation(@RequestBody AirbnbLocationDto request) {
        try {
            List<HousingListing> newListings = airbnbApiService.updateListingViaLocation(request.getCity(), request.getCheckIn(), request.getCheckOut(), request.getNumOfPets());
            return new ResponseEntity<>(newListings, HttpStatus.CREATED);
        }
        catch (FailedApiCallException fapce) {
            return new ResponseEntity<>(fapce.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /**
     * calls Airbnb geolocation endpoint
     * @param request
     * @return
     */
    @PostMapping("/airbnb/geo-coords")
    public ResponseEntity<?> callAirbnbViaGeoLocation(@RequestBody AirbnbGeoCoordinatesDto request) {
        try {
            List<HousingListing> newListings = airbnbApiService.updateHousingListingsViaGeoCoordinates(request.getAreaCorners().get(0), request.getAreaCorners().get(1), request.getAreaCorners().get(2), request.getAreaCorners().get(3), request.getCheckIn(), request.getCheckOut(), request.getNumOfPets());
            return new ResponseEntity<>(newListings, HttpStatus.CREATED);
        }
        catch (FailedApiCallException fapce) {
            return new ResponseEntity<>(fapce.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * calls RentCast location endpoint
     * @param request
     * @return
     */
    @PostMapping("/rent-cast/location")
    public ResponseEntity<?> callRentCastViaLocation(@RequestBody CityAndStateDto request) {
        try {
            List<HousingListing> newListings = rentCastAPIService.updateListingsTableViaLocation(request.getCity(), request.getStateAbv());
            return new ResponseEntity<>(newListings, HttpStatus.CREATED);
        } 
        catch (FailedApiCallException fapce) {
            return new ResponseEntity<>(fapce.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    /**
     * calls RentCast area endpoint
     * @param request
     * @return
     */
    @PostMapping("/rent-cast/area")
    public ResponseEntity<?> callRentCastViaArea(@RequestBody AreaDto request) {
        try {
            List<HousingListing> newListings = rentCastAPIService.updateListingsTableViaArea(request.getRadius(), request.getLatitude(), request.getLongitude());
            return new ResponseEntity<>(newListings, HttpStatus.CREATED);
        }
        catch (FailedApiCallException fapce) {
            return new ResponseEntity<>(fapce.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/zillow/geo-coords")
    public ResponseEntity<?> callZillowViaGeoCoordinates(@RequestBody ZillowGeoCoordinatesDto request) {
        try {
            List<HousingListing> newListings = zillowApiService.updateListingsTableViaCoordinates(request.getLatitude(), request.getLongitude());
            return new ResponseEntity<>(newListings, HttpStatus.CREATED);
        }
        catch (FailedApiCallException fapce) {
            return new ResponseEntity<>(fapce.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
}
