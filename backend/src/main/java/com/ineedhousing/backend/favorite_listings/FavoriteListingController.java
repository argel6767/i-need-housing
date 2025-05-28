package com.ineedhousing.backend.favorite_listings;

import com.ineedhousing.backend.favorite_listings.requests.AddFavoriteListingsRequest;
import com.ineedhousing.backend.jwt.JwtUtils;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Houses endpoints for Favorite Listings
 */
@Log
@RestController
@RequestMapping("/favorites")
public class FavoriteListingController {

    private final FavoriteListingService favoriteListingService;

    public FavoriteListingController(FavoriteListingService favoriteListingService) {
        this.favoriteListingService = favoriteListingService;
    }

    /**
     * returns a user's favorite listings
     * @return ResponseEntity
     */
    @GetMapping("/me")
    @RateLimiter(name = "favorites")
    public ResponseEntity<?> getAllUserFavoriteListings() {
        try {
            Long id = JwtUtils.getCurrentUserId();
            if (id.equals(-1L)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            List<FavoriteListing> favoriteListings = favoriteListingService.getAllUserFavoriteListings(id);
            return ResponseEntity.ok(favoriteListings);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * add new Listings to User list
     * @param request
     * @return ResponseEntity
     */
    @PostMapping("/listings")
    @RateLimiter(name = "favorites")
    public ResponseEntity<?> addNewFavoriteListings(@RequestBody AddFavoriteListingsRequest request) {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            List<FavoriteListing> favoriteListings = favoriteListingService.addFavoriteListings(email, request.getListings());
            return new ResponseEntity<>(favoriteListings, HttpStatus.CREATED);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * deletes listing in request from User list
     * @param id
     * @throws UsernameNotFoundException
     * @return ResponseEntity
     */
    @DeleteMapping("/listings/{id}")
    @RateLimiter(name = "favorites")
    public ResponseEntity<?> deleteFavoriteListing(@PathVariable Long id) {
        try {
            Long userId = JwtUtils.getCurrentUserId();
            List<FavoriteListing> updatedListings = favoriteListingService.deleteListing(userId, id);
            log.info("sending back:" + updatedListings);
            return new ResponseEntity<>(updatedListings, HttpStatus.OK);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * deletes all favorite listings at once
     * @throws UsernameNotFoundException
     * @return ResponseEntity
     */
    @DeleteMapping()
    @RateLimiter(name = "favorites")
    public ResponseEntity<?> deleteAllUserFavoriteListings() {
        try {
            String email = JwtUtils.getCurrentUserEmail();
            String response = favoriteListingService.deleteAllUserFavoriteListings(email);
            return ResponseEntity.ok(response);
        }
        catch (UsernameNotFoundException unfe) {
            return new ResponseEntity<>(unfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
