package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.requests.AddFavoriteListingsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Houses endpoints for Favorite Listings
 */
@RestController
@RequestMapping("/favorite")
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

    @PutMapping("/{email}")
    public ResponseEntity<?> addUFavoriteListings(@PathVariable String email, @RequestBody AddFavoriteListingsRequest request) {
        try {
            List<FavoriteListing> favoriteListings = favoriteListingService.addFavoriteListings(email, request.getListings());
            return ResponseEntity.ok(favoriteListings);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
