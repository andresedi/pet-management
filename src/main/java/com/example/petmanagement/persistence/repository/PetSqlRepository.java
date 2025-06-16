package com.example.petmanagement.persistence.repository;

import com.example.petmanagement.persistence.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// This adapter bridges the domain interface (PetRepository) with the Spring Data JPA implementation
@Repository
public class PetSqlRepository implements PetRepository {

    private final PetJpaRepository petJpaRepository;

    @Autowired
    public PetSqlRepository(PetJpaRepository petJpaRepository) {
        this.petJpaRepository = petJpaRepository;
    }

    @Override
    public Page<Pet> findAll(Pageable pageable) {
        return petJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Pet> findById(Long petId) {
        return petJpaRepository.findById(petId);
    }

    @Override
    public Pet save(Pet pet) {
        return petJpaRepository.save(pet);
    }

    @Override
    public void deleteById(Long petId) {
        petJpaRepository.deleteById(petId);
    }
}
