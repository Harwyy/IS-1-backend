package com.is.lw.auth.service;

import com.is.lw.auth.controller.AuthenticationRequest;
import com.is.lw.auth.controller.AuthenticationResponse;
import com.is.lw.auth.controller.RegisterRequest;
import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.auth.model.enums.Status;
import com.is.lw.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isConfirmed(true)
                .isEnabled(true)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .status(Status.SUCCESS)
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .status(Status.SUCCESS)
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .isConfirmed(false)
                .isEnabled(false)
                .build();
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        if (user.isConfirmed()) {
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .status(Status.SUCCESS)
                    .token(jwtToken)
                    .build();
        }
        return AuthenticationResponse.builder()
                .status(Status.FAIL)
                .message("Your credentials have not been processed yet.")
                .build();
    }
}
