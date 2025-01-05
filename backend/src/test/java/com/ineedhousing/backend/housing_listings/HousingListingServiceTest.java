package com.ineedhousing.backend.housing_listings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ineedhousing.backend.housing_listings.exceptions.ListingNotFoundException;

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
        Polygon mockPolygon = mock(Polygon.class);
        List<HousingListing> mockListings = List.of(mock(HousingListing.class), mock(HousingListing.class));
        when(housingListingRepository.getAllListingsInsideArea(mockPolygon)).thenReturn(mockListings);

        // Act
        List<HousingListing> result = housingListingService.getListingsInArea(mockPolygon);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(housingListingRepository, times(1)).getAllListingsInsideArea(mockPolygon);
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
        assertThrows(ListingNotFoundException.class, () -> housingListingService.getListing(listingId));
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
        assertThrows(ListingNotFoundException.class, () -> housingListingService.deleteListing(listingId));
        verify(housingListingRepository, times(1)).existsById(listingId);
        verify(housingListingRepository, never()).deleteById(anyLong());
    }
}