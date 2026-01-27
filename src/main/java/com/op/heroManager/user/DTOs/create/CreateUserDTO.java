package com.op.heroManager.user.DTOs.create;

import java.util.List;

import com.op.heroManager.user.enums.Role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    String name,

    @NotBlank(message = "Age is required")
    @Pattern(regexp = "\\d+", message = "Age must be a number") // Validates String is numeric
    String age,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    @NotNull(message = "Role is required")
    Role role,

    @NotNull(message = "Address is required")
    @Valid // <--- CRITICAL: Tells Spring to validate the fields INSIDE CreateAddressDTO
    CreateAddressDTO address,

    @Valid // <--- CRITICAL: Tells Spring to validate every item in the list
    List<CreatePhoneDTO> phones
) {}
