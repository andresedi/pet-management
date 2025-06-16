package com.example.petmanagement.integration;

import com.example.petmanagement.dto.PetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PetIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String createPetJson() throws Exception {
        PetRequest request = PetRequest.builder()
                .name("Fluffy")
                .species("Cat")
                .age(3)
                .ownerName("John")
                .build();
        return objectMapper.writeValueAsString(request);
    }

    @Test
    void shouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        PetRequest invalidRequest = PetRequest.builder()
                .name("")  // empty name
                .species("Cat")
                .age(-1)   // invalid age
                .ownerName("John")
                .build();

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllPets() throws Exception {
        // Create first pet
        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPetJson()))
                .andExpect(status().isCreated());

        // Create second pet with different name
        PetRequest secondPet = PetRequest.builder()
                .name("Max")
                .species("Dog")
                .age(2)
                .ownerName("Jane")
                .build();

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondPet)))
                .andExpect(status().isCreated());

        // Get all pets
        mockMvc.perform(get("/api/pets?pageNum=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*].name").value(containsInAnyOrder("Fluffy", "Max")))
                .andExpect(jsonPath("$.content[*].species").value(containsInAnyOrder("Cat", "Dog")))
                .andExpect(jsonPath("$.content[*].age").value(containsInAnyOrder(3, 2)))
                .andExpect(jsonPath("$.content[*].ownerName").value(containsInAnyOrder("John", "Jane")))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void shouldCreateAndRetrievePet() throws Exception {
        // Create pet
        MvcResult createResult = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPetJson()))
                .andExpect(status().isCreated())
                .andReturn();

        // Retrieve created pet
        Integer petId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.petId");
        mockMvc.perform(get("/api/pets/" + petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fluffy"))
                .andExpect(jsonPath("$.species").value("Cat"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("John"));
    }

    @Test
    void shouldCreateAndUpdatePet() throws Exception {
        // First create a pet
        MvcResult createResult = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPetJson()))
                .andExpect(status().isCreated())
                .andReturn();

        Integer petId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.petId");

        // Then update it
        PetRequest updateRequest = PetRequest.builder()
                .name("Updated Fluffy")
                .species("Dog")
                .age(4)
                .ownerName("Jane")
                .build();

        mockMvc.perform(put("/api/pets/" + petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Fluffy"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.age").value(4))
                .andExpect(jsonPath("$.ownerName").value("Jane"));
    }

    @Test
    void shouldCreateAndDeletePet() throws Exception {
        // Create pet
        MvcResult createResult = mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPetJson()))
                .andExpect(status().isCreated())
                .andReturn();

        Integer petId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.petId");

        // Delete pet
        mockMvc.perform(delete("/api/pets/" + petId))
                .andExpect(status().isNoContent());

        // Verify pet is deleted
        mockMvc.perform(get("/api/pets/" + petId))
                .andExpect(status().isNotFound());
    }
}
