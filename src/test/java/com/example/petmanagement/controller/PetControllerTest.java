package com.example.petmanagement.controller;

import com.example.petmanagement.dto.PaginatedResponse;
import com.example.petmanagement.dto.PetRequest;
import com.example.petmanagement.dto.PetResponse;
import com.example.petmanagement.exception.PetExceptionHandler;
import com.example.petmanagement.exception.PetNotFoundException;
import com.example.petmanagement.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    private MockMvc mockMvc;
    @Mock
    private PetService petService;
    private ObjectMapper objectMapper;
    private PetRequest petRequest;
    private PetResponse petResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PetController(petService))
                .setControllerAdvice(new PetExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        petRequest = PetRequest.builder()
                .name("Fluffy")
                .species("Cat")
                .age(3)
                .ownerName("John")
                .build();

        petResponse = PetResponse.builder()
                .petId(1L)
                .name("Fluffy")
                .species("Cat")
                .age(3)
                .ownerName("John")
                .build();
    }

    @Test
    void getAllPets_ShouldReturnPets() throws Exception {
        PaginatedResponse<PetResponse> paginatedResponse = PaginatedResponse.<PetResponse>builder()
                .content(List.of(petResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .last(true)
                .build();
        when(petService.getAllPets(any(Pageable.class)))
                .thenReturn(paginatedResponse);

        mockMvc.perform(get("/api/pets")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].petId").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Fluffy"))
                .andExpect(jsonPath("$.content[0].species").value("Cat"))
                .andExpect(jsonPath("$.content[0].age").value(3))
                .andExpect(jsonPath("$.content[0].ownerName").value("John"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getPetById_WhenPetExists_ShouldReturnOK() throws Exception {
        when(petService.getPetById(1L)).thenReturn(petResponse);

        mockMvc.perform(get("/api/pets/{petId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.name").value("Fluffy"))
                .andExpect(jsonPath("$.species").value("Cat"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("John"));
    }

    @Test
    void getPetById_WhenPetNotFound_ShouldReturnNotFound() throws Exception {
        when(petService.getPetById(999L)).thenThrow(new PetNotFoundException(999L));

        mockMvc.perform(get("/api/pets/{petId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPet_ShouldCreatePet() throws Exception {
        when(petService.createPet(any(PetRequest.class)))
                .thenReturn(petResponse);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.name").value("Fluffy"))
                .andExpect(jsonPath("$.species").value("Cat"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("John"));;
    }

    @Test
    void updatePet_ShouldReturnOK() throws Exception {
        when(petService.updatePet(anyLong(), any(PetRequest.class))).thenReturn(petResponse);

        mockMvc.perform(put("/api/pets/{petId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.name").value("Fluffy"))
                .andExpect(jsonPath("$.species").value("Cat"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.ownerName").value("John"));
    }

    @Test
    void updatePet_WhenPetNotFound_ShouldReturnNotFound() throws Exception {
        when(petService.updatePet(anyLong(), any(PetRequest.class)))
                .thenThrow(new PetNotFoundException(999L));

        mockMvc.perform(put("/api/pets/{petId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePet_ShouldReturn204() throws Exception {
        doNothing().when(petService).deletePet(1L);

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());
    }
}