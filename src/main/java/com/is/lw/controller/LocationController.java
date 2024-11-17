package com.is.lw.controller;

import com.is.lw.controller.Request.LocationAddRequest;
import com.is.lw.controller.Request.LocationUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.Location;
import com.is.lw.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/location")
@Tag(name = "Location Controller", description = "Endpoints for managing location data, including adding, updating, deleting, and filtering discipline.")
public class LocationController {

    private final LocationService service;

    @Operation(
            summary = "Add new location",
            description = "Allows the user to add a new set of location to the system."
    )
    @PostMapping("/add")
    public ResponseEntity<MyResponse> addLocation(
            @RequestBody LocationAddRequest request
    ) {
        return ResponseEntity.ok(service.addLocation(request));
    }

    @Operation(
            summary = "Update existing location",
            description = "Allows the user to update the details of an existing set of location."
    )
    @PutMapping("/update")
    public ResponseEntity<MyResponse> updateLocation(
            @RequestBody LocationUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateLocation(request));
    }

    @Operation(
            summary = "Delete location by ID",
            description = "Allows the user to delete a specific set of location by providing the unique ID of the location record to be deleted."
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MyResponse> deleteLocation(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteLocation(id));
    }

    @Operation(
            summary = "Filter and sort location",
            description = "Allows the user to filter and sort location based on various criteria such as name, x, y, z. Pagination and sorting options are supported."
    )
    @GetMapping("/filter")
    public ResponseEntity<List<Location>> filterAndSortLocations(
            @RequestParam(required = false) String nameEquals,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) Long xEquals,
            @RequestParam(required = false) Long xGreaterThan,
            @RequestParam(required = false) Long xLessThan,
            @RequestParam(required = false) Double yEquals,
            @RequestParam(required = false) Double yGreaterThan,
            @RequestParam(required = false) Double yLessThan,
            @RequestParam(required = false) Integer zEquals,
            @RequestParam(required = false) Integer zGreaterThan,
            @RequestParam(required = false) Integer zLessThan,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<Location> filteredLocations = service.filterAndSortLocations(
                nameEquals,
                nameContains,
                xEquals,
                xGreaterThan,
                xLessThan,
                yEquals,
                yGreaterThan,
                yLessThan,
                zEquals,
                zGreaterThan,
                zLessThan,
                sortBy,
                direction,
                page,
                size
        );
        return ResponseEntity.ok(filteredLocations);
    }

}
