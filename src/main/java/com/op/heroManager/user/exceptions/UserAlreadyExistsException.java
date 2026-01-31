package com.op.heroManager.user.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String email) {
        // Pass 409 here
        super("Email already registered", "email", HttpStatus.CONFLICT);
    }
}