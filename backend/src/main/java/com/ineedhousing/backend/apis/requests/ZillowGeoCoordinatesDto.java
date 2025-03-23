package com.ineedhousing.backend.apis.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZillowGeoCoordinatesDto {

    private double latitude;
    private double longitude;
}
