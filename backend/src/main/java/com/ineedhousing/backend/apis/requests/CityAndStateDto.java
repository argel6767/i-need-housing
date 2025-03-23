package com.ineedhousing.backend.apis.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityAndStateDto{

    private String city;
    private String stateAbv;
}
