package com.is.lw.controller;

import com.is.lw.controller.Request.DisciplineAddRequest;
import com.is.lw.controller.Request.DisciplineUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.Discipline;
import com.is.lw.service.DisciplineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/discipline")
@Tag(name = "Discipline Controller", description = "Endpoints for managing discipline data, including adding, updating, deleting, and filtering discipline.")
public class DisciplineController {

    private final DisciplineService service;

    @Operation(
            summary = "Add new discipline",
            description = "Allows the user to add a new set of discipline to the system."
    )
    @PostMapping("/add")
    public ResponseEntity<MyResponse> addCoordinates(
            @RequestBody DisciplineAddRequest request
    ) {
        return ResponseEntity.ok(service.addDiscipline(request));
    }
    @Operation(
            summary = "Update existing discipline",
            description = "Allows the user to update the details of an existing set of discipline."
    )
    @PutMapping("/update")
    public ResponseEntity<MyResponse> updateDiscipline(
            @RequestBody DisciplineUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateDiscipline(request));
    }

    @Operation(
            summary = "Delete discipline by ID",
            description = "Allows the user to delete a specific set of discipline by providing the unique ID of the discipline record to be deleted."
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MyResponse> deleteCoordinates(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteDiscipline(id));
    }

    @Operation(
            summary = "Filter and sort discipline",
            description = "Allows the user to filter and sort discipline based on various criteria such as name, lecture hours, and labs count, along with other attributes. Pagination and sorting options are supported."
    )
    @GetMapping("/filter")
    public ResponseEntity<List<Discipline>> filterAndSortDisciplines(
            @RequestParam(required = false) String nameEquals,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) Long lectureHoursEquals,
            @RequestParam(required = false) Long lectureHoursGreaterThan,
            @RequestParam(required = false) Long lectureHoursLessThan,
            @RequestParam(required = false) Integer practiceHoursEquals,
            @RequestParam(required = false) Integer practiceHoursGreaterThan,
            @RequestParam(required = false) Integer practiceHoursLessThan,
            @RequestParam(required = false) Integer selfStudyHoursEquals,
            @RequestParam(required = false) Integer selfStudyHoursGreaterThan,
            @RequestParam(required = false) Integer selfStudyHoursLessThan,
            @RequestParam(required = false) Long labsCountEquals,
            @RequestParam(required = false) Long labsCountGreaterThan,
            @RequestParam(required = false) Long labsCountLessThan,
            @RequestParam(required = false) Long idEquals,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<Discipline> filteredDisciplines = service.filterAndSortDisciplines(
                nameEquals,
                nameContains,
                lectureHoursEquals,
                lectureHoursGreaterThan,
                lectureHoursLessThan,
                practiceHoursEquals,
                practiceHoursGreaterThan,
                practiceHoursLessThan,
                selfStudyHoursEquals,
                selfStudyHoursGreaterThan,
                selfStudyHoursLessThan,
                labsCountEquals,
                labsCountGreaterThan,
                labsCountLessThan,
                idEquals,
                sortBy,
                direction,
                page,
                size
        );
        return ResponseEntity.ok(filteredDisciplines);
    }

}
