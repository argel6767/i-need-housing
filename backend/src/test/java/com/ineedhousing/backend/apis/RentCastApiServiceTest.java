package com.ineedhousing.backend.apis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.util.UriBuilder;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;

@ExtendWith(MockitoExtension.class)
class RentCastApiServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private HousingListingRepository housingListingRepository;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock 
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private UriBuilder uriBuilder;

    @InjectMocks
    private RentCastApiService rentCastApiService;

    @Captor
    private ArgumentCaptor<List<HousingListing>> listingsCaptor;

    private GeometryFactory geometryFactory;

     @BeforeEach
    void setUp() {
        geometryFactory = GeometrySingleton.getInstance();
        // Setup common mock chain using BDDMockito
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(Function.class));
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    }

    @Nested
    @DisplayName("Location-based tests")
    class LocationBasedTests {
        
        @Test
        @DisplayName("Should successfully update listings for valid location")
        void shouldUpdateListingsForValidLocation() {
            // Given
            String city = "Seattle";
            String state = "WA";
            var mockListing = createMockListing("test123", -122.3321, 47.6062, 
                "123 Test Ave, Seattle, WA", "Apartment", 2, 2.0);
            List<Map<String, Object>> mockResponse = List.of(mockListing);

            given(responseSpec.body(any(ParameterizedTypeReference.class))).willReturn(mockResponse);
            given(housingListingRepository.saveAll(any())).willAnswer(i -> i.getArgument(0));

            // When
            List<HousingListing> result = rentCastApiService.updateListingsTableViaLocation(city, state);

            // Then
            then(housingListingRepository).should().saveAll(listingsCaptor.capture());
            List<HousingListing> savedListings = listingsCaptor.getValue();
            
            assertAll(
                () -> assertEquals(1, savedListings.size()),
                () -> {
                    HousingListing listing = savedListings.get(0);
                    assertListing(listing, "test123", -122.3321, 47.6062, 
                        "123 Test Ave, Seattle, WA", "Apartment", 2, 2.0);
                }
            );
        }

        @Test
        @DisplayName("Should throw NoListingsFoundException when no listings found")
        void shouldThrowExceptionWhenNoListingsFound() {
            // Given
            given(responseSpec.body(any(ParameterizedTypeReference.class)))
                .willReturn(Collections.emptyList());

            // When/Then
            NoListingsFoundException exception = assertThrows(
                NoListingsFoundException.class,
                () -> rentCastApiService.updateListingsTableViaLocation("NonExistent", "XX")
            );
            
            assertEquals("No listings found for NonExistent, XX.", exception.getMessage());
            then(housingListingRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Should throw FailedApiCallException when API call fails")
        void shouldThrowExceptionWhenApiCallFails() {
            // Given
            given(responseSpec.body(any(ParameterizedTypeReference.class))).willReturn(null);

            // When/Then
            FailedApiCallException exception = assertThrows(
                FailedApiCallException.class,
                () -> rentCastApiService.updateListingsTableViaLocation("Seattle", "WA")
            );
            
            assertEquals("Api call failed to occur. Check usage rates, and make sure your city and state are valid.", 
                exception.getMessage());
            then(housingListingRepository).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("Area-based tests")
    class AreaBasedTests {
        
        @Test
        @DisplayName("Should successfully update listings for valid area")
        void shouldUpdateListingsForValidArea() {
            // Given
            int radius = 5;
            double latitude = 47.6062;
            double longitude = -122.3321;
            
            var mockListing = createMockListing("area123", longitude, latitude, 
                "456 Area St, Seattle, WA", "House", 3, 2.5);
            List<Map<String, Object>> mockResponse = List.of(mockListing);

            given(responseSpec.body(any(ParameterizedTypeReference.class))).willReturn(mockResponse);
            given(housingListingRepository.saveAll(any())).willAnswer(i -> i.getArgument(0));

            // When
            List<HousingListing> result = rentCastApiService.updateListingsTableViaArea(radius, latitude, longitude);

            // Then
            then(housingListingRepository).should().saveAll(listingsCaptor.capture());
            List<HousingListing> savedListings = listingsCaptor.getValue();
            
            assertAll(
                () -> assertEquals(1, savedListings.size()),
                () -> {
                    HousingListing listing = savedListings.get(0);
                    assertListing(listing, "area123", longitude, latitude, 
                        "456 Area St, Seattle, WA", "House", 3, 2.5);
                }
            );
        }

        @Test
        @DisplayName("Should throw NoListingsFoundException when no listings found in area")
        void shouldThrowExceptionWhenNoListingsFoundInArea() {
            // Given
            given(responseSpec.body(any(ParameterizedTypeReference.class)))
                .willReturn(Collections.emptyList());

            // When/Then
            NoListingsFoundException exception = assertThrows(
                NoListingsFoundException.class,
                () -> rentCastApiService.updateListingsTableViaArea(5, 0.0, 0.0)
            );
            
            assertEquals("No listings found for coordinates (0.00, 0.00) within radius: 5.", 
                exception.getMessage());
            then(housingListingRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Should throw FailedApiCallException when area API call fails")
        void shouldThrowExceptionWhenAreaApiCallFails() {
            // Given
            given(responseSpec.body(any(ParameterizedTypeReference.class))).willReturn(null);

            // When/Then
            FailedApiCallException exception = assertThrows(
                FailedApiCallException.class,
                () -> rentCastApiService.updateListingsTableViaArea(5, 47.6062, -122.3321)
            );
            
            assertEquals("Api call failed to occur. Check usage rates, and make sure your latitude and longitude are valid.", 
                exception.getMessage());
            then(housingListingRepository).shouldHaveNoInteractions();
        }
    }

    // Helper methods
    private Map<String, Object> createMockListing(String id, double longitude, double latitude, 
            String address, String propertyType, int bedrooms, double bathrooms) {
        Map<String, Object> mockListing = new HashMap<>();
        mockListing.put("id", id);
        mockListing.put("longitude", longitude);
        mockListing.put("latitude", latitude);
        mockListing.put("formattedAddress", address);
        mockListing.put("propertyType", propertyType);
        mockListing.put("bedrooms", bedrooms);
        mockListing.put("bathrooms", bathrooms);
        return mockListing;
    }

    private void assertListing(HousingListing listing, String id, double longitude, double latitude,
            String address, String propertyType, int bedrooms, double bathrooms) {
        assertAll(
            () -> assertEquals("RentCast", listing.getSource()),
            () -> assertEquals(id, listing.getTitle()),
            () -> assertEquals(address, listing.getAddress()),
            () -> assertEquals(propertyType, listing.getPropertyType()),
            () -> assertEquals(bedrooms, listing.getNumBeds()),
            () -> assertEquals(bathrooms, listing.getNumBaths()),
            () -> {
                Point location = listing.getLocation();
                assertEquals(longitude, location.getX(), 0.0001);
                assertEquals(latitude, location.getY(), 0.0001);
            }
        );
    }
}