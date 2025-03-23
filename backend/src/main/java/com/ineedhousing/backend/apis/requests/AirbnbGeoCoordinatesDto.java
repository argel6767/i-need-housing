package com.ineedhousing.backend.apis.requests;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirbnbGeoCoordinatesDto {

    private List<Double> areaCorners;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer numOfPets;
    
}
