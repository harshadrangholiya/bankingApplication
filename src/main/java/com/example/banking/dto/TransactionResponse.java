package com.example.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private String accountNumber;
    private String type; // DEPOSIT / WITHDRAWAL
    private BigDecimal amount;
    private BigDecimal balanceAfter;
}
