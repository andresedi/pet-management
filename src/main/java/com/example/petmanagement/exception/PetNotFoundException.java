package com.example.petmanagement.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(String petId) {
        super(String.format("Pet not found with id: %s ", petId));
    }
}
