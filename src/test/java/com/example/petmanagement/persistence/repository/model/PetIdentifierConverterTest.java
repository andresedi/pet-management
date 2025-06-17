package com.example.petmanagement.persistence.repository.model;

import com.example.petmanagement.exception.InvalidPetIdException;
import com.example.petmanagement.persistence.model.PetIdentifierConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PetIdentifierConverter Tests")
class PetIdentifierConverterTest {
    @Test
    void shouldCreateFromValidLong() {
        Long id = 1L;
        PetIdentifierConverter converter = PetIdentifierConverter.of(id);
        assertThat(converter.asLong()).isEqualTo(id);
        assertThat(converter.asString()).isEqualTo("1");
    }

    @Test
    void shouldCreateFromValidString() {
        String id = "1";
        PetIdentifierConverter converter = PetIdentifierConverter.of(id);
        assertThat(converter.asLong()).isEqualTo(1L);
        assertThat(converter.asString()).isEqualTo(id);
    }

    @Test
    void shouldHandleNullValue() {
        PetIdentifierConverter converter = PetIdentifierConverter.of((String) null);
        assertThat(converter.asLong()).isNull();
        assertThat(converter.asString()).isNull();
    }

    @Test
    void shouldThrowExceptionForInvalidString() {
        assertThrows(InvalidPetIdException.class, () ->
                PetIdentifierConverter.of("invalid"));
    }
}