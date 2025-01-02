package com.ineedhousing.backend.housing_listings;

import org.springframework.stereotype.Service;

@Service
public class HousingListingService {
    private final HousingListingRepository housingListingRepository;

    public HousingListingService(HousingListingRepository housingListingRepository) {
        this.housingListingRepository = housingListingRepository;
    }
}
