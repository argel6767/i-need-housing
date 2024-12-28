package com.ineedhousing.backend.auth.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    private String email;
    private String oldPassword;
    private String newPassword;
}
