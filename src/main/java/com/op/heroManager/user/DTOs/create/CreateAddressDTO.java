package com.op.heroManager.user.DTOs.create;


import jakarta.validation.constraints.NotBlank;

public record CreateAddressDTO(
    @NotBlank(message = "Street is required")
    String street,
    
    @NotBlank(message = "City is required")
    String city,
    
    @NotBlank(message = "ZipCode is required")
    String zipCode,

    @NotBlank(message = "neighborhood is required")
    String neighborhood,
    
    @NotBlank(message = "state is required")
    String state,

    @NotBlank(message = "country is required")
    String country,

    @NotBlank(message = "number is required")
    String number

) {}

