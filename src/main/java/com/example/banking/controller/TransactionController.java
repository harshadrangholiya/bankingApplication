package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.TransactionDTO;
import com.example.banking.dto.TransactionRequest;
import com.example.banking.dto.TransactionResponse;
import com.example.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Performs a deposit into a specified account.
     *
     * @param request the transaction request containing account number and amount
     * @return a {@link ResponseEntity} with an {@link ApiResponse} containing
     * the deposit transaction details or an error message
     */
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(@RequestBody TransactionRequest request) {
        try {
            TransactionResponse transaction = transactionService.deposit(request);

            ApiResponse<TransactionResponse> response = ApiResponse.<TransactionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Deposit successful")
                    .data(transaction)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<TransactionResponse> errorResponse = ApiResponse.<TransactionResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Deposit failed: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Performs a withdrawal from a specified account.
     *
     * @param request the transaction request containing account number and amount
     * @return a {@link ResponseEntity} with an {@link ApiResponse} containing
     * the withdrawal transaction details or an error message
     */
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(@RequestBody TransactionRequest request) {
        try {
            TransactionResponse transaction = transactionService.withdraw(request);

            ApiResponse<TransactionResponse> response = ApiResponse.<TransactionResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Withdrawal successful")
                    .data(transaction)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<TransactionResponse> errorResponse = ApiResponse.<TransactionResponse>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Withdrawal failed: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Retrieves the transaction history for a specified account.
     *
     * @param accountNumber the account number whose transaction history is requested
     * @return a {@link ResponseEntity} with an {@link ApiResponse} containing
     * a list of {@link TransactionDTO} objects or an error message
     */
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionHistoryResponse(@PathVariable String accountNumber) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionHistory(accountNumber);

            ApiResponse<List<TransactionDTO>> response = ApiResponse.<List<TransactionDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Transaction history fetched successfully")
                    .data(transactions)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<List<TransactionDTO>> errorResponse = ApiResponse.<List<TransactionDTO>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch transaction history: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
