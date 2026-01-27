package com.op.heroManager.user.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.projections.AddressSummary;
import com.op.heroManager.user.projections.UserSummary;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // MapStruct automatically maps nested AddressSummary -> AddressDTO
    @Mapping(target = "phoneNumbers", source = "phones")
    UserResponseDTO toDTO(UserSummary summary, List<String> phones);

    // Helper for the inner record
    UserResponseDTO.AddressDTO toAddressDTO(AddressSummary summary);
}