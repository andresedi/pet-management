package com.example.petmanagement.exception;

import com.example.petmanagement.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE) // Ensures this handler is called after other specific handlers
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Handling Data Integrity Violation: constraint={}, detail={}",
                ex.getClass().getSimpleName(),
                ex.getMostSpecificCause().getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of("Database operation failed"))
                .reason(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Handling Http Message Not Readable: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of("Invalid request format"))
                .reason(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Handling Http Request Method Not Supported: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of("HTTP method '" + ex.getMethod() + "' is not supported for this request"))
                .reason(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Handling No Resource Found: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of("The requested resource does not exist"))
                .reason(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Handling Runtime Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of("An unexpected error occurred. If the problem persists, contact support."))
                .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Handling Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of("An unexpected error occurred. If the problem persists, contact support."))
                .reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
