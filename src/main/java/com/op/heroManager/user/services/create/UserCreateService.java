package com.op.heroManager.user.services.create;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.op.heroManager.user.DTOs.create.CreateUserDTO;
import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.entities.User;
import com.op.heroManager.user.exceptions.UserAlreadyExistsException;
import com.op.heroManager.user.mappers.UserMapper;
import com.op.heroManager.user.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCreateService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponseDTO createUser(CreateUserDTO createDTO){
        // 1. Convert DTO to Entity (Address and Phones are linked here by Mapper)
        if (userRepository.existsByEmail(createDTO.email())){
            throw new UserAlreadyExistsException(createDTO.email());
        }

        User user = mapper.toEntity(createDTO);

        // 2. Save
        // The returned 'savedUser' is the persisted entity with the generated ID.
        // It already holds the phone/address data in memory, so accessing them 
        // in 'toResponse' will NOT trigger a database query.
        User savedUser = userRepository.save(user);

        // 3. Return
        return mapper.toResponse(savedUser);
    }
}