package com.example.banking.controller;

import com.example.banking.dto.AccountResponse;
import com.example.banking.dto.ApiResponse;
import com.example.banking.entity.Account;
import com.example.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create/{customerId}")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @PathVariable Long customerId,
            @RequestParam String accountType) {

        Account account = accountService.createAccount(customerId, accountType);

        // Map Account entity to AccountResponse DTO
        AccountResponse accountResponse = AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .customerId(account.getCustomer().getId())
                .customerName(account.getCustomer().getFullName())
                .build();

        ApiResponse<AccountResponse> response = ApiResponse.<AccountResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Account created successfully")
                .data(accountResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getBalance(accountNumber);
        ApiResponse<BigDecimal> response = ApiResponse.<BigDecimal>builder()
                .status(200)
                .message("Balance fetched successfully")
                .data(balance)
                .build();
        return ResponseEntity.ok(response);
    }
}
