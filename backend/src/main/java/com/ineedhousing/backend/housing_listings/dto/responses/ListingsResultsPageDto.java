package com.ineedhousing.backend.housing_listings.dto.responses;

import com.ineedhousing.backend.housing_listings.HousingListing;

import java.util.List;

public record ListingsResultsPageDto(List<HousingListing> housingListings, Integer pageNumber, Integer totalPages) {
}
