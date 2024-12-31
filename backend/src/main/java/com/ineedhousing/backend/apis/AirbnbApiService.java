package com.ineedhousing.backend.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.housing_listings.HousingListingRepository;

/**
 * Houses business logic for Airbnb calls
 */
@Service
public class AirbnbApiService {

    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final String SOURCE = "Airbnb";

    public AirbnbApiService(@Qualifier("Airbnb API") RestClient restClient, HousingListingRepository housingListingRepository) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
    }

    public List<HousingListing> updateListingViaLocation(String city, )

}
