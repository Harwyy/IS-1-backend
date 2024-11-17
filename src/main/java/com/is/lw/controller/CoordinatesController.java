package com.is.lw.controller;

import com.is.lw.controller.Request.CoordinatesAddRequest;
import com.is.lw.controller.Request.CoordinatesUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.Coordinates;
import com.is.lw.service.CoordinatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/coordinates")
@Tag(name = "Coordinates Controller", description = "Endpoints for managing coordinates data, including adding, updating, deleting, and filtering coordinates.")
public class CoordinatesController {

    private final CoordinatesService service;

    @Operation(
            summary = "Add new coordinates",
            description = "Allows the user to add a new set of coordinates to the system."
    )
    @PostMapping("/add")
    public ResponseEntity<MyResponse> addCoordinates(
            @RequestBody CoordinatesAddRequest request
    ) {
        return ResponseEntity.ok(service.addCoordinates(request));
    }

    @Operation(
            summary = "Update existing coordinates",
            description = "Allows the user to update the details of an existing set of coordinates."
    )
    @PutMapping("/update")
     public ResponseEntity<MyResponse> updateCoordinates(
             @RequestBody CoordinatesUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateCoordinates(request));
    }

    @Operation(
            summary = "Delete coordinates by ID",
            description = "Allows the user to delete a specific set of coordinates by providing the unique ID of the coordinate record to be deleted."
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MyResponse> deleteCoordinates(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteCoordinates(id));
    }

    @Operation(
            summary = "Filter and sort coordinates",
            description = "Allows the user to filter and sort coordinates based on various criteria such as X, Y, and Z values, along with other attributes. Pagination and sorting options are supported."
    )
    @GetMapping("/filter")
    public ResponseEntity<List<Coordinates>> filterAndSortCoordinates(
            @RequestParam(required = false) Float xEquals,
            @RequestParam(required = false) Float xGreaterThan,
            @RequestParam(required = false) Float xLessThan,
            @RequestParam(required = false) Long yEquals,
            @RequestParam(required = false) Long yGreaterThan,
            @RequestParam(required = false) Long yLessThan,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer zEquals,
            @RequestParam(required = false) Integer zGreaterThan,
            @RequestParam(required = false) Integer zLessThan,
            @RequestParam(required = false) Long idEquals,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<Coordinates> filteredCoordinates = service.filterAndSortCoordinates(
                xEquals,
                xGreaterThan,
                xLessThan,
                yEquals,
                yGreaterThan,
                yLessThan,
                zEquals,
                zGreaterThan,
                zLessThan,
                idEquals,
                sortBy,
                direction,
                page,
                size
        );
        return ResponseEntity.ok(filteredCoordinates);
    }

}
