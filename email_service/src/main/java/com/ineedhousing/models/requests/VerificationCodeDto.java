package com.ineedhousing.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VerificationCodeDto(@JsonProperty("email") String email,
                                  @JsonProperty("verificationCode") String verificationCode) {
}
