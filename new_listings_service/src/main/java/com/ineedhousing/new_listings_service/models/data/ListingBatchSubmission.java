package com.ineedhousing.new_listings_service.models.data;

import java.util.List;

public class ListingBatchSubmission {
    private List<HousingListing> batch;
    private int RETRY_COUNT = 0;
    private final int MAX_RETRIES = 5;

    public ListingBatchSubmission(List<HousingListing> batch) {
        this.batch = batch;
    }

    public List<HousingListing> getBatch() {
        return batch;
    }

    public void increaseRetryCount() {
        RETRY_COUNT++;
    }

    public boolean isRetryCountMaxed() {
        return RETRY_COUNT == MAX_RETRIES;
    }
}
