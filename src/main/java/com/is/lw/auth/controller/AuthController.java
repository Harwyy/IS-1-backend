package com.is.lw.auth.controller;

import com.is.lw.auth.service.AuthService;
import com.is.lw.auth.service.request.LoginRequest;
import com.is.lw.auth.service.request.RegisterRequest;
import com.is.lw.auth.service.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Register a new user", description = "Register a new user with a unique username and password.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @Operation(summary = "Login user", description = "Authenticate a user and generate a JWT token if credentials are valid.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}
