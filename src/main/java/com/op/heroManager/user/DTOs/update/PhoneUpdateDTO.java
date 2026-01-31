package com.op.heroManager.user.DTOs.update;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record PhoneUpdateDTO(
    UUID id, // Null = Create New; Present = Update Existing
    @NotBlank String number,
    @NotBlank String areaCode
) {
    
}
