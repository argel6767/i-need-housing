package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.FavoriteListing;
import com.ineedhousing.backend.favorite_listings.FavoriteListingService;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user.UserType;
import com.ineedhousing.backend.user.requests.SetUserTypeRequest;
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

class FavoriteListingServiceTest {

    @Mock
    private FavoriteListingRepository favoriteListingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoriteListingService favoriteListingService;

    private User testUser;
    private HousingListing testHousingListing;
    private FavoriteListing testFavoriteListing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@example.com", "hashedPassword");
        testUser.setId(1L);
        testUser.setUserType(UserType.INTERN);

        testHousingListing = new HousingListing();
        testHousingListing.setId(100L);

        testFavoriteListing = new FavoriteListing(testUser, testHousingListing);
        testUser.setFavoriteListings(Collections.singletonList(testFavoriteListing));
    }

    @Test
    void getAllUserFavoriteListings_whenUserExists_returnsFavoriteListings() {
        // Arrange
        String email = "test@example.com";
        when(userService.getUserByEmail(email)).thenReturn(testUser);

        // Act
        List<FavoriteListing> favoriteListings = favoriteListingService.getAllUserFavoriteListings(email);

        // Assert
        assertNotNull(favoriteListings);
        assertEquals(1, favoriteListings.size());
        assertEquals(testFavoriteListing, favoriteListings.get(0));
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void getAllUserFavoriteListings_whenUserDoesNotExist_throwsException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userService.getUserByEmail(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> favoriteListingService.getAllUserFavoriteListings(email));
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void addFavoriteListings_whenUserExists_addsListings() {
        // Arrange
        String email = "test@example.com";
        List<HousingListing> newListings = Collections.singletonList(testHousingListing);

        when(userService.getUserByEmail(email)).thenReturn(testUser);
        when(userService.saveUser(testUser)).thenReturn(testUser);

        // Act
        List<FavoriteListing> updatedFavorites = favoriteListingService.addFavoriteListings(email, newListings);

        // Assert
        assertNotNull(updatedFavorites);
        assertEquals(2, updatedFavorites.size());
        verify(userService, times(1)).getUserByEmail(email);
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void addFavoriteListings_whenUserDoesNotExist_throwsException() {
        // Arrange
        String email = "nonexistent@example.com";
        List<HousingListing> newListings = Collections.singletonList(testHousingListing);

        when(userService.getUserByEmail(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> favoriteListingService.addFavoriteListings(email, newListings));
        verify(userService, times(1)).getUserByEmail(email);
    }
}
