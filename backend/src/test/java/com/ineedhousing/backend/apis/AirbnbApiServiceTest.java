package com.ineedhousing.backend.apis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;

@ExtendWith(MockitoExtension.class)
class AirbnbApiServiceTest {

    @Mock
    @Qualifier("Airbnb API")
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
    private AirbnbApiService airbnbApiService;

    @Captor
    private ArgumentCaptor<List<HousingListing>> listingsCaptor;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = GeometrySingleton.getInstance();
        // Setup common mock chain for RestClient GET usage
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
            String city = "Chicago";
            LocalDate checkIn = LocalDate.of(2024, 1, 1);
            LocalDate checkOut = LocalDate.of(2024, 1, 5);
            Integer numOfPets = 1;

            Map<String, Object> validResponse = createMockApiResponse(
                createMockListing(
                    "Lovely Apartment in the Loop",
                    120.5,
                    -87.6298,
                    41.8781,
                    "123 Downtown St, Chicago, IL",
                    "Apartment",
                    2,
                    1.5,
                    "https://airbnb-link.example.com"
                )
            );

            given(responseSpec.body(eq(Map.class))).willReturn(validResponse);
            given(housingListingRepository.saveAll(any())).willAnswer(inv -> inv.getArgument(0));

            // When
            List<HousingListing> result = airbnbApiService.updateListingViaLocation(
                city, checkIn, checkOut, numOfPets
            );

            // Then
            then(housingListingRepository).should().saveAll(listingsCaptor.capture());
            List<HousingListing> savedListings = listingsCaptor.getValue();
            assertEquals(1, savedListings.size());

