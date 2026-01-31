package com.op.heroManager.user.DTOs.update;

import java.util.List;

// import com.op.heroManager.user.DTOs.create.CreatePhoneDTO;
import com.op.heroManager.user.enums.Role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
    
    @Size(min =3, message = "name should be at least 3 characters long")
    String name,

    @Min(value = 18, message = "Must be 18 or older")
    @Max(value = 130, message = "Age must be realistic")
    Integer age,

    @Email(message = "should be a valid email")
    String email,

    @Valid
    AddressUpdateDTO address,

    @Valid 
    List<PhoneUpdateDTO> phones,

    Role role,

    @Size(min = 8, message = "Password must have a minimum of 8 characters")
    String password

) 
{}
