package com.ineedhousing.backend.user.requests;

import com.ineedhousing.backend.user.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetUserTypeRequest {
    private UserType userType;
}
