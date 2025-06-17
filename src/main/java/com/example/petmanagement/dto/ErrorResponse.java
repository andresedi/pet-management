package com.example.petmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "ErrorResponse", description = "Response object for error details")
public class ErrorResponse {
    @Schema(description = "List of error messages", example = "[\"Pet with ID 1 not found\"]")
    private List<String> messages;
    @Schema(description = "HTTP error reason", example = "Not Found")
    private String reason;
    @Schema(description = "Timestamp of the error", example = "2024-01-10T15:30:00Z")
    private Instant timestamp;
}
