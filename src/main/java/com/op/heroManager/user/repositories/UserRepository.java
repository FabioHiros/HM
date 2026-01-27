package com.op.heroManager.user.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.op.heroManager.user.entities.User;
import com.op.heroManager.user.projections.PhoneSummary;
import com.op.heroManager.user.projections.UserSummary;

public interface UserRepository extends JpaRepository<User,UUID> {
    // 1. Fetch Users + Address (Automatic Nested Projection)
    // Spring sees 'UserSummary' has 'getAddress()', so it does a Left Join automatically.
    Page<UserSummary> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 2. Fetch Phones (Still manual to handle N+1 correctly)
    @Query("SELECT p.user.id as userId, p.number as number FROM phones p WHERE p.user.id IN :userIds")
    List<PhoneSummary> findPhoneSummaries(@Param("userIds") List<UUID> userIds);
}
