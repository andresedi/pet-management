package com.example.petmanagement.persistence.repository;

import com.example.petmanagement.persistence.model.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

// This interface defines the application’s contract for working with pets, independent of the underlying database
// It abstracts the data access layer, allowing for different implementations (e.g., SQL, NoSQL) without changing
// the service layer
public interface PetRepository {
    Page<Pet> findAll(Pageable pageable);
    Optional<Pet> findById(Long petId);
    Pet save(Pet pet);
    void deleteById(Long petId);
}