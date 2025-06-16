package com.example.petmanagement.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(Long petId) {
        super(String.format("Pet not found with id: %d ", petId));
    }
}
