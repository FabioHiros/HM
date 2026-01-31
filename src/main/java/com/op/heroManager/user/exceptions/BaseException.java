package com.op.heroManager.user.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final String field;
    private final HttpStatus status; // Add this

    // Constructor with status
    public BaseException(String message, String field, HttpStatus status) {
        super(message);
        this.field = field;
        this.status = status;
    }

    // Default to 400 if not specified
    public BaseException(String message, String field) {
        this(message, field, HttpStatus.BAD_REQUEST);
    }

    public String getField() { return field; }
    public HttpStatus getStatus() { return status; }
}