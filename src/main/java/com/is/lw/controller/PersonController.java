package com.is.lw.controller;

import com.is.lw.controller.Request.PersonAddRequest;
import com.is.lw.controller.Request.PersonUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.Person;
import com.is.lw.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/person")
@Tag(name = "Person Controller", description = "Endpoints for managing person data, including adding, updating, deleting, and filtering discipline.")
public class PersonController {

    private final PersonService service;

    @Operation(
            summary = "Add new person",
            description = "Allows the user to add a new person to the system."
    )
    @PostMapping("/add")
    public ResponseEntity<MyResponse> addLocation(
            @RequestBody PersonAddRequest request
    ) {
        return ResponseEntity.ok(service.addPerson(request));
    }

    @Operation(
            summary = "Update existing person",
            description = "Allows the user to update the details of an existing person."
    )
    @PutMapping("/update")
    public ResponseEntity<MyResponse> updateLocation(
            @RequestBody PersonUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updatePerson(request));
    }

    @Operation(
            summary = "Delete person by ID",
            description = "Allows the user to delete a specific person by providing the unique ID of the location record to be deleted."
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MyResponse> deleteLocation(@PathVariable Long id) {
        return ResponseEntity.ok(service.deletePerson(id));
    }

    @Operation(
            summary = "Filter and sort persons",
            description = "Allows the user to filter and sort person based on various criteria such as name, color, weight, nationality, and location. Pagination and sorting options are supported."
    )
    @GetMapping("/filter")
    public ResponseEntity<List<Person>> filterAndSortPersons(
            @RequestParam(required = false) String nameEquals,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) String colorEquals,
            @RequestParam(required = false) String hairColorEquals,
            @RequestParam(required = false) Long weightEquals,
            @RequestParam(required = false) Long weightGreaterThan,
            @RequestParam(required = false) Long weightLessThan,
            @RequestParam(required = false) String nationalityEquals,
            @RequestParam(required = false) Long locationIdEquals,
            @RequestParam(required = false) Long idEquals,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<Person> filteredPersons = service.filterAndSortPersons(
                nameEquals,
                nameContains,
                colorEquals,
                hairColorEquals,
                weightEquals,
                weightGreaterThan,
                weightLessThan,
                nationalityEquals,
                locationIdEquals,
                idEquals,
                sortBy,
                direction,
                page,
                size
        );
        return ResponseEntity.ok(filteredPersons);
    }


}
