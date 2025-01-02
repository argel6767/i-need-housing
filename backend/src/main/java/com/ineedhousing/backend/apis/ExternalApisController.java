package com.ineedhousing.backend.apis;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.apis.requests.AirbnbGeoCoordinateRequest;
import com.ineedhousing.backend.apis.requests.AirbnbLocationRequest;
import com.ineedhousing.backend.apis.requests.AreaRequest;
import com.ineedhousing.backend.apis.requests.CityAndStateRequest;
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

    public ExternalApisController (AirbnbApiService airbnbApiService, RentCastAPIService rentCastAPIService) {
        this.airbnbApiService = airbnbApiService;
        this.rentCastAPIService = rentCastAPIService;
    }

    /**
     * calls Airbnb location endpoint
     * @param request
     * @return
     */
    @PostMapping("/airbnb/location")
    public ResponseEntity<?> callAirbnbViaLocation(@RequestBody AirbnbLocationRequest request) {
        try {
            List<HousingListing> newListings = airbnbApiService.updateListingViaLocation(request.getCity(), request.getCheckIn(), request.getCheckOut(), request.getNumOfPets());
            return ResponseEntity.ok(newListings);
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
    public ResponseEntity<?> callAirbnbViaGeoLocation(@RequestBody AirbnbGeoCoordinateRequest request) {
        try {
            List<HousingListing> newListings = airbnbApiService.updateHousingListingsViaGeoCoordinates(request.getAreaCorners().get(0), request.getAreaCorners().get(1), request.getAreaCorners().get(2), request.getAreaCorners().get(3), request.getCheckIn(), request.getCheckOut(), request.getNumOfPets());
            return ResponseEntity.ok(newListings);
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
    public ResponseEntity<?> callRentCastViaLocation(@RequestBody CityAndStateRequest request) {
        try {
            List<HousingListing> newListing = rentCastAPIService.updateListingsTableViaLocation(request.getCity(), request.getStateAbv());
            return ResponseEntity.ok(newListing);
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
    public ResponseEntity<?> callRentCastViaArea(@RequestBody AreaRequest request) {
        try {
            List<HousingListing> newListing = rentCastAPIService.updateListingsTableViaArea(request.getRadius(), request.getLatitude(), request.getLongitude());
            return ResponseEntity.ok(newListing);
        } 
        catch (FailedApiCallException fapce) {
            return new ResponseEntity<>(fapce.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (NoListingsFoundException nlfe) {
            return new ResponseEntity<>(nlfe.getMessage(), HttpStatus.NO_CONTENT);
        }
    }
    
}
