package com.ineedhousing.backend.auth.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordDto {

    private String email;
    private String oldPassword;
    private String newPassword;
}
