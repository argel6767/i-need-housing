package com.ineedhousing.backend.user_search_preferences.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawUserPreferencesDto {
    @NotNull
    private String jobLocationAddress;
    @NotNull
    private String cityOfEmploymentAddress;
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
