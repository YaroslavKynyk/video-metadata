package com.goldmediatech.vms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String CODE_500_ERROR_MESSAGE = "Internal Server Error: An unexpected error occurred on the server.";
    public static final String CODE_403_ERROR_MESSAGE = "Access denied.";
    public static final String CODE_401_ERROR_MESSAGE = "Bad credentials";
    public static final String CODE_400_ERROR_MESSAGE = "Invalid input";

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("[BAD_REQUEST] Validation error occurred", ex);
        return ResponseEntity.badRequest().body(Map.of("error", CODE_400_ERROR_MESSAGE));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        log.error("[UNAUTHORIZED] User not found", ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", CODE_401_ERROR_MESSAGE));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        log.error("[UNAUTHORIZED] Bad credentials", ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", CODE_401_ERROR_MESSAGE));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        log.error("[FORBIDDEN] Access denied", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", CODE_403_ERROR_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception ex) {
        log.error("[INTERNAL_SERVER_ERROR] Unhandled exception occurred", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", CODE_500_ERROR_MESSAGE));
    }
}
