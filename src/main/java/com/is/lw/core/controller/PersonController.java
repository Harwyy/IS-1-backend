package com.is.lw.core.controller;

import com.is.lw.core.model.Person;
import com.is.lw.core.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/person")
@Tag(name = "Person Controller")
public class PersonController {

    private final PersonService service;

    @Operation(summary = "Create new person", description = "Create a new person in the system.")
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return service.createPerson(person);
    }

    @Operation(summary = "Get person by ID", description = "Retrieve a person by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return service.getPersonById(id);
    }

    @Operation(summary = "Update person", description = "Update an existing person. Can be done only by the creator or an admin.")
    @PutMapping()
    public ResponseEntity<Person> updateLocation(@RequestBody Person person) {
        return service.updatePerson(person);
    }

    @Operation(summary = "Get all my people", description = "Retrieve a list of my all people.")
    @GetMapping("/my")
    private ResponseEntity<List<Person>> getAllMyLocations(){
        return service.getMyPersons();
    }

    @Operation(summary = "Delete person", description = "Delete a person by its ID. Can be done only by the creator or an admin.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        return service.deletePerson(id);
    }

    @Operation(summary = "Get all people", description = "Retrieve a paginated list of all people.")
    @GetMapping
    public ResponseEntity<List<Person>> getAllLocations(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAllPersons(nameContains, sortBy, direction, page, size);
    }
}
