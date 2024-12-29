package com.ineedhousing.backend.favorite_listings.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFavoriteListingRequest {

    private List<Long> favoriteListingIds;
}
