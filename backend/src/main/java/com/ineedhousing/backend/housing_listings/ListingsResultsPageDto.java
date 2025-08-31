package com.ineedhousing.backend.housing_listings;

import java.util.List;

public record ListingsResultsPageDto(List<HousingListing> housingListings, Integer pageNumber, Integer totalPages) {
}
