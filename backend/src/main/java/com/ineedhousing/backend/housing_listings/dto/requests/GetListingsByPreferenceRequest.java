package com.ineedhousing.backend.housing_listings.dto.requests;

import com.ineedhousing.backend.user_search_preferences.UserPreference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListingsByPreferenceRequest extends GetListingsInAreaRequest {
    private UserPreference preferences;
}
