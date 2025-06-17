package com.example.petmanagement.exception;

public class InvalidPetIdException extends RuntimeException {
    public InvalidPetIdException(String id) {
        super(String.format("Pet ID must be an integer, but was: '%s'", id));
    }
}
