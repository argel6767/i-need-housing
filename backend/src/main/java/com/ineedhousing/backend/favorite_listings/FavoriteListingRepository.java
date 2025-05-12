package com.ineedhousing.backend.favorite_listings;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteListingRepository extends JpaRepository<FavoriteListing, Long> {

    /**
     * Find all favorite listings by user ID
     * @param userId the ID of the user
     * @return List of favorite listings
     */
    @Query("SELECT fl FROM FavoriteListing fl WHERE fl.user.id = :userId")
    List<FavoriteListing> findAllByUserId(@Param("userId") Long userId);


    /**
     * Delete favorite listings by user email and listing IDs
     * ensures an authenicated user can only delete their own
     * @param userId the user's id
     * @param favoriteId the IDs of favorite listings to delete
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM FavoriteListing fl WHERE fl.user.id = :userId AND fl.id = :favoriteId")
    void deleteByUserIdAndFavoriteId(@Param("userId") Long userId, @Param("favoriteId") Long favoriteId);

    /**
     * Delete all favorite listings by user email
     * @param email
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM FavoriteListing fl WHERE fl.user.email = :email")
    void deleteByUserEmail(@Param("email") String email);

    /**
     * Find all favorite listings by user email
     * @param email the user's email
     * @return List of favorite listings
     */
    @Query("SELECT fl FROM FavoriteListing fl JOIN fl.user u WHERE u.email = :email")
    List<FavoriteListing> findAllByUserEmail(@Param("email") String email);

}
