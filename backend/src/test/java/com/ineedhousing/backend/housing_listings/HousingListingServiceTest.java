package com.ineedhousing.backend.housing_listings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ineedhousing.backend.housing_listings.exceptions.NoListingFoundException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.user_search_preferences.UserPreference;

class HousingListingServiceTest {

    @Mock
    private HousingListingRepository housingListingRepository;

    @InjectMocks
    private HousingListingService housingListingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetListingsInArea() {
        // Arrange
        List<HousingListing> mockListings = List.of(mock(HousingListing.class), mock(HousingListing.class));
        when(housingListingRepository.getAllListingsInsideArea(any(Polygon.class))).thenReturn(mockListings);
        // Act
        List<HousingListing> result = housingListingService.getListingsInArea(1.0, 1.0, 1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(housingListingRepository, times(1)).getAllListingsInsideArea(any(Polygon.class));
    }

    @Test
    void testGetListing_Success() {
        // Arrange
        Long listingId = 1L;
        HousingListing mockListing = mock(HousingListing.class);
        when(housingListingRepository.findById(listingId)).thenReturn(Optional.of(mockListing));

        // Act
        HousingListing result = housingListingService.getListing(listingId);

        // Assert
        assertNotNull(result);
        assertEquals(mockListing, result);
        verify(housingListingRepository, times(1)).findById(listingId);
    }

    @Test
    void testGetListing_NotFound() {
        // Arrange
        Long listingId = 1L;
        when(housingListingRepository.findById(listingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoListingFoundException.class, () -> housingListingService.getListing(listingId));
        verify(housingListingRepository, times(1)).findById(listingId);
    }

    @Test
    void testDeleteListing_Success() {
        // Arrange
        Long listingId = 1L;
        when(housingListingRepository.existsById(listingId)).thenReturn(true);

        // Act
        String result = housingListingService.deleteListing(listingId);

        // Assert
        assertEquals("Listing successfully deleted.", result);
        verify(housingListingRepository, times(1)).existsById(listingId);
        verify(housingListingRepository, times(1)).deleteById(listingId);
    }

    @Test
    void testDeleteListing_NotFound() {
        // Arrange
        Long listingId = 1L;
        when(housingListingRepository.existsById(listingId)).thenReturn(false);

        // Act & Assert
        assertThrows(NoListingFoundException.class, () -> housingListingService.deleteListing(listingId));
        verify(housingListingRepository, times(1)).existsById(listingId);
        verify(housingListingRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetListingsByPreferences_Success() {
        // Arrange
        double latitude = 1.0;
        double longitude = 1.0;
        int radius = 1;
        UserPreference mockPreference = mock(UserPreference.class);
        List<HousingListing> mockAreaListings = List.of(mock(HousingListing.class), mock(HousingListing.class));
        List<HousingListing> mockFilteredListings = List.of(mock(HousingListing.class));
        
        // Mock the getListingsInArea method
        when(housingListingRepository.getAllListingsInsideArea(any(Polygon.class))).thenReturn(mockAreaListings);
        
        // Create a mock filter method
        BiFunction<UserPreference, List<HousingListing>, List<HousingListing>> mockFilterMethod = 
            (pref, listings) -> mockFilteredListings;

        // Act
        List<HousingListing> result = housingListingService.getListingsByPreferences(
            latitude, longitude, radius, mockPreference, mockFilterMethod);

        // Assert
        assertNotNull(result);
        assertEquals(mockFilteredListings, result);
        assertEquals(1, result.size());
        verify(housingListingRepository, times(1)).getAllListingsInsideArea(any(Polygon.class));
    }

    @Test
    void testGetListingsByPreferences_NoListingsFound() {
        // Arrange
        double latitude = 1.0;
        double longitude = 1.0;
        int radius = 1;
        UserPreference mockPreference = mock(UserPreference.class);
        List<HousingListing> mockAreaListings = List.of(mock(HousingListing.class));
        
        // Mock the getListingsInArea method
        Polygon mockPolygon = mock(Polygon.class);
        when(housingListingRepository.getAllListingsInsideArea(mockPolygon)).thenReturn(mockAreaListings);
        
        // Create a mock filter method that returns empty list
        BiFunction<UserPreference, List<HousingListing>, List<HousingListing>> mockFilterMethod = 
            (pref, listings) -> List.of();

        // Act & Assert
            assertThrows(NoListingFoundException.class, 
            () -> housingListingService.getListingsByPreferences(
                latitude, longitude, radius, mockPreference, mockFilterMethod));
                verify(housingListingRepository, times(1)).getAllListingsInsideArea(any(Polygon.class));
    }

    @Test
    void testGetListingsBySpecificPreference_Success() {
        // Arrange
        double latitude = 1.0;
        double longitude = 1.0;
        int radius = 1;
        Map<String, Object> preference = Map.of("rate", 2000.0);
        List<HousingListing> mockAreaListings = List.of(
            createMockListing(1500.0),
            createMockListing(2500.0)
        );
        
        // Mock the getListingsInArea method
        when(housingListingRepository.getAllListingsInsideArea(any(Polygon.class))).thenReturn(mockAreaListings);
        // Act
        List<HousingListing> result = housingListingService.getListingsBySpecificPreference(
            latitude, longitude, radius, preference);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(housingListingRepository, times(1)).getAllListingsInsideArea(any(Polygon.class));
    }

    @Test
    void testGetListingsBySpecificPreference_NoListingsFound() {
        // Arrange
        double latitude = 1.0;
        double longitude = 1.0;
        int radius = 1;
        Map<String, Object> preference = Map.of("rate", 500.0);  // Very low rate that won't match
        List<HousingListing> mockAreaListings = List.of(
            createMockListing(1500.0),
            createMockListing(2500.0)
        );
        
        // Mock the getListingsInArea method
        when(housingListingRepository.getAllListingsInsideArea(any(Polygon.class))) .thenReturn(mockAreaListings);

        // Act & Assert
        NoListingsFoundException exception = assertThrows(NoListingsFoundException.class, 
            () -> housingListingService.getListingsBySpecificPreference(
                latitude, longitude, radius, preference));
        
        assertTrue(exception.getMessage().contains("No Listings found for given preference"));
        verify(housingListingRepository, times(1)).getAllListingsInsideArea(any(Polygon.class));
    }

    // Helper method to create mock listings with specific rates
    private HousingListing createMockListing(double rate) {
        HousingListing mockListing = new HousingListing();
        mockListing.setRate(rate);
        return mockListing;
    }

}