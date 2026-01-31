package com.op.heroManager.user.controllers;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.op.heroManager.user.DTOs.error.ErrorMessageDTO;
import com.op.heroManager.user.exceptions.BaseException;

import org.springframework.security.access.AccessDeniedException; // Needs the dependency aboved
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class ExceptionHandlerController {
    
    private final MessageSource messageSource;
    
    public ExceptionHandlerController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // 1. SMART BUSINESS HANDLER
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleBusinessException(BaseException e) {
        List<ErrorMessageDTO> errors = new ArrayList<>();
        errors.add(new ErrorMessageDTO(e.getMessage(), e.getField()));
        
        // Use the status from the exception itself
        return ResponseEntity.status(e.getStatus()).body(errors);
    }

    // 2. VALIDATION HANDLER
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorMessageDTO> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorMessageDTO(
                    messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()),
                    fieldError.getField()
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.badRequest().body(errors);
    }
    
    // 3. SECURITY HANDLER
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleAccessDeniedException(AccessDeniedException e) {
        List<ErrorMessageDTO> errors = new ArrayList<>();
        errors.add(new ErrorMessageDTO(
            "Access denied. You don't have permission to access this resource.", 
            "authorization"
        ));
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }

    // 4. JSON PARSING HANDLER
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        List<ErrorMessageDTO> errors = new ArrayList<>();
        String message = "Invalid input format";
        String field = "request";
        
        if (e.getMessage() != null) {
            if (e.getMessage().contains("Role")) {
                message = "Invalid role value. Must be: ADMIN, USER, MODERATOR, or GUEST";
                field = "role";
            } else if (e.getMessage().contains("LocalDate")) {
                message = "Invalid date format. Use: YYYY-MM-DD";
                field = "date";
            } else if (e.getMessage().contains("Double") || e.getMessage().contains("Number")) {
                message = "Invalid number format";
                field = "amount";
            }
        }
        
        errors.add(new ErrorMessageDTO(message, field));
        return ResponseEntity.badRequest().body(errors);
    }
    
    // 5. GENERIC FALLBACK
    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<ErrorMessageDTO>> handleGenericException(Exception e) {
        List<ErrorMessageDTO> errors = new ArrayList<>();
        errors.add(new ErrorMessageDTO("An unexpected error occurred", "server"));
        e.printStackTrace(); 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}