package com.example.banking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String username;
    private String email;
}
