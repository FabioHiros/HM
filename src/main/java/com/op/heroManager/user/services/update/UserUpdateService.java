package com.op.heroManager.user.services.update;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.DTOs.update.UserUpdateDTO;
import com.op.heroManager.user.entities.User;
import com.op.heroManager.user.exceptions.UserNotFoundException;
import com.op.heroManager.user.mappers.UserMapper;
import com.op.heroManager.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    public UserResponseDTO updateUser(UUID id, UserUpdateDTO dto) {
        // Fetch User + Phones eagerly
        User user = repository.findByIdWithPhones(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        mapper.updateUserFromDTO(dto, user);


        // Passes the new List<UpdatePhoneDTO>
        mapper.updatePhonesList(dto.phones(), user);

        return mapper.toResponse(user);
    }
}