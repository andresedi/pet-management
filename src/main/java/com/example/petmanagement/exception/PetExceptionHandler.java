package com.example.petmanagement.exception;

import com.example.petmanagement.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class PetExceptionHandler {
    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFound(PetNotFoundException ex) {
        log.debug("Handling Pet Not Found: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .messages(List.of(ex.getMessage()))
                .reason(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
