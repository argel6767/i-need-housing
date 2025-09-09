package com.ineedhousing.models.requests;

public record VerificationCodeDto(String email, String verificationCode) {
}
