package com.example.banking.controller;

import com.example.banking.dto.AccountResponse;
import com.example.banking.dto.ApiResponse;
import com.example.banking.entity.Account;
import com.example.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Creates a new account for a given customer.
     *
     * <p>The account type is provided as a request parameter,
     * while the customer is identified by a path variable.</p>
     *
     * @param customerId  the ID of the customer for whom the account is being created
     * @param accountType the type of account (e.g., "SAVINGS", "CURRENT")
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     * with the newly created account details or an error message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/{customerId}")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @PathVariable Long customerId,
            @RequestParam String accountType) {

        try {
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

        } catch (Exception ex) {
            ApiResponse<AccountResponse> errorResponse = ApiResponse.<AccountResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to create account: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Retrieves the balance of an account by its account number.
     *
     * @param accountNumber the unique account number
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     * with the balance or an error message if the account is not found
     */
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable String accountNumber) {
        try {
            BigDecimal balance = accountService.getBalance(accountNumber);
            ApiResponse<BigDecimal> response = ApiResponse.<BigDecimal>builder()
                    .status(200)
                    .message("Balance fetched successfully")
                    .data(balance)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<BigDecimal> errorResponse = ApiResponse.<BigDecimal>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch balance: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    /**
     * Retrieves a list of all accounts.
     *
     * @return a {@link ResponseEntity} containing an {@link ApiResponse} with
     *         the list of all accounts or an error message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();

            // Map each Account entity to AccountResponse DTO
            List<AccountResponse> accountResponses = accounts.stream()
                    .map(account -> AccountResponse.builder()
                            .id(account.getId())
                            .accountNumber(account.getAccountNumber())
                            .accountType(account.getAccountType())
                            .balance(account.getBalance())
                            .customerId(account.getCustomer().getId())
                            .customerName(account.getCustomer().getFullName())
                            .build())
                    .toList();

            ApiResponse<List<AccountResponse>> response = ApiResponse.<List<AccountResponse>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Accounts fetched successfully")
                    .data(accountResponses)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<List<AccountResponse>> errorResponse = ApiResponse.<List<AccountResponse>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch accounts: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/withPagination")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AccountResponse> accountPage = accountService.getAccounts(page, size);

        ApiResponse<Page<AccountResponse>> response = ApiResponse.<Page<AccountResponse>>builder()
                .status(200)
                .message("Accounts fetched successfully")
                .data(accountPage)
                .build();

        return ResponseEntity.ok(response);
    }

}
