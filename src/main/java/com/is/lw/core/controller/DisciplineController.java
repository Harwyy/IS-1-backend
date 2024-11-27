package com.is.lw.core.controller;

import com.is.lw.core.model.Discipline;
import com.is.lw.core.service.DisciplineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disciplines")
@RequiredArgsConstructor
@Tag(name = "Discipline Controller")
public class DisciplineController {

    private final DisciplineService service;

    @Operation(summary = "Create a new discipline", description = "Create a new discipline in the system.")
    @PostMapping
    public ResponseEntity<Discipline> createDiscipline(@RequestBody Discipline discipline) {
        return service.createDiscipline(discipline);
    }

    @Operation(summary = "Get a disciplines by ID", description = "Retrieve a disciplines by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Discipline> getDisciplineById(@PathVariable Long id) {
        return service.getDisciplineById(id);
    }

    @Operation(summary = "Update discipline", description = "Update an existing discipline. Can be done only by the creator or an admin.")
    @PutMapping
    public ResponseEntity<Discipline> updateDiscipline(@RequestBody Discipline discipline) {
        return service.updateDiscipline(discipline);
    }

    @Operation(summary = "Delete a discipline by ID", description = "Delete a discipline by its ID. Can be done only by the creator or an admin.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscipline(@PathVariable Long id) {
        return service.deleteDiscipline(id);
    }

    @Operation(summary = "Get all my disciplines", description = "Retrieve a list of my all disciplines.")
    @GetMapping("/my")
    public ResponseEntity<List<Discipline>> getMyDisciplines() {
        return service.getMyDisciplines();
    }

    @Operation(summary = "Get all disciplines with optional filtering and pagination")
    @GetMapping
    public ResponseEntity<List<Discipline>> getAllDisciplines(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAllDisciplines(nameContains, sortBy, direction, page, size);
    }
}
