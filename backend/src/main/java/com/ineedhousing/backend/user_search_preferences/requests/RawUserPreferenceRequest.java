package com.ineedhousing.backend.user_search_preferences.requests;

import java.time.LocalDate;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawUserPreferenceRequest {

    private Point jobLocation;
    private Point cityOfEmployment;
    private Integer maxRadius;
    private Integer maxRent;
    private Integer travelType;
    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean isFurnished;
    private LocalDate startDate;
    private LocalDate endDate;

}
