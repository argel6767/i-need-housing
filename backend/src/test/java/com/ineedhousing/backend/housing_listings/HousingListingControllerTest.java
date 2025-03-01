package com.ineedhousing.backend.housing_listings;

import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.requests.ExactPreferencesDto;
import com.ineedhousing.backend.housing_listings.requests.GetListingsByPreferenceRequest;
import com.ineedhousing.backend.housing_listings.requests.GetListingsInAreaRequest;
import com.ineedhousing.backend.housing_listings.utils.UserPreferencesFilterer;
import com.ineedhousing.backend.housing_listings.exceptions.NoListingFoundException;
import com.ineedhousing.backend.user_search_preferences.UserPreference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HousingListingControllerTest {

    private HousingListingController controller;
    private HousingListingService housingListingService;

    @BeforeEach
    void setUp() {
        housingListingService = mock(HousingListingService.class);
        controller = new HousingListingController(housingListingService);
    }

    private HousingListing createListing(Long id, Double rate) {
        HousingListing listing = new HousingListing();
        listing.setId(id);
        listing.setRate(rate);
        return listing;
    }

    @Test
    @DisplayName("getListingsInArea - Success")
    void getListingsInArea_Success() {
        // Arrange
        List<HousingListing> listings = List.of(
            createListing(1L, 1000.0),
            createListing(2L, 1500.0)
        );
        GetListingsInAreaRequest request = new GetListingsInAreaRequest(1.0, 2.0, 3);

        when(housingListingService.getListingsInArea(1.0, 2.0, 3)).thenReturn(listings);

        // Act
        ResponseEntity<?> response = controller.getListingsInArea(request.getLatitude(), request.getLongitude(), request.getRadius());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listings, response.getBody());
        verify(housingListingService, times(1)).getListingsInArea(1.0, 2.0, 3);
    }

    @Test
    @DisplayName("getListingsInArea - No Listings Found")
    void getListingsInArea_NoListingsFound() {
        // Arrange
        GetListingsInAreaRequest request = new GetListingsInAreaRequest(1.0, 2.0, 3);

        when(housingListingService.getListingsInArea(1.0, 2.0, 3))
            .thenThrow(new NoListingsFoundException("No listings in area"));

        // Act
        ResponseEntity<?> response = controller.getListingsInArea(request.getLatitude(), request.getLongitude(), request.getRadius());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No listings in area", response.getBody());
        verify(housingListingService, times(1)).getListingsInArea(1.0, 2.0, 3);
    }

    @Test
    @DisplayName("getListing - Success")
    void getListing_Success() {
        // Arrange
        HousingListing listing = createListing(1L, 1200.0);

        when(housingListingService.getListing(1L)).thenReturn(listing);

        // Act
        ResponseEntity<?> response = controller.getListing(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listing, response.getBody());
        verify(housingListingService, times(1)).getListing(1L);
    }

    @Test
    @DisplayName("getListing - Not Found")
    void getListing_NotFound() {
        // Arrange
        when(housingListingService.getListing(999L))
            .thenThrow(new NoListingFoundException("Listing not found"));

        // Act
        ResponseEntity<?> response = controller.getListing(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Listing not found", response.getBody());
        verify(housingListingService, times(1)).getListing(999L);
    }

    @Test
    @DisplayName("deleteListing - Success")
    void deleteListing_Success() {
        // Arrange
        when(housingListingService.deleteListing(1L))
            .thenReturn("Deleted listing with id 1");

        // Act
        ResponseEntity<?> response = controller.deleteListing(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted listing with id 1", response.getBody());
        verify(housingListingService, times(1)).deleteListing(1L);
    }

    @Test
    @DisplayName("deleteListing - Not Found")
    void deleteListing_NotFound() {
        // Arrange
        when(housingListingService.deleteListing(999L))
            .thenThrow(new NoListingFoundException("Listing not found"));

        // Act
        ResponseEntity<?> response = controller.deleteListing(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Listing not found", response.getBody());
        verify(housingListingService, times(1)).deleteListing(999L);
    }

    @Test
    @DisplayName("getListingWithExactPreferences - Success")
    void getListingWithExactPreferences_Success() {
        // Arrange
        List<HousingListing> listings = List.of(createListing(1L, 1800.0));
        ExactPreferencesDto request =  new ExactPreferencesDto(List.of(
            createListing(1L, 1800.0),
            createListing(2L, 1500.0)
        ),  1L);

        when(housingListingService.getListingsByPreferences(
            request.getId(), request.getListings(), UserPreferencesFilterer::findByExactPreferences))
            .thenReturn(listings);

        // Act
        ResponseEntity<?> response = controller.getListingWithExactPreferences(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listings, response.getBody());
    }

    @Test
    @DisplayName("getListingWithExactPreferences - No Listings Found")
    void getListingWithExactPreferences_NoListingsFound() {
        // Arrange
        ExactPreferencesDto request =  new ExactPreferencesDto(List.of(
            createListing(1L, 1800.0),
            createListing(2L, 1500.0)
        ),  1L);

        when(housingListingService.getListingsByPreferences(
            request.getId(), request.getListings(), UserPreferencesFilterer::findByExactPreferences))
            .thenThrow(new NoListingsFoundException("No Listings found for given preferences"));

        // Act
        ResponseEntity<?> response = controller.getListingWithExactPreferences(request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No Listings found for given preferences", response.getBody());
    }

}

