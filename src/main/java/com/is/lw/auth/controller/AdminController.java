package com.is.lw.auth.controller;

import com.is.lw.auth.model.UserSummary;
import com.is.lw.auth.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Controller", description = "Endpoints for managing admin users.")
public class AdminController {

    private final AdminService service;

    @Operation(
            summary = "Get list of unconfirmed admins",
            description = "Fetches a list of admins who are not confirmed yet.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of unconfirmed admins",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserSummary.class)))),
            }
    )
    @GetMapping("/unconfirmed")
    public ResponseEntity<List<UserSummary>> getUnconfirmedAdmins() {
        return service.getUnconfirmedAdmins();
    }

    @Operation(summary = "Confirm an admin", description = "Confirms an admin user by their ID. Only an admin can perform this action.")
    @PutMapping("/confirm/{adminId}")
    public ResponseEntity<String> confirmAdmin(@PathVariable @Parameter(description = "ID of the admin to be confirmed", example = "1") Long adminId) {
        return service.confirmAdmin(adminId);
    }
}