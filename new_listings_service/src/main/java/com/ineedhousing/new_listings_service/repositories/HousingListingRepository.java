package com.ineedhousing.new_listings_service.repositories;

import com.ineedhousing.new_listings_service.models.HousingListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingListingRepository extends JpaRepository<Long, HousingListing> {
}
