package com.ineedhousing.backend.auth.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResendEmailDto {

    private String email;

}
