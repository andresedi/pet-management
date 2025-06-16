package com.example.petmanagement.service;

import com.example.petmanagement.dto.PaginatedResponse;
import com.example.petmanagement.dto.PetRequest;
import com.example.petmanagement.dto.PetResponse;
import com.example.petmanagement.exception.PetNotFoundException;
import com.example.petmanagement.persistence.model.Pet;
import com.example.petmanagement.persistence.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {
    @Mock
    private PetRepository petRepository;
    @InjectMocks
    private PetServiceImpl petService;
    private PetRequest petRequest;
    private Pet pet;

    @BeforeEach
    void setUp() {
        petRequest = PetRequest.builder()
                .name("Fluffy")
                .species("Cat")
                .age(3)
                .ownerName("John")
                .build();

        pet = Pet.builder()
                .petId(1L)
                .name("Fluffy")
                .species("Cat")
                .age(3)
                .ownerName("John")
                .build();
    }

    @Test
    void getAllPets_ShouldReturnPetsList() {
        Page<Pet> page = new PageImpl<>(List.of(pet), PageRequest.of(0, 10), 1);
        when(petRepository.findAll(any(Pageable.class))).thenReturn(page);

        PaginatedResponse<PetResponse> result = petService.getAllPets(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Fluffy");
        assertThat(result.getContent().get(0).getSpecies()).isEqualTo("Cat");
        assertThat(result.getContent().get(0).getAge()).isEqualTo(3);
        assertThat(result.getContent().get(0).getOwnerName()).isEqualTo("John");
        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();
    }

    @Test
    void getPetById_WhenPetExists_ShouldReturnPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        PetResponse result = petService.getPetById(1L);

        assertThat(result.getPetId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Fluffy");
        assertThat(result.getSpecies()).isEqualTo("Cat");
        assertThat(result.getAge()).isEqualTo(3);
        assertThat(result.getOwnerName()).isEqualTo("John");
    }

    @Test
    void getPetById_WhenPetNotFound_ShouldThrowException() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getPetById(1L));
    }

    @Test
    void createPet_ShouldReturnCreatedPet() {
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        PetResponse result = petService.createPet(petRequest);

        assertThat(result.getName()).isEqualTo("Fluffy");
        assertThat(result.getSpecies()).isEqualTo("Cat");
        assertThat(result.getAge()).isEqualTo(3);
        assertThat(result.getOwnerName()).isEqualTo("John");
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void deletePet_WhenPetExists_ShouldDeletePet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        petService.deletePet(1L);
        verify(petRepository).deleteById(1L);
    }

    @Test
    void deletePet_WhenPetNotFound_ShouldThrowException() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PetNotFoundException.class, () -> petService.deletePet(1L));
    }
}