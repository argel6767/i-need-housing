package com.ineedhousing.backend.apis;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ineedhousing.backend.apis.exceptions.FailedApiCallException;
import com.ineedhousing.backend.apis.exceptions.NoListingsFoundException;
import com.ineedhousing.backend.apis.requests.AirbnbGeoCoordinatesDto;
import com.ineedhousing.backend.apis.requests.AirbnbLocationDto;
import com.ineedhousing.backend.apis.requests.AreaDto;
import com.ineedhousing.backend.apis.requests.CityAndStateDto;
import com.ineedhousing.backend.apis.requests.ZillowGeoCoordinatesDto;
import com.ineedhousing.backend.housing_listings.HousingListing;

@ExtendWith(MockitoExtension.class)
public class ExternalApisControllerTest {

    @Mock
    private AirbnbApiService airbnbApiService;
    
    @Mock
    private RentCastAPIService rentCastApiService;

    @Mock
    private ZillowApiService zillowApiService;
    
    private ExternalApisController controller;
    
    private List<HousingListing> mockListings;
    
    @BeforeEach
    void setUp() {
        controller = new ExternalApisController(airbnbApiService, rentCastApiService, zillowApiService);
        mockListings = Arrays.asList(new HousingListing(), new HousingListing());
    }
    
    @Test
    void testCallAirbnbViaLocation_Success() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2024, 1, 1);
        LocalDate checkOut = LocalDate.of(2024, 1, 7);
        AirbnbLocationDto request = new AirbnbLocationDto();
        request.setCity("New York");
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setNumOfPets(1);
        
        when(airbnbApiService.updateListingViaLocation(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyInt()
        )).thenReturn(mockListings);
        
        // Act
        ResponseEntity<?> response = controller.callAirbnbViaLocation(request);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockListings, response.getBody());
    }
    
    @Test
    void testCallAirbnbViaLocation_FailedApiCall() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2024, 1, 1);
        LocalDate checkOut = LocalDate.of(2024, 1, 7);
        AirbnbLocationDto request = new AirbnbLocationDto();
        request.setCity("New York");
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setNumOfPets(22);
        
        when(airbnbApiService.updateListingViaLocation(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyInt()
        )).thenThrow(new FailedApiCallException("API Call Failed"));
        
        // Act
        ResponseEntity<?> response = controller.callAirbnbViaLocation(request);
        
        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    void testCallAirbnbViaLocation_WithEmptyApiResponse() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2024, 1, 1);
        LocalDate checkOut = LocalDate.of(2024, 1, 7);
        AirbnbLocationDto request = new AirbnbLocationDto();
        request.setCity("New York");
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setNumOfPets(22);
        
        when(airbnbApiService.updateListingViaLocation(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyInt()
        )).thenThrow(new NoListingsFoundException("API Call Failed"));
        
        // Act
        ResponseEntity<?> response = controller.callAirbnbViaLocation(request);
        
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    
    @Test
    void testCallAirbnbViaGeoLocation_Success() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2024, 1, 1);
        LocalDate checkOut = LocalDate.of(2024, 1, 7);
        AirbnbGeoCoordinatesDto request = new AirbnbGeoCoordinatesDto();
        List<Double> corners = Arrays.asList(1.0, 2.0, 3.0, 4.0);
        request.setAreaCorners(corners);
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setNumOfPets(1);
        
        when(airbnbApiService.updateHousingListingsViaGeoCoordinates(
            anyDouble(), anyDouble(), anyDouble(), anyDouble(), 
            any(LocalDate.class), any(LocalDate.class), anyInt()
        )).thenReturn(mockListings);
        
        // Act
        ResponseEntity<?> response = controller.callAirbnbViaGeoLocation(request);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockListings, response.getBody());
    }
    
    @Test
    void testCallRentCastViaLocation_Success() {
        // Arrange
        CityAndStateDto request = new CityAndStateDto();
        request.setCity("New York");
        request.setStateAbv("NY");
        
        when(rentCastApiService.updateListingsTableViaLocation(
            anyString(), anyString()
        )).thenReturn(mockListings);
        
        // Act
        ResponseEntity<?> response = controller.callRentCastViaLocation(request);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockListings, response.getBody());
    }
    
    @Test
    void testCallRentCastViaArea_Success() {
        // Arrange
        AreaDto request = new AreaDto();
        request.setRadius(10);
        request.setLatitude(40.7128);
        request.setLongitude(-74.0060);
        
        when(rentCastApiService.updateListingsTableViaArea(
            anyInt(), anyDouble(), anyDouble()
        )).thenReturn(mockListings);
        
        // Act
        ResponseEntity<?> response = controller.callRentCastViaArea(request);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockListings, response.getBody());
    }
    
    @Test
    void testCallRentCastViaArea_NoListingsFound() {
        // Arrange
        AreaDto request = new AreaDto();
        request.setRadius(10);
        request.setLatitude(40.7128);
        request.setLongitude(-74.0060);
        when(rentCastApiService.updateListingsTableViaArea(anyInt(), anyDouble(), anyDouble())).thenThrow(new NoListingsFoundException("No listings found"));
        
        // Act
        ResponseEntity<?> response = controller.callRentCastViaArea(request);
        
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testCallZillowViaGeoCoordinates_Success() {
        //Arrange
        ZillowGeoCoordinatesDto request = new ZillowGeoCoordinatesDto(12.22, 13.22);
        when(zillowApiService.updateListingsTableViaCoordinates(anyDouble(), anyDouble())).thenReturn(mockListings);

        //Act
        ResponseEntity<?> response = controller.callZillowViaGeoCoordinates(request);

        //Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCallZillowViaGeoCoordinates_FailedApiCall() {
        //Arrange
        ZillowGeoCoordinatesDto request = new ZillowGeoCoordinatesDto(12.22, 13.22);
        when(zillowApiService.updateListingsTableViaCoordinates(anyDouble(), anyDouble())).thenThrow(new FailedApiCallException("API Call Failed"));

        //Act
        ResponseEntity<?> response = controller.callZillowViaGeoCoordinates(request);

        //Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    void testCallAirbnbViaGeoLocation_NoListingsFound() {
        //Arrange
        ZillowGeoCoordinatesDto request = new ZillowGeoCoordinatesDto(12.22, 13.22);
        when(zillowApiService.updateListingsTableViaCoordinates(anyDouble(), anyDouble())).thenThrow(new NoListingsFoundException("No listings found"));

        //Act
        ResponseEntity<?> response = controller.callZillowViaGeoCoordinates(request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

