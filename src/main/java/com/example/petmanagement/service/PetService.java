package com.example.petmanagement.service;

import com.example.petmanagement.dto.PaginatedResponse;
import com.example.petmanagement.dto.PetRequest;
import com.example.petmanagement.dto.PetResponse;
import org.springframework.data.domain.Pageable;

public interface PetService {
    PaginatedResponse<PetResponse> getAllPets(Pageable pageable);

    PetResponse getPetById(String petId);

    PetResponse createPet(PetRequest petRequest);

    PetResponse updatePet(String petId, PetRequest petRequest);

    void deletePet(String petId);
}
