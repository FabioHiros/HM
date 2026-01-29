package com.op.heroManager.user.DTOs.read;

import java.util.List;
import java.util.UUID;

import com.op.heroManager.user.enums.Role;

public record UserResponseDTO(
    UUID id, 
    String name, 
    String email, 
    AddressDTO address,
    Role role, 
    List<String> phoneNumbers // The optimized list
) {
    public record AddressDTO(String street, String city, String zipCode, String state, String country) {}
}
