package com.ineedhousing.backend.admin.models;

import com.ineedhousing.backend.user.responses.UserDto;

public record AuthenticatedAdminDto(String cookie, UserDto userDto) {
}
