package com.op.heroManager.user.mappers;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.op.heroManager.user.DTOs.create.CreateAddressDTO;
import com.op.heroManager.user.DTOs.create.CreatePhoneDTO;
import com.op.heroManager.user.DTOs.create.CreateUserDTO;
import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.entities.Address;
import com.op.heroManager.user.entities.Phone;
import com.op.heroManager.user.entities.User;
import com.op.heroManager.user.projections.AddressSummary;
import com.op.heroManager.user.projections.UserSummary;

@Mapper(componentModel = "spring")
public interface UserMapper {

// --- READ MAPPINGS ---
    @Mapping(target = "phoneNumbers", source = "phones")
    UserResponseDTO toDTO(UserSummary summary, List<String> phones);

    UserResponseDTO.AddressDTO toAddressDTO(AddressSummary summary);

    // --- CREATE MAPPINGS ---

    // 1. Main Entity Mapping
    @Mapping(target = "id", ignore = true)
    // FIX A: Don't ignore phones! We need MapStruct to convert DTOs to Entities.
    // The AfterMapping will then link them to the User.
    User toEntity(CreateUserDTO dto);

    // 2. Address Helper (Fixes your specific error)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toAddressEntity(CreateAddressDTO dto);

    // 3. Phone Helper (Needed because we stopped ignoring phones)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "areacode", source = "areaCode") // Fix case mismatch (areaCode -> areacode)
    Phone toPhoneEntity(CreatePhoneDTO dto);

    // --- RESPONSE MAPPINGS ---
    @Mapping(target = "phoneNumbers", source = "phones")
    UserResponseDTO toResponse(User user);

    default String map(Phone phone) {
        return phone.getNumber();
    }

    // --- POST-PROCESSING ---
    // This runs AFTER toEntity. 
    // Since we allowed MapStruct to populate the list, this loop will now actually work.
    @AfterMapping
    default void linkPhones(@MappingTarget User user) {
        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> phone.setUser(user));
        }
    }
}