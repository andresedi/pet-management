package com.example.petmanagement.persistence.model;

import com.example.petmanagement.exception.InvalidPetIdException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * PetIdentifierConverter is a class that implements the PetIdentifier interface.
 * It provides methods to convert a Long value to a String and vice versa,
 * while also handling invalid input gracefully.
 */
@EqualsAndHashCode
@ToString
public class PetIdentifierConverter implements PetIdentifier {
    private final Long value;

    private PetIdentifierConverter(Long value) {
        this.value = value;
    }

    public static PetIdentifierConverter of(Long value) {
        return new PetIdentifierConverter(value);
    }

    public static PetIdentifierConverter of(String value) {
        try {
            return new PetIdentifierConverter(value != null ? Long.parseLong(value) : null);
        } catch (NumberFormatException e) {
            throw new InvalidPetIdException(value);
        }
    }

    @Override
    public String asString() {
        return value != null ? value.toString() : null;
    }

    @Override
    public Long asLong() {
        return value;
    }
}
