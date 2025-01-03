package com.ineedhousing.backend.housing_listings;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HousingListingRepository extends JpaRepository<HousingListing, Long>{

    public boolean existsByLocation(Point locationPoint);

    @Query("SELECT l FROM HousingListing WHERE WITHIN(l.location, :area) = true")
    public List<HousingListing> findAllListingsInArea(Polygon area);
}