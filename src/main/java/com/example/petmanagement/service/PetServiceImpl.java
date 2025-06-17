package com.example.petmanagement.service;

import com.example.petmanagement.dto.PaginatedResponse;
import com.example.petmanagement.dto.PetRequest;
import com.example.petmanagement.dto.PetResponse;
import com.example.petmanagement.exception.PetNotFoundException;
import com.example.petmanagement.persistence.model.Pet;
import com.example.petmanagement.persistence.model.PetIdentifierConverter;
import com.example.petmanagement.persistence.repository.PetRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    @Autowired
    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<PetResponse> getAllPets(Pageable pageable) {
        log.debug("Finding all pets with pagination: {}", pageable);
        Page<Pet> petPage = petRepository.findAll(pageable);
        List<PetResponse> petResponses = petPage.getContent().stream()
                .map(Pet::toPetResponseDto)
                .toList();

        PaginatedResponse <PetResponse> paginatedResponse = PaginatedResponse.<PetResponse>builder()
                .content(petResponses)
                .pageNumber(petPage.getNumber())
                .pageSize(petPage.getSize())
                .totalElements(petPage.getTotalElements())
                .totalPages(petPage.getTotalPages())
                .last(petPage.isLast())
                .build();
        log.debug("Found {} pets", petPage.getContent().size());
        return paginatedResponse;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "pets", key = "#id")
    public PetResponse getPetById(String id) {
        log.debug("Finding pet by id: {}", id);
        Long petId = PetIdentifierConverter.of(id).asLong();
        PetResponse pet = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException(id)).toPetResponseDto();
        log.debug("Found pet: {}", pet);
        return pet;
    }

    @Override
    public PetResponse createPet(PetRequest petRequest) {
        log.info("Creating new pet: {}", petRequest);
        PetResponse saved = petRepository.save(petRequest.toPetModel()).toPetResponseDto();
        log.info("Created pet with id: {}", saved.getPetId());
        return saved;
    }

    @Override
    public PetResponse updatePet(String id, PetRequest petRequest) {
        log.info("Updating pet with id: {}", id);
        Long petId = PetIdentifierConverter.of(id).asLong();
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException(id));
        log.debug("Found pet for update: {}", pet);
        pet.setName(petRequest.getName());
        pet.setSpecies(petRequest.getSpecies());
        pet.setAge(petRequest.getAge());
        pet.setOwnerName(petRequest.getOwnerName());
        PetResponse updatedPet = petRepository.save(pet).toPetResponseDto();
        log.info("Updated pet: {}", updatedPet);
        return updatedPet;
    }

    @Override
    @CacheEvict(value = "pets", key = "#id")
    public void deletePet(String id) {
        log.info("Deleting pet with id: {}", id);
        Long petId = PetIdentifierConverter.of(id).asLong();
        petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException(id));
        petRepository.deleteById(petId);
        log.info("Deleted pet with id: {}", petId);
    }
}
