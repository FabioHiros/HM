package com.op.heroManager.user.services.read;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.mappers.UserMapper;
import com.op.heroManager.user.projections.PhoneSummary;
import com.op.heroManager.user.projections.UserSummary;
import com.op.heroManager.user.repositories.UserRepository;

@Service
public class UserReadService {
    private final UserRepository repository;
    private final UserMapper mapper;


    public UserReadService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsers(String name, Pageable pageable) {
        // 1. Fetch Users + Address (1 Query)
        Page<UserSummary> page = repository.findByNameContainingIgnoreCase(name, pageable);

        if (page.isEmpty()) return Page.empty(pageable);

        // 2. Fetch Phones (1 Query)
        List<UUID> ids = page.getContent().stream().map(UserSummary::getId).toList();
        List<PhoneSummary> phones = repository.findPhoneSummaries(ids);

        // 3. Group Phones (In-Memory)
        Map<UUID, List<String>> phoneMap = phones.stream()
            .collect(Collectors.groupingBy(
                 PhoneSummary::getUserId, 
                 Collectors.mapping(PhoneSummary::getNumber, Collectors.toUnmodifiableList())
            ));

        // 4. Map to DTO
        return page.map(summary -> mapper.toDTO(
            summary, 
            phoneMap.getOrDefault(summary.getId(), Collections.emptyList())
        ));
    }
}
