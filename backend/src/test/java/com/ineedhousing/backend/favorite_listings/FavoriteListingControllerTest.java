package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.requests.AddFavoriteListingsRequest;
import com.ineedhousing.backend.favorite_listings.requests.DeleteFavoriteListingsRequest;
import com.ineedhousing.backend.housing_listings.HousingListing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteListingControllerTest {

    @Mock
    private FavoriteListingService favoriteListingService;

    @InjectMocks
    private FavoriteListingController favoriteListingController;

    private FavoriteListing testFavoriteListing;
    private AddFavoriteListingsRequest addRequest;
    private DeleteFavoriteListingsRequest deleteRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testFavoriteListing = new FavoriteListing();
        testFavoriteListing.setId(1L);

        addRequest = new AddFavoriteListingsRequest(Collections.singletonList(new HousingListing()));
        deleteRequest = new DeleteFavoriteListingsRequest(Collections.singletonList(1L));
    }

    @Test
    void getAllUserFavoriteListings_whenUserExists_returnsListings() {
        // Arrange
        String email = "test@example.com";
        when(favoriteListingService.getAllUserFavoriteListings(email)).thenReturn(Collections.singletonList(testFavoriteListing));

        // Act
        ResponseEntity<?> response = favoriteListingController.getAllUserFavoriteListings(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<FavoriteListing> listings = (List<FavoriteListing>) response.getBody();
        assertEquals(1, listings.size());
        verify(favoriteListingService, times(1)).getAllUserFavoriteListings(email);
    }

    @Test
    void getAllUserFavoriteListings_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(favoriteListingService.getAllUserFavoriteListings(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = favoriteListingController.getAllUserFavoriteListings(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(favoriteListingService, times(1)).getAllUserFavoriteListings(email);
    }

    @Test
    void addNewFavoriteListings_whenUserExists_addsListings() {
        // Arrange
        String email = "test@example.com";
        when(favoriteListingService.addFavoriteListings(email, addRequest.getListings())).thenReturn(Collections.singletonList(testFavoriteListing));

        // Act
        ResponseEntity<?> response = favoriteListingController.addNewFavoriteListings(email, addRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<FavoriteListing> listings = (List<FavoriteListing>) response.getBody();
        assertEquals(1, listings.size());
        verify(favoriteListingService, times(1)).addFavoriteListings(email, addRequest.getListings());
    }

    @Test
    void addNewFavoriteListings_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(favoriteListingService.addFavoriteListings(email, addRequest.getListings())).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = favoriteListingController.addNewFavoriteListings(email, addRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(favoriteListingService, times(1)).addFavoriteListings(email, addRequest.getListings());
    }

    @Test
    void deleteFavoriteListings_whenUserExists_deletesListings() {
        // Arrange
        String email = "test@example.com";
        when(favoriteListingService.deleteListings(email, deleteRequest.getFavoriteListingIds())).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<?> response = favoriteListingController.deleteFavoriteListings(email, deleteRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<FavoriteListing> updatedListings = (List<FavoriteListing>) response.getBody();
        assertTrue(updatedListings.isEmpty());
        verify(favoriteListingService, times(1)).deleteListings(email, deleteRequest.getFavoriteListingIds());
    }

    @Test
    void deleteFavoriteListings_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(favoriteListingService.deleteListings(email, deleteRequest.getFavoriteListingIds())).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = favoriteListingController.deleteFavoriteListings(email, deleteRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(favoriteListingService, times(1)).deleteListings(email, deleteRequest.getFavoriteListingIds());
    }

    @Test
    void deleteAllUserFavoriteListings_whenUserExists_clearsListings() {
        // Arrange
        String email = "test@example.com";
        when(favoriteListingService.deleteAllUserFavoriteListings(email)).thenReturn("List successfully deleted!");

        // Act
        ResponseEntity<?> response = favoriteListingController.deleteAllUserFavoriteListings(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("List successfully deleted!", response.getBody());
        verify(favoriteListingService, times(1)).deleteAllUserFavoriteListings(email);
    }

    @Test
    void deleteAllUserFavoriteListings_whenUserDoesNotExist_returnsNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(favoriteListingService.deleteAllUserFavoriteListings(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = favoriteListingController.deleteAllUserFavoriteListings(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(favoriteListingService, times(1)).deleteAllUserFavoriteListings(email);
    }
    
}
