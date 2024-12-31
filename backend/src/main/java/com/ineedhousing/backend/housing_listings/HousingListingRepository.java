package com.ineedhousing.backend.housing_listings;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousingListingRepository extends JpaRepository<HousingListing, Long>{

    public boolean existsByLocation(Point locationPoint);
}