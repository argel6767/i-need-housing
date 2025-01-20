package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.requests.AddFavoriteListingsRequest;
import com.ineedhousing.backend.favorite_listings.requests.DeleteFavoriteListingsRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Houses endpoints for Favorite Listings
 */
@RestController
@RequestMapping("/favorites")
public class FavoriteListingController {

    private final FavoriteListingService favoriteListingService;

    public FavoriteListingController(FavoriteListingService favoriteListingService) {
        this.favoriteListingService = favoriteListingService;
    }

    /**
     * returns a user's favorite listings
     * @param email
     * @return ResponseEntity
     */
    @GetMapping("/{email}")
    public ResponseEntity<?> getAllUserFavoriteListings(@PathVariable String email) {
        try {
            List<FavoriteListing> favoriteListings = favoriteListingService.getAllUserFavoriteListings(email);
            return ResponseEntity.ok(favoriteListings);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * add new Listings to User list
     * @param email
     * @param request
     * @return ResponseEntity
     */
    @PutMapping("/{email}/listings")
    public ResponseEntity<?> addNewFavoriteListings(@PathVariable String email, @RequestBody AddFavoriteListingsRequest request) {
        try {
            List<FavoriteListing> favoriteListings = favoriteListingService.addFavoriteListings(email, request.getListings());
            return new ResponseEntity<>(favoriteListings, HttpStatus.CREATED);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * deletes listing in request from User list
     * @param email
     * @param request
     * @throws UsernameNotFoundException
     * @return ResponseEntity
     */
    @PostMapping("/{email}/listings")
    public ResponseEntity<?> deleteFavoriteListings(@PathVariable String email, @RequestBody DeleteFavoriteListingsRequest request) {
        try {
            List<FavoriteListing> updatedListings = favoriteListingService.deleteListings(email, request.getFavoriteListingIds());
            return ResponseEntity.ok(updatedListings);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 
     * @param email
     * @throws UsernameNotFoundException
     * @return ResponseEntity
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteAllUserFavoriteListings(@PathVariable String email) {
        try {
            String response = favoriteListingService.deleteAllUserFavoriteListings(email);
            return ResponseEntity.ok(response);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
