package com.ineedhousing.backend.auth.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyUserDto {

    private String email;
    private String verificationToken;

}
