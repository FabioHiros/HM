package com.op.heroManager.user.DTOs.create;

import jakarta.validation.constraints.NotBlank;

public record CreatePhoneDTO(
    @NotBlank(message = "Number is required")
    String number,
    @NotBlank(message = "Area code is required")
    String areaCode
) {}
