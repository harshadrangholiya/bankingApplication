package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.LoginRequest;
import com.example.banking.dto.RegisterRequest;
import com.example.banking.dto.AuthResponse;
import com.example.banking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Registers a new user in the system.
     *
     * <p>The registration request contains user details such as
     * username, password, and role. If successful, a success message
     * is returned. Otherwise, an error response with status 400 is sent.</p>
     *
     * @param request the registration request containing user details
     * @return a {@link ResponseEntity} with an {@link ApiResponse} that includes
     * either a success message or an error message
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request) {
        try {
            ApiResponse<String> response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * <p>The login request contains the username and password. If the
     * credentials are valid, a token and user details are returned.
     * Otherwise, an unauthorized response is sent.</p>
     *
     * @param request the login request containing username and password
     * @return a {@link ResponseEntity} with an {@link ApiResponse} that includes
     * either the authentication response with token or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);
            ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Login successful")
                    .data(authResponse)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ApiResponse<AuthResponse> errorResponse = ApiResponse.<AuthResponse>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message(ex.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
