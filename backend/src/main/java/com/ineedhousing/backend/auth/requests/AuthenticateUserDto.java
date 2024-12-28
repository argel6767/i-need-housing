package com.ineedhousing.backend.auth.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticateUserDto {
    private String username;
    private String password;

}
