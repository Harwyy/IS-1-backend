package com.is.lw.auth.service;

import com.is.lw.auth.config.JwtConfig;
import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.auth.repository.UserRepository;
import com.is.lw.auth.service.request.LoginRequest;
import com.is.lw.auth.service.request.RegisterRequest;
import com.is.lw.auth.service.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public ResponseEntity<String> register(RegisterRequest request){

        if (request == null || request.getUsername() == null || request.getPassword() == null || request.getRole() == null) {
            return ResponseEntity.badRequest().body("Invalid data: missing required fields");
        }

        if (request.getUsername().isEmpty() || request.getUsername().length() < 3) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Username must be at least 3 characters long");
        }
        if (request.getUsername().contains(" ")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Username must not contain spaces");
        }

        if (request.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Password must be at least 8 characters long");
        }
        if (request.getPassword().contains(" ")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Password must not contain spaces");
        }

        if (!EnumSet.of(Role.USER, Role.ADMIN).contains(request.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user role");
        }

        if (repository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username is already taken");
        }

        if (repository.existsByPassword(encoder.encode(request.getPassword()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Password is already taken");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .role(request.getRole())
                .isConfirmed(Role.USER.equals(request.getRole()))
                .isEnabled(Role.USER.equals(request.getRole()))
                .build();
        repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User successfully registered");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            User user = repository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.isEnabled()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(LoginResponse.builder()
                                .message("Your account is not enable")
                                .build());
            }

            if (Role.ADMIN.equals(user.getRole()) && !user.isConfirmed()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(LoginResponse.builder()
                                .message("Your account is not confirmed")
                                .build());
            }

            String token = jwtConfig.generateToken(user);

            return ResponseEntity.ok(
                    LoginResponse.builder()
                            .token(token)
                            .message("Login successful")
                            .build()
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponse.builder()
                            .message("Invalid username or password")
                            .build());
        }
    }
}
