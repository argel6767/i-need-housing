package com.ineedhousing.new_listings_service.subscribers;

import com.ineedhousing.new_listings_service.models.NewListingsEvent;
import com.ineedhousing.new_listings_service.repositories.HousingListingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RentCastSubscriber {

    private final RestClient restClient;
    private final HousingListingRepository housingListingRepository;
    private final String SOURCE = "RentCast";
    private final int LIMIT = 100;

    public RentCastSubscriber(@Qualifier("RentCast API") RestClient restClient, HousingListingRepository housingListingRepository) {
        this.restClient = restClient;
        this.housingListingRepository = housingListingRepository;
    }


    @EventListener
    @Async
    public void handleNewListingsEvent(NewListingsEvent event) {
        fetchNewListings();
    }

    private void fetchNewListings() {
        //fetches data. transforms it into a HousingListing object, then saves to DB
    }
}
