package com.op.heroManager.user.DTOs.update;

public record AddressUpdateDTO(
    String street,
    String city,
    String zipCode,
    String neighborhood,
    String state,
    String country,
    String number
) {}