package com.op.heroManager.user.exceptions;


import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    
    public ResourceNotFoundException(String resourceName, Object id) {
        // This generates the message: "User not found with id 123"
        // It sets the field to "id"
        // It sets the status to 404 NOT_FOUND
        super(resourceName + " not found with id " + id, "id", HttpStatus.NOT_FOUND);
    }
}