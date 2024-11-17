package com.is.lw.auth.controller;

import com.is.lw.auth.controller.request.AuthenticationRequest;
import com.is.lw.auth.controller.request.RegisterRequest;
import com.is.lw.auth.controller.response.AuthenticationResponse;
import com.is.lw.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller", description = "Endpoints for authorization and registration")
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "Register a new user", description = "Register a new user with a unique email address.")
    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.registerUser(request));
    }

    @Operation(summary = "User authorization")
    @PostMapping("/user/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticateUser(request));
    }

    @Operation(summary = "Register a new admin", description = "Register a new admin with a unique email address, access to the account cannot be obtained until the account passes moderation.")
    @PostMapping("/admin/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.registerAdmin(request));
    }

    @Operation(summary = "Admin authorization", description = "Admin authorization, access to the account cannot be obtained until the account passes moderation.")
    @PostMapping("/admin/authentication")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticateAdmin(request));
    }

}
