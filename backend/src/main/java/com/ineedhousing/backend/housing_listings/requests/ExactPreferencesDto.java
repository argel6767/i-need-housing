package com.ineedhousing.backend.housing_listings.requests;

import java.util.List;

import com.ineedhousing.backend.housing_listings.HousingListing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExactPreferencesDto {
    List<HousingListing> listings;
    Long id;
}
