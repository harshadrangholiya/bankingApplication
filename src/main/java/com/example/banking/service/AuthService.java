package com.example.banking.service;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.LoginRequest;
import com.example.banking.dto.RegisterRequest;
import com.example.banking.dto.AuthResponse;
import com.example.banking.entity.Customer;
import com.example.banking.entity.Role;
import com.example.banking.repository.CustomerRepository;
import com.example.banking.repository.RoleRepository;
import com.example.banking.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user in the system.
     *
     * <p>If the username already exists, a {@link RuntimeException} is thrown.
     * If the role specified in the request does not exist, it is created automatically.</p>
     *
     * @param request the registration request containing username, password, full name, email, phone, and role
     * @return an {@link ApiResponse} containing the username and success message
     * @throws RuntimeException if the username is already taken
     */

    public ApiResponse<String> register(RegisterRequest request) {
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken!");
        }

        // Get role from request
        String roleName = request.getRole() != null ? request.getRole().toUpperCase() : "CUSTOMER";

        // Find role or create if not exists
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(roleName)
                            .build();
                    return roleRepository.save(newRole);
                });

        Customer customer = Customer.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();

        customerRepository.save(customer);

        return ApiResponse.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully with role " + roleName)
                .data(request.getUsername())
                .build();
    }


    /**
     * Authenticates a user and generates a JWT token.
     *
     * <p>The login request must contain the username and password. If authentication
     * fails or the user does not exist, a {@link RuntimeException} is thrown.</p>
     *
     * @param request the login request containing username and password
     * @return an {@link AuthResponse} containing the JWT token
     * @throws RuntimeException if the user is not found
     */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var userDetails = customerRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var springUser = new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getRoles().stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                        .toList()
        );

        String token = jwtUtil.generateToken(springUser);
        return new AuthResponse(token);
    }
}
