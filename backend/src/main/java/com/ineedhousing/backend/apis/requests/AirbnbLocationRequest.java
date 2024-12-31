package com.ineedhousing.backend.apis.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirbnbLocationRequest {

    private String city;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer numOfPets;
}
