package com.ineedhousing.backend.user_search_preferences.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewFiltersDto {
    String cityOfEmployment;
    Double[] cityOfEmploymentCoords;
    List<Map<String, Double>> desiredArea;
    Long id;
    LocalDate internshipStart;
    LocalDate internshipEnd;
    Boolean isFurnished;
    Double[] jobLocation;
    Integer maxRadius;
    Integer maxRent;
    Integer minNumberOfBedrooms;
    Integer minNumberOfBathrooms;
    String travelType;
    LocalDateTime updatedAt;
}
