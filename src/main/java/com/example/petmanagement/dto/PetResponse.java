package com.example.petmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {
    private String petId;
    private String name;
    private String species;
    private int age;
    private String ownerName;
}
