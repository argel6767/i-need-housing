package com.ineedhousing.backend.housing_listings.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListingsInAreaRequest {
    private Double latitude;
    private Double longitude;
    private Integer radius;
}
