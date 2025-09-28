package com.example.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String token;
    private String username;
    private List<String> roles; // in case user has multiple roles
}
