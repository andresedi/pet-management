package com.example.petmanagement.persistence.repository;

import com.example.petmanagement.persistence.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PetSqlRepositoryTest {
    @Autowired
    private PetJpaRepository petJpaRepository;

    private PetSqlRepository petSqlRepository;
    private Pet pet;

    @BeforeEach
    void setUp() {
        petSqlRepository = new PetSqlRepository(petJpaRepository);

        pet = Pet.builder()
                .name("Fluffy")
                .species("Cat")
                .age(3)
                .ownerName("John")
                .build();
    }

    @Test
    void findAll_ShouldReturnAllPets() {
        petJpaRepository.save(pet);

        Page<Pet> resultPage = petSqlRepository.findAll(PageRequest.of(0, 10));

        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent().get(0).getName()).isEqualTo("Fluffy");
        assertThat(resultPage.getContent().get(0).getSpecies()).isEqualTo("Cat");
        assertThat(resultPage.getContent().get(0).getAge()).isEqualTo(3);
        assertThat(resultPage.getContent().get(0).getOwnerName()).isEqualTo("John");
        assertThat(resultPage.getTotalElements()).isEqualTo(1);
        assertThat(resultPage.getTotalPages()).isEqualTo(1);
        assertThat(resultPage.getNumber()).isEqualTo(0);
        assertThat(resultPage.getSize()).isEqualTo(10);
        assertThat(resultPage.isLast()).isTrue();
    }

    @Test
    void save_ShouldPersistPet() {
        Pet savedPet = petSqlRepository.save(pet);

        assertThat(savedPet.getPetId()).isNotNull();
        assertThat(savedPet.getName()).isEqualTo("Fluffy");
        assertThat(savedPet.getSpecies()).isEqualTo("Cat");
        assertThat(savedPet.getAge()).isEqualTo(3);
        assertThat(savedPet.getOwnerName()).isEqualTo("John");
    }

    @Test
    void findById_WhenPetExists_ShouldReturnPet() {
        Pet savedPet = petJpaRepository.save(pet);

        Optional<Pet> result = petSqlRepository.findById(savedPet.getPetId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Fluffy");
        assertThat(result.get().getSpecies()).isEqualTo("Cat");
        assertThat(result.get().getAge()).isEqualTo(3);
        assertThat(result.get().getOwnerName()).isEqualTo("John");
    }

    @Test
    void update_ShouldModifyPet() {
        Pet savedPet = petJpaRepository.save(pet);

        savedPet.setName("Fluffy Updated");
        savedPet.setSpecies("Dog");
        savedPet.setAge(4);
        savedPet.setOwnerName("Jane");
        Pet updatedPet = petSqlRepository.save(savedPet);

        assertThat(updatedPet.getPetId()).isEqualTo(savedPet.getPetId());
        assertThat(updatedPet.getName()).isEqualTo("Fluffy Updated");
        assertThat(updatedPet.getSpecies()).isEqualTo("Dog");
        assertThat(updatedPet.getAge()).isEqualTo(4);
        assertThat(updatedPet.getOwnerName()).isEqualTo("Jane");
    }

    @Test
    void deleteById_ShouldRemovePet() {
        Pet savedPet = petJpaRepository.save(pet);

        petSqlRepository.deleteById(savedPet.getPetId());

        assertThat(petJpaRepository.findById(savedPet.getPetId())).isEmpty();
    }
}
