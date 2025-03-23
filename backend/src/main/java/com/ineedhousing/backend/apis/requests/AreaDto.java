package com.ineedhousing.backend.apis.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaDto {

    private Integer radius;
    private Double latitude;
    private Double longitude;

}
