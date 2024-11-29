package com.is.lw.core.controller;

import com.is.lw.core.service.CommandsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/commands")
@Tag(name = "Commands Controller")
public class CommandsController {

    private final CommandsService service;

    @DeleteMapping("/{minimalPoint}")
    @Operation(summary = "Delete laboratory work with the specified minimal point")
    public ResponseEntity<String> deleteWhereMinimalPointEqualsValue(@PathVariable Integer minimalPoint) {
        return service.deleteOneLabWorkByMinimalPoint(minimalPoint);
    }

    @GetMapping("/{minimalPoint}")
    @Operation(summary = "Get laboratory works with minimal point greater entered value")
    public ResponseEntity<String> getWhereMinimalPointGreaterValue(@PathVariable Integer minimalPoint) {
        return service.getCountByMinimalPoint(minimalPoint);
    }

    @GetMapping("/substring/{substring}")
    @Operation(summary = "Find laboratory works which contains substring")
    public ResponseEntity<?> getWhereSubstring(@PathVariable String substring) {
        return service.getLabWorksByDescriptionSubstring(substring);
    }

    @PostMapping("/{disciplineId}/labworks/{labWorkId}")
    @Operation(summary = "Add laboratory work to the discipline program")
    public ResponseEntity<String> addLabWorkFromDiscipline(
            @PathVariable Long labWorkId,
            @PathVariable Long disciplineId) {
        return service.addLabWorkToDiscipline(labWorkId, disciplineId);
    }

    @DeleteMapping("/labworks/{labWorkId}")
    @Operation(summary = "Delete laboratory work from the discipline program")
    public ResponseEntity<String> removeLabWorkFromDiscipline(
            @PathVariable Long labWorkId) {
        return service.deleteLabWorkToDiscipline(labWorkId);
    }

}
