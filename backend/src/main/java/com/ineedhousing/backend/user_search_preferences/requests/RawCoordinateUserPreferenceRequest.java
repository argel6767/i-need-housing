package com.ineedhousing.backend.user_search_preferences.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawCoordinateUserPreferenceRequest {
    private Double[] jobLocationCoordinates; //will be in lat, long order
    private Double[] cityOfEmploymentCoordinates;
    private String cityOfEmployment;
    private Integer maxRadius;
    private Integer maxRent;
    private Integer travelType;
    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean isFurnished;
    private LocalDate startDate;
    private LocalDate endDate;
}
