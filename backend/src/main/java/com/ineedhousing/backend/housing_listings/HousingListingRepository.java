package com.ineedhousing.backend.housing_listings;

import java.time.LocalDate;
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
public interface HousingListingRepository extends JpaRepository<HousingListing, Long> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM HousingListing l WHERE l.location = :location")
    boolean existsByLocation(@Param("location") Point location);


    @Query("SELECT l FROM HousingListing l WHERE ST_Within(l.location, :area) = true")
    List<HousingListing> getAllListingsInsideArea(@Param("area") Polygon area);

    @Query("SELECT l FROM HousingListing l WHERE ST_Within(l.location, :area) = true")
    Page<HousingListing> getAllListingsInsideArea(@Param("area") Polygon area, Pageable pageable);

    /**
     * Used for New Grads
     * @param pageable
     * @param area
     * @param maxRent
     * @param minBedrooms
     * @param minBathrooms
     * @return
     */
    @Query("SELECT l FROM HousingListing l WHERE ST_Within(l.location, :area) = true " +
            "AND l.rate IS NOT NULL " +
            "AND l.numBeds IS NOT NULL " +
            "AND l.numBaths IS NOT NULL " +
            "AND (:maxRent IS NULL OR l.rate <= :maxRent) " +
            "AND (:minBedrooms IS NULL OR l.numBeds >= :minBedrooms) " +
            "AND (:minBathrooms IS NULL OR l.numBaths >= :minBathrooms)")
    Page<HousingListing> filterListingByPreferences(Pageable pageable, @Param("area") Polygon area, @Param("maxRent") Double maxRent,
                                                    @Param("minBedrooms") Integer minBedrooms, @Param("minBathrooms") Double minBathrooms);

    /**
     * Used for interns
     * @param pageable
     * @param area
     * @param maxRent
     * @param minBedrooms
     * @param minBathrooms
     * @param internshipStart
     * @param internshipEnd
     * @return
     */
    @Query("SELECT l FROM HousingListing l WHERE ST_Within(l.location, :area) = true " +
            "AND l.rate IS NOT NULL " +
            "AND l.numBeds IS NOT NULL " +
            "AND l.numBaths IS NOT NULL " +
            "AND (:maxRent IS NULL OR l.rate <= :maxRent) " +
            "AND (:minBedrooms IS NULL OR l.numBeds >= :minBedrooms) " +
            "AND (:minBathrooms IS NULL OR l.numBaths >= :minBathrooms) " +
            "AND (l.createdAt >= :internshipStart AND l.createdAt <= :internshipEnd)")
    Page<HousingListing> filterListingByPreferences(Pageable pageable, @Param("area") Polygon area, @Param("maxRent") Double maxRent, @Param("minBedrooms") Integer minBedrooms,
                                                    @Param("minBathrooms") Double minBathrooms, @Param("internshipStart") LocalDate internshipStart, @Param("internshipEnd") LocalDate internshipEnd);
}