package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.exceptions.FavoriteListingNotFoundException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
     * makes a new List for each call to adhere to functional programming
     * @param email
     * @param housingListings
     * @return List<FavoriteListing>
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

    /**
     * deletes any number of favorite listings from the entire List
     * This can be used for up to the entire list
     * returns a new list to adhere to functional programming
     * @param email
     * @param id
     * @throws UsernameNotFoundException
     * @return List<FavoriteListing>
     */
    @Transactional
    public List<FavoriteListing> deleteListings(String email, List<Long> ids) {
        User user = userService.getUserByEmail(email);
        List<FavoriteListing> currentListings = user.getFavoriteListings();
        List<FavoriteListing> removedListings = favoriteListingRepository.findAllById(ids);
        currentListings.removeAll(removedListings);

        List<FavoriteListing> newListings = new ArrayList<>(currentListings);
        user.setFavoriteListings(newListings);
        userService.saveUser(user);
        return newListings;
    }

    /**
     * deletes all favorite listing of users by set a users favoriteListings to a fresh ArrayList
     * @param email
     * @throws UsernameNotFoundException
     * @return String
     */
    public String deleteAllUserFavoriteListings(String email) {
        User user = userService.getUserByEmail(email);
        user.setFavoriteListings(new ArrayList<>());
        userService.saveUser(user);
        return "List successfully deleted!";
    }
}
