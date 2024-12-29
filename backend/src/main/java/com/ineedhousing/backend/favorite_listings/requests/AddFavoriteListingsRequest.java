package com.ineedhousing.backend.favorite_listings.requests;

import com.ineedhousing.backend.favorite_listings.FavoriteListing;
import com.ineedhousing.backend.housing_listings.HousingListing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFavoriteListingsRequest {

    private List<HousingListing> listings;
}
