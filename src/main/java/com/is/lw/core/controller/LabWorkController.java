package com.is.lw.core.controller;

import com.is.lw.core.model.LabWork;
import com.is.lw.core.service.LabWorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/labworks")
@Tag(name = "Laboratory Work Controller")
public class LabWorkController {

    private final LabWorkService service;

    @Operation(summary = "Create new laboratory work", description = "Create a new laboratory work in the system.")
    @PostMapping
    public ResponseEntity<LabWork> createPerson(@RequestBody LabWork labWork) {
        return service.createLabWork(labWork);
    }

    @Operation(summary = "Get laboratory work by ID", description = "Retrieve a laboratory work by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<LabWork> getLabWorkByID(@PathVariable Long id) {
        return service.getLabWorkById(id);
    }

    @Operation(summary = "Update laboratory work", description = "Update an existing laboratory work. Can be done only by the creator or an admin.")
    @PutMapping()
    public ResponseEntity<LabWork> updateLabWork(@RequestBody LabWork labWork) {
        return service.updateLabWork(labWork);
    }

    @Operation(summary = "Get all my laboratory works", description = "Retrieve a list of my all laboratory works.")
    @GetMapping("/my")
    private ResponseEntity<List<LabWork>> getAllMyLabWorks(){
        return service.getMyLabWorks();
    }

    @Operation(summary = "Delete laboratory work", description = "Delete a laboratory work by its ID. Can be done only by the creator or an admin.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        return service.deleteLabWork(id);
    }

    @Operation(summary = "Get all laboratory works", description = "Retrieve a paginated list of all laboratory works.")
    @GetMapping
    public ResponseEntity<List<LabWork>> getAllLocations(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) String descriptionContains,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAllLabWorks(nameContains, descriptionContains, sortBy, direction, page, size);
    }

}
