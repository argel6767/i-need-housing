package com.ineedhousing.backend.apis.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityAndStateRequest {

    private String city;
    private String stateAbv;
}
