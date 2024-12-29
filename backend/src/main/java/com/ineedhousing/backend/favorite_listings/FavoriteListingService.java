package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds business logic for Favorite Listings
 */
@Service
public class FavoriteListingService {
    private final FavoriteListingRepository favoriteListingRepository;
    private final UserService userService;

    public FavoriteListingService(FavoriteListingRepository favoriteListingRepository, UserService userService) {
        this.favoriteListingRepository = favoriteListingRepository;
        this.userService = userService;
    }

    /**
     * gets all Users favorite listing via their email
     * @param email
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
     * @return List<FavoriteListing>
     */
    public List<FavoriteListing> getAllUserFavoriteListings(String email) {
        User user = userService.getUserByEmail(email);
        return user.getFavoriteListings();
    }

    /**
     * adds any number of new favorites to a user FavoriteListings
     * makes a new List for each call to adhere to functional style
     * @param email
     * @param housingListings
     * @return
     */
    @Transactional
    public List<FavoriteListing> addFavoriteListings(String email, List<HousingListing> housingListings) {
        User user = userService.getUserByEmail(email);
        List<FavoriteListing> newFavoriteListings = new ArrayList<>();
        housingListings.forEach(housingListing -> {
            FavoriteListing favoriteListing = new FavoriteListing(user, housingListing);
            newFavoriteListings.add(favoriteListing);
        });

        List<FavoriteListing> updatedFavorites = Stream.concat(user.getFavoriteListings().stream(), newFavoriteListings.stream()).toList();
        user.setFavoriteListings(updatedFavorites);
        userService.saveUser(user);
        return updatedFavorites;
    }

}