            HousingListing savedListing = savedListings.get(0);
            // Verify each field is mapped correctly
            assertListing(
                savedListing,
                "Lovely Apartment in the Loop",
                120.5,
                -87.6298,
                41.8781,
                "123 Downtown St, Chicago, IL",
                "Apartment",
                2,
                1.5,
                true // Because numOfPets = 1
            );
        }

        @Test
        @DisplayName("Should throw NoListingsFoundException when no listings found")
        void shouldThrowExceptionWhenNoListingsFound() {
            // Given
            // Empty "results" in the response
            Map<String, Object> emptyResponse = new HashMap<>();

            given(responseSpec.body(eq(Map.class))).willReturn(emptyResponse);

            // When/Then
            NoListingsFoundException exception = assertThrows(
                NoListingsFoundException.class,
                () -> airbnbApiService.updateListingViaLocation(
                    "NowhereCity",
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 1, 2),
                    0
                )
            );

            assertTrue(
                exception.getMessage().contains(
                    "No listings found within NowhereCity"
                )
            );
            then(housingListingRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Should throw FailedApiCallException when API call fails (null response)")
        void shouldThrowExceptionWhenApiCallFails() {
            // Given
            // The body() call returns null
            given(responseSpec.body(eq(Map.class))).willReturn(null);

            // When/Then
            FailedApiCallException exception = assertThrows(
                FailedApiCallException.class,
                () -> airbnbApiService.updateListingViaLocation(
                    "Chicago",
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 1, 2),
                    0
                )
            );

            assertTrue(
                exception.getMessage().contains("Api call failed to occur. Check usage rates")
            );
            then(housingListingRepository).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("Geo-coordinates based tests")
    class GeoCoordinatesBasedTests {

        @Test
        @DisplayName("Should successfully update listings for valid coordinates")
        void shouldUpdateListingsForValidCoordinates() {
            // Given
            Double neLat = 41.99;
            Double neLong = -87.60;
            Double swLat = 41.84;
            Double swLong = -87.74;
            LocalDate checkIn = LocalDate.of(2024, 2, 1);
            LocalDate checkOut = LocalDate.of(2024, 2, 5);
            Integer numOfPets = 0; // No pets

            Map<String, Object> validResponse = createMockApiResponse(
                createMockListing(
                    "High-Rise Condo",
                    180.0,
                    -87.645,
                    41.88,
                    "123 Lake Shore Dr, Chicago, IL",
                    "Condo",
                    1,
                    1.0,
                    "https://airbnb-condo.example.com"
                )
            );

            given(responseSpec.body(eq(Map.class))).willReturn(validResponse);
            given(housingListingRepository.saveAll(any())).willAnswer(inv -> inv.getArgument(0));

            // When
            List<HousingListing> result = airbnbApiService.updateHousingListingsViaGeoCoordinates(
                neLat, neLong, swLat, swLong, checkIn, checkOut, numOfPets
            );

            // Then
            then(housingListingRepository).should().saveAll(listingsCaptor.capture());
            List<HousingListing> savedListings = listingsCaptor.getValue();
            assertEquals(1, savedListings.size());

            HousingListing listing = savedListings.get(0);
            assertListing(
                listing,
                "High-Rise Condo",
                180.0,
                -87.645,
                41.88,
                "123 Lake Shore Dr, Chicago, IL",
                "Condo",
                1,
                1.0,
                false
            );
        }

        @Test
        @DisplayName("Should throw NoListingsFoundException when no listings found in given coordinates")
        void shouldThrowExceptionWhenNoListingsFoundInCoordinates() {
            // Given
            Map<String, Object> emptyResponse = new HashMap<>();

            given(responseSpec.body(eq(Map.class))).willReturn(emptyResponse);

            // When/Then
            NoListingsFoundException exception = assertThrows(
                NoListingsFoundException.class,
                () -> airbnbApiService.updateHousingListingsViaGeoCoordinates(
                    42.0, -87.60, 41.8, -87.7,
                    LocalDate.of(2024, 3, 1),
                    LocalDate.of(2024, 3, 2),
                    0
                )
            );

            assertTrue(
                exception.getMessage().contains("No listings found within coordinates given")
            );
            then(housingListingRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("Should throw FailedApiCallException when API call fails (null response)")
        void shouldThrowExceptionWhenGeoApiCallFails() {
            // Given
            given(responseSpec.body(eq(Map.class))).willReturn(null);

            // When/Then
            FailedApiCallException exception = assertThrows(
                FailedApiCallException.class,
                () -> airbnbApiService.updateHousingListingsViaGeoCoordinates(
                    42.0, -87.60, 41.8, -87.7,
                    LocalDate.of(2024, 3, 1),
                    LocalDate.of(2024, 3, 2),
                    0
                )
            );

            assertTrue(
                exception.getMessage().contains("Api call failed to occur. Check usage rates")
            );
            then(housingListingRepository).shouldHaveNoInteractions();
        }
    }

    // -------------------
    // Helper methods
    // -------------------

    /**
     * Creates a mock Airbnb API "response" map that the service expects from restClient.get().body()
     * 
     * @param listingMap A single listing's data (Map<String, Object>)
     * @return A map that mimics the Airbnb API response containing "results" key
     */
    private Map<String, Object> createMockApiResponse(Map<String, Object> listingMap) {
        Map<String, Object> mockApiResponse = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();
        results.add(listingMap);
        mockApiResponse.put("results", results);
        return mockApiResponse;
    }

    /**
     * Creates a single listing map with fields that mimic Airbnb's response shape.
     */
    private Map<String, Object> createMockListing(
        String name,
        Double rate,
        Double lng,
        Double lat,
        String address,
        String type,
        Integer bedrooms,
        Double bathrooms,
        String deeplink
    ) {
        Map<String, Object> listing = new HashMap<>();
        listing.put("name", name);

        // Price info is nested as a Map in AirbnbApiService
        Map<String, Object> priceInfo = new HashMap<>();
        priceInfo.put("rate", rate);
        listing.put("price", priceInfo);

        listing.put("lng", lng);
        listing.put("lat", lat);
        listing.put("address", address);
        listing.put("type", type);
        listing.put("bedrooms", bedrooms);
        listing.put("bathrooms", bathrooms);
        listing.put("deeplink", deeplink);
        listing.put("images", Arrays.asList("https://example.com/img1.jpg", "https://example.com/img2.jpg"));

        return listing;
    }

    /**
     * Verifies each field on HousingListing matches what we expect.
     */
    private void assertListing(
        HousingListing listing,
        String expectedName,
        Double expectedRate,
        Double expectedLng,
        Double expectedLat,
        String expectedAddress,
        String expectedType,
        Integer expectedBeds,
        Double expectedBaths,
        boolean isPetFriendly
    ) {
        assertAll(
            () -> assertEquals("Airbnb", listing.getSource()),
            () -> assertEquals(expectedName, listing.getTitle()),
            () -> assertEquals(expectedRate, listing.getRate(), 0.0001),
            () -> assertEquals(expectedAddress, listing.getAddress()),
            () -> assertEquals(expectedType, listing.getPropertyType()),
            () -> assertEquals(expectedBeds, listing.getNumBeds()),
            () -> assertEquals(expectedBaths, listing.getNumBaths(), 0.0001),
            () -> assertEquals(isPetFriendly, listing.getIsPetFriendly()),
            () -> {
                Point location = listing.getLocation();
                assertEquals(expectedLng, location.getX(), 0.0001);
                assertEquals(expectedLat, location.getY(), 0.0001);
            }
        );
    }
}

