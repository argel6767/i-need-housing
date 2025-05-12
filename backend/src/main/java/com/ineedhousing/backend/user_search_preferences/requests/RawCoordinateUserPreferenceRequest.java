package com.ineedhousing.backend.user_search_preferences.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawCoordinateUserPreferenceRequest {
    @NotNull
    private Double[] jobLocationCoordinates; //will be in lat, long order
    @NotNull
    private Double[] cityOfEmploymentCoordinates;
    @NotNull
    private String cityOfEmployment;
    @NotNull
    private Integer maxRadius;
    @NotNull
    private Integer maxRent;
    private Integer travelType;
    @NotNull
    private Integer bedrooms;
    @NotNull
    private Integer bathrooms;
    private Boolean isFurnished;
    private LocalDate startDate;
    private LocalDate endDate;
}
