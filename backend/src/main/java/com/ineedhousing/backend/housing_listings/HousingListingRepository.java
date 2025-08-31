package com.ineedhousing.backend.housing_listings;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HousingListingRepository extends JpaRepository<HousingListing, Long>{

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM HousingListing l WHERE l.location = :location")
    boolean existsByLocation(@Param("location") Point location);


    @Query("SELECT l FROM HousingListing l WHERE ST_Within(l.location, :area) = true")
    List<HousingListing> getAllListingsInsideArea(@Param("area") Polygon area);

    @Query("SELECT l FROM HousingListing l WHERE ST_Within(l.location, :area) = true")
    Page<HousingListing> getAllListingsInsideArea(@Param("area") Polygon area, Pageable pageable);

}