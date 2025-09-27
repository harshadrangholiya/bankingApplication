package com.example.banking.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role; // e.g., "CUSTOMER" or "ADMIN"
}
