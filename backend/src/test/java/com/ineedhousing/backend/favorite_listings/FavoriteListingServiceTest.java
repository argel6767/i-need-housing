package com.ineedhousing.backend.favorite_listings;


import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.ineedhousing.backend.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<FavoriteListing> favoriteListings = favoriteListingService.getAllUserFavoriteListings(1L);

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
        assertThrows(UsernameNotFoundException.class, () -> favoriteListingService.getAllUserFavoriteListings(1L));
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

    @Test
    void deleteListings_whenUserExists_removesListings() {
        // Arrange
        String email = "test@example.com";
        List<Long> idsToDelete = Arrays.asList(1L, 2L);
        FavoriteListing anotherListing = new FavoriteListing(testUser, new HousingListing());
        anotherListing.setId(2L);

        testUser.setFavoriteListings(new ArrayList<>(List.of(testFavoriteListing, anotherListing)));

        when(userService.getUserByEmail(email)).thenReturn(testUser);
        when(favoriteListingRepository.findAllById(idsToDelete)).thenReturn(Arrays.asList(testFavoriteListing, anotherListing));
        when(userService.saveUser(testUser)).thenReturn(testUser);

        // Act
        List<FavoriteListing> updatedFavorites = favoriteListingService.deleteListings(email, idsToDelete);

        // Assert
        assertNotNull(updatedFavorites);
        assertTrue(updatedFavorites.isEmpty());
        verify(userService, times(1)).getUserByEmail(email);
        verify(favoriteListingRepository, times(1)).findAllById(idsToDelete);
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void deleteListings_whenUserDoesNotExist_throwsException() {
        // Arrange
        String email = "nonexistent@example.com";
        List<Long> idsToDelete = Arrays.asList(1L, 2L);

        when(userService.getUserByEmail(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> favoriteListingService.deleteListings(email, idsToDelete));
        verify(userService, times(1)).getUserByEmail(email);
    }

    @Test
    void deleteAllUserFavoriteListings_whenUserExists_clearsFavorites() {
        // Arrange
        String email = "test@example.com";
        when(userService.getUserByEmail(email)).thenReturn(testUser);
        when(userService.saveUser(testUser)).thenReturn(testUser);

        // Act
        String result = favoriteListingService.deleteAllUserFavoriteListings(email);

        // Assert
        assertEquals("List successfully deleted!", result);
        assertTrue(testUser.getFavoriteListings().isEmpty());
        verify(userService, times(1)).getUserByEmail(email);
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void deleteAllUserFavoriteListings_whenUserDoesNotExist_throwsException() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userService.getUserByEmail(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> favoriteListingService.deleteAllUserFavoriteListings(email));
        verify(userService, times(1)).getUserByEmail(email);
    }
    
}
