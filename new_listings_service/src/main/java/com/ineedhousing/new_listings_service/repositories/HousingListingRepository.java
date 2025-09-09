package com.ineedhousing.new_listings_service.repositories;

import com.ineedhousing.new_listings_service.models.HousingListing;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Collection;
import java.util.Set;

public interface HousingListingRepository extends JpaRepository<HousingListing, Long> {

    @Query("SELECT l.location FROM HousingListing l WHERE l.location IN :locations")
    Set<Point> findExistingLocations(@Param("locations") Collection<Point> locations);
}