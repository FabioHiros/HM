package com.op.heroManager.user.DTOs.create;

import java.util.List;

import com.op.heroManager.user.enums.Role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;

public record CreateUserDTO(
    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    String name,

    
    @NotNull(message = "age is required")
    @Min(value = 18, message = "Must be 18 or older")
    @Max(value = 130, message = "Age must be realistic")
    Integer age,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    @NotNull(message = "Role is required")
    Role role,

    @NotNull(message = "Address is required")
    @Valid // <--- CRITICAL: Tells Spring to validate the fields INSIDE CreateAddressDTO
    CreateAddressDTO address,

    @Valid // <--- CRITICAL: Tells Spring to validate every item in the list
    List<CreatePhoneDTO> phones,

    @NotBlank
    @Size(min = 8, message = "Password must hava 8 a minimum of 8 characters")
    String password
) {}
