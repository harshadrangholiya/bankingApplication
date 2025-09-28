package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.CustomerResponse;
import com.example.banking.entity.Customer;
import com.example.banking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves a list of all customers.
     *
     * @return a ResponseEntity containing ApiResponse with list of customers
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();

            // Map Customer entity to CustomerResponse DTO
            List<CustomerResponse> customerResponses = customers.stream()
                    .map(c -> CustomerResponse.builder()
                            .id(c.getId())
                            .username(c.getUsername())
                            .email(c.getEmail())
                            .build())
                    .collect(Collectors.toList());

            ApiResponse<List<CustomerResponse>> response = ApiResponse.<List<CustomerResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Customers fetched successfully")
                    .data(customerResponses)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<List<CustomerResponse>> errorResponse = ApiResponse.<List<CustomerResponse>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch customers: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
