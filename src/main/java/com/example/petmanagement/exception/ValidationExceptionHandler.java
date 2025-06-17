package com.example.petmanagement.exception;

import com.example.petmanagement.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex) {
        log.info("Handling Method Argument Not Valid: {}", ex.getMessage());
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(errors)
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.info("Handling Constraint Violation: {}", ex.getMessage());
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(errors)
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
}