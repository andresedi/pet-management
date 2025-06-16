package com.example.petmanagement.annotation.trimmed;

import com.example.petmanagement.dto.PetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrimmedAnnotationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new TrimmedAnnotationHandler());
    }

    @Test
    void shouldTrimAnnotatedFields() throws Exception {
        String json = """
            {
                "name": "  Fluffy  ",
                "species": "\\tCat\\t",
                "ownerName": "  John  Doe  "
            }
            """;

        PetRequest pet = objectMapper.readValue(json, PetRequest.class);

        assertEquals("Fluffy", pet.getName());
        assertEquals("Cat", pet.getSpecies());
        assertEquals("John  Doe", pet.getOwnerName());
    }

    @Test
    void shouldHandleNullValues() throws Exception {
        String json = """
            {
                "name": null,
                "species": null,
                "ownerName": null
            }
            """;

        PetRequest pet = objectMapper.readValue(json, PetRequest.class);

        assertNull(pet.getName());
        assertNull(pet.getSpecies());
        assertNull(pet.getOwnerName());
    }

    @Test
    void shouldHandleEmptyStrings() throws Exception {
        String json = """
            {
                "name": "",
                "species": "   ",
                "ownerName": "\\t\\n"
            }
            """;

        PetRequest pet = objectMapper.readValue(json, PetRequest.class);

        assertEquals("", pet.getName());
        assertEquals("", pet.getSpecies());
        assertEquals("", pet.getOwnerName());
    }
}