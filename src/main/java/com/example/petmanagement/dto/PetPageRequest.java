package com.example.petmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetPageRequest {
    @Min(value = 0, message = "Page index must be 0 or greater")
    private int page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    private int size = 10;

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Sort direction must be 'asc' or 'desc'")
    private String direction = "asc";

    @NotBlank(message = "Sort field must not be blank")
    @Pattern(regexp = "petId|name|age", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Sort field must be either 'petId', 'name' or 'age'")
    private String sortBy = "petId";
}
