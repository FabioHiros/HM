package com.op.heroManager.user.services.create;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.op.heroManager.user.DTOs.create.CreateUserDTO;
import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.entities.User;
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
        User user = mapper.toEntity(createDTO);

        User savedUser = userRepository.save(user);

        return mapper.toResponse(savedUser);
    }

}
