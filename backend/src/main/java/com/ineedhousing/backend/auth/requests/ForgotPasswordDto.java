package com.ineedhousing.backend.auth.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgotPasswordDto {
    private String email;
    private String password;
    private String verificationCode;


}
