package com.op.heroManager.user.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.op.heroManager.user.DTOs.create.CreateUserDTO;
import com.op.heroManager.user.DTOs.read.UserResponseDTO;
import com.op.heroManager.user.DTOs.update.UserUpdateDTO;
import com.op.heroManager.user.services.create.UserCreateService;
import com.op.heroManager.user.services.delete.UserDeleteService;
import com.op.heroManager.user.services.read.UserReadService;
import com.op.heroManager.user.services.update.UserUpdateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserReadService readService;
    private final UserCreateService createService;
    private final UserUpdateService updateService;
    private final UserDeleteService deleteService;
    // public UserController(UserReadService readService, UserCreateService createService) {
    //     this.readService = readService;
    //     this.createService = createService;
    // }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> search(
            @RequestParam(required = false, defaultValue = "") String name,
            Pageable pageable) {
        return ResponseEntity.ok(readService.searchUsers(name, pageable));
    }


    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserDTO dto) {
        
        UserResponseDTO response = createService.createUser(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(response.id())
        .toUri();
        
        return ResponseEntity.created(uri).body(response);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id, 
            @RequestBody @Valid UserUpdateDTO dto) {
        
        return ResponseEntity.ok(updateService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        deleteService.deleteUser(id);
        // Returns "204 No Content" (Standard for Delete)
        return ResponseEntity.noContent().build();
    }
}