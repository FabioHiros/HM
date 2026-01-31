package com.op.heroManager.user.services.delete;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.op.heroManager.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

    private final UserRepository repository;

    @Transactional
    public void deleteUser(UUID id) {
        // 1. Check if exists (to throw 404 if not found)
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }

        // 2. Delete
        // JPA Cascade takes care of Phones and Address automatically
        repository.deleteById(id);
    }
}