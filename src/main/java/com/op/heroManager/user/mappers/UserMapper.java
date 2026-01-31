package com.op.heroManager.user.mappers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.*;
import com.op.heroManager.user.DTOs.create.*;
import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.DTOs.update.*;
import com.op.heroManager.user.entities.*;
import com.op.heroManager.user.projections.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // --- READ ---
    @Mapping(target = "phoneNumbers", source = "phones")
    UserResponseDTO toDTO(UserSummary summary, List<String> phones);

    UserResponseDTO.AddressDTO toAddressDTO(AddressSummary summary);
    
    @Mapping(target = "phoneNumbers", source = "phones")
    UserResponseDTO toResponse(User user);

    default String map(Phone phone) {
        if (phone == null) return null;
        // Returns format: "(11) 99999-9999"
        return "(" + phone.getAreacode() + ") " + phone.getNumber();
    }

   @Mapping(target = "id", ignore = true)
    User toEntity(CreateUserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toAddressEntity(CreateAddressDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "areacode", source = "areaCode")
    Phone toPhoneEntity(CreatePhoneDTO dto);

    // 🔴 FIX: RESTORE THIS! 
    // Without this, MapStruct uses setPhones(), and the 'user' field in Phone remains NULL.
    @AfterMapping
    default void linkBidirectionalRelationships(@MappingTarget User user) {
        // Link phones
        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> {
                if (phone.getUser() == null) { // Defensive
                    phone.setUser(user);
                }
            });
        }
    }

    // --- UPDATE ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phones", ignore = true) 
    void updateUserFromDTO(UserUpdateDTO dto, @MappingTarget User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateAddressFromDTO(AddressUpdateDTO dto, @MappingTarget Address entity);
    
    // 🛡️ SECURITY FIX: Ignore ID on new phones to prevent injection
    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "areacode", source = "areaCode")
    Phone createPhoneFromUpdate(PhoneUpdateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "areacode", source = "areaCode")
    void updatePhoneFromDTO(PhoneUpdateDTO dto, @MappingTarget Phone entity);

    default void updatePhonesList(List<PhoneUpdateDTO> dtos, @MappingTarget User user) {
        if (dtos == null) return;

        Map<UUID, Phone> existingPhones = user.getPhones().stream()
            .collect(Collectors.toMap(Phone::getId, p -> p));

        List<UUID> keptIds = dtos.stream()
            .map(PhoneUpdateDTO::id)
            .filter(id -> id != null)
            .toList();

        user.getPhones().removeIf(p -> !keptIds.contains(p.getId()));

        for (PhoneUpdateDTO dto : dtos) {
            if (dto.id() != null && existingPhones.containsKey(dto.id())) {
                updatePhoneFromDTO(dto, existingPhones.get(dto.id()));
            } else {
                // 🔴 FIX: Use the 'New' mapper that ignores malicious IDs
                user.addPhone(createPhoneFromUpdate(dto));
            }
        }
    }
}
