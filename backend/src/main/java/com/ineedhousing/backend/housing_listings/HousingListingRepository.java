package com.ineedhousing.backend.housing_listings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingListingRepository extends JpaRepository<HousingListing, Long>{

    
}