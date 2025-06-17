package com.example.petmanagement.persistence.model;

import com.example.petmanagement.dto.PetResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;
    private String name;
    private String species;
    private Integer age;
    private String ownerName;

    public PetResponse toPetResponseDto() {
        return PetResponse.builder()
                .petId(String.valueOf(this.petId))
                .name(this.name)
                .species(this.species)
                .age(this.age)
                .ownerName(this.ownerName)
                .build();
    }

}


