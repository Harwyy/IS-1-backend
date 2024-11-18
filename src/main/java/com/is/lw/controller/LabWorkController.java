package com.is.lw.controller;

import com.is.lw.controller.Request.LabWorkAddRequest;
import com.is.lw.controller.Request.LabWorkUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.LabWork;
import com.is.lw.service.LabWorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/labwork")
@Tag(name = "LabWork Controller", description = "Endpoints for managing labwork data, including adding, updating, deleting, and filtering labwork.")
public class LabWorkController {

    private final LabWorkService service;

    @Operation(
            summary = "Add new labwork",
            description = "Allows the user to add a new labwork to the system."
    )
    @PostMapping("/add")
    public ResponseEntity<MyResponse> addLocation(
            @RequestBody LabWorkAddRequest request
    ) {
        return ResponseEntity.ok(service.addLabWork(request));
    }

    @Operation(
            summary = "Update existing labwork",
            description = "Allows the user to update the details of an existing labwork."
    )
    @PutMapping("/update")
    public ResponseEntity<MyResponse> updateLocation(
            @RequestBody LabWorkUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateLabWork(request));
    }

    @Operation(
            summary = "Delete labwork by ID"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MyResponse> deleteLocation(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteLabWork(id));
    }

    @Operation(
            summary = "Filter and sort lab works",
            description = "Allows the user to filter and sort lab works based on various criteria such as name, coordinates, difficulty, discipline, minimal point, and author. Pagination and sorting options are supported."
    )
    @GetMapping("/filter")
    public ResponseEntity<List<LabWork>> filterAndSortLabWorks(
            @RequestParam(required = false) String nameEquals,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) Long coordinatesIdEquals,
            @RequestParam(required = false) String descriptionContains,
            @RequestParam(required = false) String difficultyEquals,
            @RequestParam(required = false) Long disciplineIdEquals,
            @RequestParam(required = false) Float minimalPointEquals,
            @RequestParam(required = false) Float minimalPointGreaterThan,
            @RequestParam(required = false) Float minimalPointLessThan,
            @RequestParam(required = false) Long authorIdEquals,
            @RequestParam(required = false) Long createdByIdEquals,
            @RequestParam(required = false) Long updatedByIdEquals,
            @RequestParam(required = false) LocalDateTime createdAtAfter,
            @RequestParam(required = false) LocalDateTime createdAtBefore,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<LabWork> filteredLabWorks = service.filterAndSortLabWorks(
                nameEquals,
                nameContains,
                coordinatesIdEquals,
                descriptionContains,
                difficultyEquals,
                disciplineIdEquals,
                minimalPointEquals,
                minimalPointGreaterThan,
                minimalPointLessThan,
                authorIdEquals,
                createdByIdEquals,
                updatedByIdEquals,
                createdAtAfter,
                createdAtBefore,
                sortBy,
                direction,
                page,
                size
        );
        return ResponseEntity.ok(filteredLabWorks);
    }

}
