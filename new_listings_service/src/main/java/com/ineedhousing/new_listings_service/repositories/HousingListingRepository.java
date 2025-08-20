package com.ineedhousing.new_listings_service.repositories;

import com.ineedhousing.new_listings_service.models.HousingListing;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HousingListingRepository extends JpaRepository<HousingListing, Long> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM HousingListing l WHERE l.location = :location")
    boolean existsByLocation(@Param("location") Point location);
}
