package com.example.banking.service;

import com.example.banking.dto.TransactionDTO;
import com.example.banking.dto.TransactionRequest;
import com.example.banking.dto.TransactionResponse;
import com.example.banking.entity.Account;
import com.example.banking.entity.Transaction;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponse deposit(TransactionRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .account(account)
                .type("DEPOSIT")
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return new TransactionResponse(
                account.getAccountNumber(),
                "DEPOSIT",
                request.getAmount(),
                account.getBalance()
        );
    }

    @Transactional
    public TransactionResponse withdraw(TransactionRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .account(account)
                .type("WITHDRAWAL")
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return new TransactionResponse(
                account.getAccountNumber(),
                "WITHDRAWAL",
                request.getAmount(),
                account.getBalance()
        );
    }

    public List<TransactionDTO> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return transactionRepository.findByAccountId(account.getId())
                .stream()
                .map(tx -> TransactionDTO.builder()
                        .id(tx.getId())
                        .amount(tx.getAmount())
                        .description(tx.getDescription())
                        .transactionTime(tx.getTransactionTime())
                        .type(tx.getType())
                        .build())
                .toList();
    }

}
