package com.example.petmanagement.persistence.repository;

import com.example.petmanagement.persistence.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetJpaRepository extends JpaRepository<Pet, Long> {
    // No need to add methods — JpaRepository already provides them
}
