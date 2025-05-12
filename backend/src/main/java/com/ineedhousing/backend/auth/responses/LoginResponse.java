package com.ineedhousing.backend.auth.responses;

import com.ineedhousing.backend.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private long expiresIn;
    private User user;

}
