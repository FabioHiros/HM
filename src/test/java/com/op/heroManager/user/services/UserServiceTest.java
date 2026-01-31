package com.op.heroManager.user.services;

import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.entities.Address;
import com.op.heroManager.user.entities.Phone;
import com.op.heroManager.user.entities.User;
import com.op.heroManager.user.enums.Role;
import com.op.heroManager.user.repositories.UserRepository;
import com.op.heroManager.user.services.read.UserReadService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest loads the full context (Repository + Service + H2 DB)
@SpringBootTest
@Transactional // Rolls back data after each test so it's clean
class UserServiceTest {

    @Autowired
    private UserReadService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should fetch Users, Address, and Phones in 2 queries and stitch them correctly")
    void shouldSearchUsersWithProjections() {
        // --- GIVEN (Setup Data in H2) ---
        User user = new User();
        user.setName("Fabio");
        user.setEmail("fabio@example.com");
        user.setAge(30);
        user.setRole(Role.USER);

        // Add Address
        Address address = new Address();
        address.setStreet("Tech Blvd");
        address.setCity("Silicon Valley");
        address.setZipCode("94000");
        // ... fill other required fields for validation if needed ...
        address.setCountry("USA");
        address.setState("CA");
        address.setNumber("101");
        address.setNeighborhood("North");
        
        user.setAddress(address); // Link Address

        // Add Phones (Using your helper method)
        Phone p1 = new Phone();
        p1.setNumber("111-222");
        p1.setAreacode("55");
        
        Phone p2 = new Phone();
        p2.setNumber("999-888");
        p2.setAreacode("55");

        user.addPhone(p1);
        user.addPhone(p2);

        // Save (Cascades will save Address and Phones automatically)
        userRepository.save(user);

        // --- WHEN (Execute the Optimized Logic) ---
        Page<UserResponseDTO> result = userService.searchUsers("Fabio", PageRequest.of(0, 10));

        // --- THEN (Verify Everything) ---
        assertThat(result).isNotEmpty();
        UserResponseDTO dto = result.getContent().get(0);

        // 1. Verify User Basic Info
        assertThat(dto.name()).isEqualTo("Fabio");
        assertThat(dto.email()).isEqualTo("fabio@example.com");

        // 2. Verify Address (fetched via Projection in Query 1)
        assertThat(dto.address()).isNotNull();
        assertThat(dto.address().street()).isEqualTo("Tech Blvd");
        assertThat(dto.address().city()).isEqualTo("Silicon Valley");

        // 3. Verify Phones (fetched via Batch Query 2 and stitched)
        assertThat(dto.phoneNumbers())
                .hasSize(2)
                .contains("111-222", "999-888");
    }
}