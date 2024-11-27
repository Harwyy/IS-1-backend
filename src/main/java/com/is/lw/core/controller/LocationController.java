package com.is.lw.core.controller;

import com.is.lw.core.model.Location;
import com.is.lw.core.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Location Controller")
public class LocationController {

    private final LocationService service;

    @Operation(summary = "Create new location", description = "Create a new location in the system.")
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return service.createLocation(location);
    }

    @Operation(summary = "Get location by ID", description = "Retrieve a location by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        return service.getLocationById(id);
    }

    @Operation(summary = "Get all locations", description = "Retrieve a paginated list of all locations.")
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAllLocations(nameContains, sortBy, direction, page, size);
    }

    @Operation(summary = "Get all my locations", description = "Retrieve a list of my all locations.")
    @GetMapping("/my")
    private ResponseEntity<List<Location>> getAllMyLocations(){
        return service.getMyLocations();
    }

    @Operation(summary = "Update location", description = "Update an existing location. Can be done only by the creator or an admin.")
    @PutMapping()
    public ResponseEntity<Location> updateLocation(@RequestBody Location location) {
        return service.updateLocation(location);
    }

    @Operation(summary = "Delete location", description = "Delete a location by its ID. Can be done only by the creator or an admin.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        return service.deleteLocation(id);
    }
}
