package com.example.banking.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyTransactionReportDTO {

    private Long customerId;
    private String customerName;
    private String email;

    private List<CustomerAddressDTO> addresses; // All addresses of customer
    private List<AccountDTO> accounts;           // Customer accounts
    private List<TransactionDTO> transactions;  // Transactions in the month

    private BigDecimal totalDeposit;             // Sum of deposits
    private BigDecimal totalWithdrawal;          // Sum of withdrawals
}
