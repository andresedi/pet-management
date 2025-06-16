package com.example.petmanagement.dto;

import com.example.petmanagement.annotation.trimmed.Trimmed;
import com.example.petmanagement.persistence.model.Pet;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Trimmed
    private String name;
    @NotBlank(message = "Species cannot be blank")
    @Size(max = 100, message = "Species cannot exceed 100 characters")
    @Trimmed
    private String species;
    @Min(value = 0, message = "Age must be a non-negative integer")
    private Integer age;
    @Size(max = 100, message = "Owner Name cannot exceed 100 characters")
    @Trimmed
    private String ownerName;

    public Pet toPetModel() {
        return Pet.builder()
                .name(this.name)
                .species(this.species)
                .age(this.age)
                .ownerName(this.ownerName)
                .build();
    }
}
