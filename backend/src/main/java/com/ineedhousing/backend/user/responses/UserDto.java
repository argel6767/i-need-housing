package com.ineedhousing.backend.user.responses;

import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

public record UserDto(Long id, String email, Collection<? extends GrantedAuthority> authorities, LocalDateTime lastLogin, LocalDateTime createdAt) {
}
