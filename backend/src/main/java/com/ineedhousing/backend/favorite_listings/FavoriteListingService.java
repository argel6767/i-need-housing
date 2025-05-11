package com.ineedhousing.backend.favorite_listings;


import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;



import java.util.ArrayList;
import java.util.List;

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
     * @param id
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
     * @return List<FavoriteListing>
     */
    @Cacheable("favorites")
    @Transactional(readOnly = true)
    public List<FavoriteListing> getAllUserFavoriteListings(Long id) {
        return new ArrayList<>(favoriteListingRepository.findAllByUserId(id));
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
        favoriteListingRepository.saveAll(newFavoriteListings);

        return favoriteListingRepository.findAllByUserEmail(email);
    }

    /**
     * deletes any number of favorite listings from the entire List
     * This can be used for up to the entire list
     * @param email
     * @param ids
     * @throws UsernameNotFoundException
     * @return List<FavoriteListing>
     */
@Transactional
public List<FavoriteListing> deleteListings(String email, List<Long> ids) {
    favoriteListingRepository.deleteByUserEmailAndIdIn(email, ids);
    return favoriteListingRepository.findAllByUserEmail(email); //updated list
}

    /**
     * deletes all favorite listing of users by email
     * @param email
     * @throws UsernameNotFoundException
     * @return String
     */
    public String deleteAllUserFavoriteListings(String email) {
        favoriteListingRepository.deleteByUserEmail(email);
        return "List successfully deleted!";
    }
}
