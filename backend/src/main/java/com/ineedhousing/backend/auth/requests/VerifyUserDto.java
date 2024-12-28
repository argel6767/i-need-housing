package com.ineedhousing.backend.auth.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUserDto {

    private String email;
    private String verificationToken;

}
