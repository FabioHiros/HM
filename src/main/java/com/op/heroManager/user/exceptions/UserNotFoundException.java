package com.op.heroManager.user.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(UUID id) {
        // Pass 404 here
        super("User not found with id " + id, "id", HttpStatus.NOT_FOUND);
    }
}
