package com.ineedhousing.backend.favorite_listings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteListingRepository extends JpaRepository<FavoriteListing, Long> {
}
