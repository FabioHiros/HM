package com.op.heroManager.user.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.services.read.UserReadService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserReadService service;

    public UserController(UserReadService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> search(
            @RequestParam(required = false, defaultValue = "") String name,
            Pageable pageable) {
        return ResponseEntity.ok(service.searchUsers(name, pageable));
    }
}