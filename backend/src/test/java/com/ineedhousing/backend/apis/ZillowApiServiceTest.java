package com.ineedhousing.backend.apis;

import com.ineedhousing.backend.geometry.GeometrySingleton;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ZillowApiServiceTest {

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

    @InjectMocks
    private ZillowApiService zillowApiService;

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = GeometrySingleton.getInstance();
    }

    @Test
    void testUpdateListingsTableViaCoordinates() {
        // Arrange
        Double latitude = 37.7749;
        Double longitude = -122.4194;
        
        // Mock the RestClient chain
        when(restClient.get()).thenReturn(any());
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        // Create sample API response based on the provided examples
        Map<String, Object> apiResponse = createSampleApiResponse();
        when(responseSpec.body(Map.class)).thenReturn(apiResponse);
        
        // Mock repository to indicate no duplicates
        when(housingListingRepository.existsByLocation(any(Point.class))).thenReturn(false);
        
        // Execute
        List<HousingListing> result = zillowApiService.updateListingsTableViaCoordinates(latitude, longitude);
        
        // Verify
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(any(Function.class));
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(Map.class);
        
        // Verify the result contains expected listings
        assertNotNull(result);
        assertEquals(3, result.size()); // 1 regular property + 2 units from property group
        
        // Verify property group units
        boolean foundUnit1 = false;
        boolean foundUnit2 = false;
        boolean foundSingleProperty = false;
        
        for (HousingListing listing : result) {
            if (listing.getTitle().equals("Bristol Station Apartments")) {
                if (listing.getRate() == 2204.0) {
                    // This should be the first unit from the property group
                    foundUnit1 = true;
                    assertEquals("Apartment Complex", listing.getPropertyType());
                    // Verify other fields as needed
                } else if (listing.getRate() == 2804.0) {
                    // This should be the second unit from the property group
                    foundUnit2 = true;
                    assertEquals("Apartment Complex", listing.getPropertyType());
                    // Verify other fields as needed
                }
            } else if (listing.getTitle().equals("HarborView, Harbor View")) {
                // This should be the regular property
                foundSingleProperty = true;
                assertEquals("Apartment", listing.getPropertyType());
                assertEquals(2, listing.getNumBeds());
                assertEquals(1.0, listing.getNumBaths());
                // Verify the rate from the price object
                assertNotEquals(0.0, listing.getRate());
                // Verify other fields as needed
            }
        }
        
        assertTrue(foundUnit1, "First unit from property group not found");
        assertTrue(foundUnit2, "Second unit from property group not found");
        assertTrue(foundSingleProperty, "Single property not found");
        
        // Verify that the repository was checked for duplicates
        verify(housingListingRepository, times(3)).existsByLocation(any(Point.class));
    }

    @Test
    void testUpdateListingsTableViaCoordinates_WithDuplicates() {
        // Arrange
        Double latitude = 37.7749;
        Double longitude = -122.4194;
        
        // Mock the RestClient chain
        when(restClient.get()).thenReturn(any());
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        // Create sample API response based on the provided examples
        Map<String, Object> apiResponse = createSampleApiResponse();
        when(responseSpec.body(Map.class)).thenReturn(apiResponse);
        
        // Mock repository to indicate all are duplicates
        when(housingListingRepository.existsByLocation(any(Point.class))).thenReturn(true);
        
        // Execute
        List<HousingListing> result = zillowApiService.updateListingsTableViaCoordinates(latitude, longitude);
        
        // Verify
        verify(restClient).get();
        verify(responseSpec).body(Map.class);
        
        // Verify that the duplicate check was called for each listing
        verify(housingListingRepository, times(3)).existsByLocation(any(Point.class));
        
        // Verify that no listings were returned since all were duplicates
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateListingsTableViaCoordinates_MixedDuplicates() {
        // Arrange
        Double latitude = 37.7749;
        Double longitude = -122.4194;
        
        // Mock the RestClient chain
        when(restClient.get()).thenReturn(any());
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        // Create sample API response based on the provided examples
        Map<String, Object> apiResponse = createSampleApiResponse();
        when(responseSpec.body(Map.class)).thenReturn(apiResponse);
        
        // Create a point that matches the single property's location
        Point singlePropertyPoint = geometryFactory.createPoint(
            new Coordinate(-122.4194, 37.7749));
            
        // Mock repository to indicate only the single property is a duplicate
        when(housingListingRepository.existsByLocation(any(Point.class))).thenReturn(false);
        when(housingListingRepository.existsByLocation(eq(singlePropertyPoint))).thenReturn(true);
        
        // Use ArgumentCaptor to capture the points being checked
        ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
        
        // Execute
        List<HousingListing> result = zillowApiService.updateListingsTableViaCoordinates(latitude, longitude);
        
        // Verify
        verify(housingListingRepository, times(3)).existsByLocation(pointCaptor.capture());
        
        // Verify that we still have the 2 units from the property group
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify all listings are from the property group
        for (HousingListing listing : result) {
            assertEquals("Bristol Station Apartments", listing.getTitle());
            assertEquals("Apartment Complex", listing.getPropertyType());
        }
    }

    // Helper methods to create sample API responses
    private Map<String, Object> createSampleApiResponse() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> searchResults = new ArrayList<>();
        
        // Add the property group example
        searchResults.add(createSamplePropertyGroup());
        
        // Add the regular property example
        searchResults.add(createSampleRegularProperty());
        
        response.put("searchResults", searchResults);
        return response;
    }
    
    private Map<String, Object> createSamplePropertyGroup() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> property = new HashMap<>();
        
        // Property details
        property.put("zpid", 448636115);
        property.put("title", "Bristol Station Apartments");
        property.put("isFeatured", true);
        property.put("isShowcaseListing", false);
        property.put("currency", "usd");
        property.put("country", "usa");
        property.put("groupType", "apartmentComplex");
        property.put("listingDateTimeOnZillow", 1742569063025L);
        property.put("bestGuessTimeZone", "America/New_York");
        property.put("isUnmappable", false);
        property.put("matchingHomeCount", 12);
        property.put("listingStatus", "forRent");
        property.put("providerListingID", "4bkmcvw7qdhvv");
        property.put("minPrice", 2204);
        property.put("maxPrice", 2804);
        
        // Location
        Map<String, Double> location = new HashMap<>();
        location.put("latitude", 37.7750);
        location.put("longitude", -122.4195);
        property.put("location", location);
        
        // Address
        Map<String, String> address = new HashMap<>();
        address.put("streetAddress", "123 Bristol St");
        address.put("city", "San Francisco");
        address.put("state", "CA");
        address.put("zipcode", "94107");
        address.put("country", "USA");
        property.put("address", address);
        
        // Media
        Map<String, Object> media = new HashMap<>();
        Map<String, List<String>> allPropertyPhotos = new HashMap<>();
        allPropertyPhotos.put("highResolution", Arrays.asList("img1.jpg", "img2.jpg"));
        media.put("allPropertyPhotos", allPropertyPhotos);
        property.put("media", media);
        
        // Units group
        List<Map<String, Object>> unitsGroup = new ArrayList<>();
        
        // Unit 1
        Map<String, Object> unit1 = new HashMap<>();
        unit1.put("minPrice", 2204.0);
        unit1.put("bedrooms", 1);
        unitsGroup.add(unit1);
        
        // Unit 2
        Map<String, Object> unit2 = new HashMap<>();
        unit2.put("minPrice", 2804.0);
        unit2.put("bedrooms", 2);
        unitsGroup.add(unit2);
        
        property.put("unitsGroup", unitsGroup);
        
        result.put("property", property);
        result.put("resultType", "propertyGroup");
        
        return result;
    }
    
    private Map<String, Object> createSampleRegularProperty() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> property = new HashMap<>();
        
        // Property details
        property.put("zpid", 402622965);
        property.put("title", "HarborView, Harbor View");
        property.put("isFeatured", true);
        property.put("isShowcaseListing", false);
        property.put("currency", "usd");
        property.put("country", "usa");
        property.put("propertyType", "apartment");
        property.put("listingDateTimeOnZillow", 1722265984784L);
        property.put("bestGuessTimeZone", "America/New_York");
        property.put("isUnmappable", false);
        property.put("bathrooms", 1);
        property.put("bedrooms", 2);
        property.put("livingArea", 935);
        property.put("daysOnZillow", 236);
        property.put("isPreforeclosureAuction", false);
        
        // Location
        Map<String, Double> location = new HashMap<>();
        location.put("latitude", 37.7749);
        location.put("longitude", -122.4194);
        property.put("location", location);
        
        // Address
        Map<String, String> address = new HashMap<>();
        address.put("streetAddress", "456 Harbor View");
        address.put("city", "San Francisco");
        address.put("state", "CA");
        address.put("zipcode", "94105");
        address.put("country", "USA");
        property.put("address", address);
        
        // Media
        Map<String, Object> media = new HashMap<>();
        Map<String, List<String>> allPropertyPhotos = new HashMap<>();
        allPropertyPhotos.put("highResolution", Arrays.asList("img3.jpg", "img4.jpg"));
        media.put("allPropertyPhotos", allPropertyPhotos);
        property.put("media", media);
        
        // Price
        Map<String, Object> price = new HashMap<>();
        price.put("value", 3500.0);
        property.put("price", price);
        
        result.put("property", property);
        result.put("resultType", "property");
        
        return result;
    }
}