package com.ineedhousing.backend.apis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;

class RentCastApiServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private HousingListingRepository housingListingRepository;

    @InjectMocks
    private RentCastApiService rentCastApiService;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        geometryFactory = GeometrySingleton.getInstance();
    }

    @Test
    void testUpdateListingsTableViaLocation_Success() {
        // Mock data
        String city = "New York";
        String state = "NY";
        List<Map<String, Object>> mockResponse = List.of(
            Map.of(
                "id", "listing1",
                "longitude", -74.006,
                "latitude", 40.7128,
                "formattedAddress", "123 Main St, New York, NY",
                "propertyType", "Apartment",
                "bedrooms", 2,
                "bathrooms", 1.5
            )
        );

        List<HousingListing> savedListings = List.of(new HousingListing());

        // Mock RestClient and Repository behavior
        when(restClient.any())
            .thenReturn(mockResponse);

        when(housingListingRepository.saveAll(anyList())).thenReturn(savedListings);

        // Execute method
        List<HousingListing> result = rentCastApiService.updateListingsTableViaLocation(city, state);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restClient, times(1)).get();
        verify(housingListingRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdateListingsTableViaLocation_NoListingsFound() {
        // Mock data
        String city = "Unknown City";
        String state = "XX";
        List<Map<String, Object>> mockResponse = List.of();

        // Mock RestClient behavior
        when(restClient.get()
            .uri(any(URI.class))
            .retrieve()
            .body(any(ParameterizedTypeReference.class)))
            .thenReturn(mockResponse);

        // Execute and assert exception
        NoListingsFoundException exception = assertThrows(
            NoListingsFoundException.class,
            () -> rentCastApiService.updateListingsTableViaLocation(city, state)
        );
        assertEquals("No listings found for Unknown City, XX.", exception.getMessage());
    }

    @Test
    void testUpdateListingsTableViaLocation_FailedApiCall() {
        // Mock data
        String city = "New York";
        String state = "NY";

        // Mock RestClient behavior
        when(restClient.get()
            .uri(any(URI.class))
            .retrieve()
            .body(any(ParameterizedTypeReference.class)))
            .thenReturn(null);

        // Execute and assert exception
        FailedApiCallException exception = assertThrows(
            FailedApiCallException.class,
            () -> rentCastApiService.updateListingsTableViaLocation(city, state)
        );
        assertEquals("Api call failed to occur. Check usage rates, and make sure your city and state are valid.", exception.getMessage());
    }

    @Test
    void testUpdateListingsTableViaArea_Success() {
        // Mock data
        int radius = 10;
        double latitude = 40.7128;
        double longitude = -74.0060;
        List<Map<String, Object>> mockResponse = List.of(
            Map.of(
                "id", "listing2",
                "longitude", -74.006,
                "latitude", 40.7128,
                "formattedAddress", "456 Park Ave, New York, NY",
                "propertyType", "Condo",
                "bedrooms", 3,
                "bathrooms", 2.0
            )
        );

        List<HousingListing> savedListings = List.of(new HousingListing());

        // Mock RestClient and Repository behavior
        when(restClient.get()
            .uri(any(URI.class))
            .retrieve()
            .body(any(ParameterizedTypeReference.class)))
            .thenReturn(mockResponse);

        when(housingListingRepository.saveAll(anyList())).thenReturn(savedListings);

        // Execute method
        List<HousingListing> result = rentCastApiService.updateListingsTableViaArea(radius, latitude, longitude);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restClient, times(1)).get();
        verify(housingListingRepository, times(1)).saveAll(anyList());
    }
}

