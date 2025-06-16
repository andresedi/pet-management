package com.example.petmanagement.controller;

import com.example.petmanagement.dto.PaginatedResponse;
import com.example.petmanagement.dto.PetPageRequest;
import com.example.petmanagement.dto.PetRequest;
import com.example.petmanagement.dto.PetResponse;
import com.example.petmanagement.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/pets", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pet", description = "Operations related to pet management")
@Validated
@Log4j2
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    @Operation(description = "Get pets with pagination and sorting")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pets"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "405", description = "Method not allowed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaginatedResponse<PetResponse>> getAllPets(@Valid @ModelAttribute PetPageRequest params) {
        log.debug("Receiving request to get all pets with page: {}, size: {}, sortBy: {}, direction: {}", params.getPage(),
                params.getSize(), params.getSortBy(), params.getDirection());
        Sort sort = "asc".equalsIgnoreCase(params.getDirection()) ?
                Sort.by(params.getSortBy()).ascending() :
                Sort.by(params.getSortBy()).descending();
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sort);

        PaginatedResponse<PetResponse> pets = petService.getAllPets(pageable);
        log.debug("Returning {} pets", pets.getContent().size());
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get pet with specified id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pet"),
        @ApiResponse(responseCode = "404", description = "Pet not found"),
        @ApiResponse(responseCode = "405", description = "Method not allowed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long id) {
        log.debug("Receiving request to get pet by id: {}", id);
        PetResponse pet = petService.getPetById(id);
        log.debug("Returning pet: {}", pet);
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    @Operation(description = "Create pet with specified details")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully created pet"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "405", description = "Method not allowed"),
        @ApiResponse(responseCode = "409", description = "Database operation failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PetResponse> createPet(@RequestBody @Valid PetRequest petRequest) {
        log.debug("Receiving request to create new pet: {}", petRequest);
        PetResponse createdPet = petService.createPet(petRequest);
        log.debug("Pet created successfully: {}", createdPet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update pet with specified id and details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully updated pet"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Pet not found"),
        @ApiResponse(responseCode = "405", description = "Method not allowed"),
        @ApiResponse(responseCode = "409", description = "Database operation failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @RequestBody @Valid PetRequest petRequest) {
        log.debug("Receiving request to update pet with id: {}, details: {}", id, petRequest);
        PetResponse updatedPet = petService.updatePet(id, petRequest);
        log.debug("Pet updated successfully: {}", updatedPet);
        return ResponseEntity.ok(updatedPet);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete pet with specified id")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Successfully deleted pet"),
        @ApiResponse(responseCode = "404", description = "Pet not found"),
        @ApiResponse(responseCode = "405", description = "Method not allowed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.debug("Receiving request to delete pet with id: {}", id);
        petService.deletePet(id);
        log.debug("Pet with id: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
