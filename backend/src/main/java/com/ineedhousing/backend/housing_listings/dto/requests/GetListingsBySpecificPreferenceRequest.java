package com.ineedhousing.backend.housing_listings.dto.requests;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListingsBySpecificPreferenceRequest extends GetListingsInAreaRequest{

    private Map<String, Object> specificPreference;
    
}