package com.is.lw.core.controller;

import com.is.lw.core.service.SpecialOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/special-operations")
@Tag(name = "Special Operations", description = "Perform special database operations.")
public class SpecialOperationsController {

    private final SpecialOperationsService service;

//    @Operation(summary = "Delete one by minimalPoint", description = "Deletes one object with the given minimalPoint.")
//    @DeleteMapping("/delete-by-minimal-point")
//    public ResponseEntity<Void> deleteOneByMinimalPoint(@RequestParam Double minimalPoint) {
//        service.deleteOneByMinimalPoint(minimalPoint);
//        return ResponseEntity.ok().build();
//    }

//    @Operation(summary = "Count objects with minimalPoint greater than", description = "Returns the count of objects with minimalPoint greater than the given value.")
//    @GetMapping("/count-by-minimal-point-greater")
//    public ResponseEntity<Long> countByMinimalPointGreaterThan(@RequestParam Double minimalPoint) {
//        return ResponseEntity.ok(service.countByMinimalPointGreaterThan(minimalPoint));
//    }
//
//    @Operation(summary = "Find by description substring", description = "Finds objects whose description contains the given substring.")
//    @GetMapping("/find-by-description")
//    public ResponseEntity<List<Object>> findByDescriptionContains(@RequestParam String substring) {
//        return ResponseEntity.ok(service.findByDescriptionContains(substring));
//    }

}
