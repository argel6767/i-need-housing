package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.exceptions.FavoriteListingNotFoundException;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;


import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Cacheable("favorites")
    @Transactional(readOnly = true)
    public List<FavoriteListing> getAllUserFavoriteListings(String email) {
        User user = userService.getUserByEmail(email);
        return new ArrayList<>(user.getFavoriteListings());
    }

    /**
     * adds any number of new favorites to a user FavoriteListings
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

        List<FavoriteListing> currentFavorites = user.getFavoriteListings();
        currentFavorites.addAll(newFavoriteListings);
        userService.saveUser(user);
        return currentFavorites;
    }

    /**
     * deletes any number of favorite listings from the entire List
     * This can be used for up to the entire list
     * @param email
     * @param id
     * @throws UsernameNotFoundException
     * @return List<FavoriteListing>
     */
@Transactional
public List<FavoriteListing> deleteListings(String email, List<Long> ids) {
    User user = userService.getUserByEmail(email);
    List<FavoriteListing> currentListings = user.getFavoriteListings();
   // Create a set of IDs to be removed for efficient lookup
    Set<Long> idsToRemove = new HashSet<>(ids);
   // Use removeIf to filter out the listings with matching IDs
    currentListings.removeIf(listing -> idsToRemove.contains(listing.getId()));
    userService.saveUser(user);
    return currentListings;
}

    /**
     * deletes all favorite listing of users by set a users favoriteListings
     * @param email
     * @throws UsernameNotFoundException
     * @return String
     */
    public String deleteAllUserFavoriteListings(String email) {
        User user = userService.getUserByEmail(email);
        user.getFavoriteListings().clear();
        userService.saveUser(user);
        return "List successfully deleted!";
    }
}
