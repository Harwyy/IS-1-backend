package com.is.lw.core.controller;

import com.is.lw.core.model.Coordinates;
import com.is.lw.core.service.CoordinatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coordinates")
@Tag(name = "Coordinates Controller")
public class CoordinatesController {

    private final CoordinatesService service;

    @PostMapping
    @Operation(summary = "Create new coordinates", description = "Create a new coordinates in the system.")
    public ResponseEntity<Coordinates> createCoordinates(
            @RequestBody Coordinates coordinates
    ) {
        return service.createCoordinates(coordinates);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coordinates by ID", description = "Retrieve a coordinates by its unique ID.")
    public ResponseEntity<Coordinates> getCoordinatesById(
            @PathVariable Long id
    ) {
        return service.getCoordinatesById(id);
    }

    @PutMapping
    @Operation(summary = "Update coordinates", description = "Update an existing coordinates. Can be done only by the creator or an admin.")
    public ResponseEntity<Coordinates> updateCoordinates(
            @RequestBody Coordinates coordinates
    ) {
        return service.updateCoordinates(coordinates);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete coordinates", description = "Delete a coordinates by its ID. Can be done only by the creator or an admin.")
    public ResponseEntity<String> deleteCoordinates(
            @PathVariable Long id
    ) {
        return service.deleteCoordinates(id);
    }

    @GetMapping("/my")
    @Operation(summary = "Get all my coordinates", description = "Retrieve a paginated list of all my coordinates.")
    public ResponseEntity<List<Coordinates>> getMyCoordinates() {
        return service.getMyCoordinates();
    }

    @GetMapping
    @Operation(summary = "Get all coordinates", description = "Retrieve a paginated list of all coordinates.")
    public ResponseEntity<List<Coordinates>> getAllCoordinates(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.getAllCoordinates(sortBy, direction, page, size);
    }
}
