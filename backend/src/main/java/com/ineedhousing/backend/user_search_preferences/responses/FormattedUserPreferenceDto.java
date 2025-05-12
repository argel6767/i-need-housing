package com.ineedhousing.backend.user_search_preferences.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormattedUserPreferenceDto {

    private Long id;
    private double[] jobLocation;
    private double[] cityOfEmploymentCoordinates;
    private String cityOfEmployment;
    private List<Map<String, Double>> desiredArea;
    private int maxRadius;
    private int minNumberOfBedrooms;
    private double minNumberOfBathrooms;
    private LocalDate internshipStart;
    private LocalDate internshipEnd;
    private LocalDateTime updatedAt;
}
