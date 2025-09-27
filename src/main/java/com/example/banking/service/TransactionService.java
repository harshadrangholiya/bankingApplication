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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Deposits a specified amount into an account.
     *
     * <p>The account balance is updated, and a corresponding transaction
     * record is created.</p>
     *
     * @param request the deposit request containing account number, amount, and description
     * @return a {@link TransactionResponse} containing updated account balance and transaction details
     * @throws RuntimeException if the account is not found
     */

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
                .transactionTime(LocalDateTime.now())
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

    /**
     * Withdraws a specified amount from an account.
     *
     * <p>The account balance is updated, and a corresponding transaction
     * record is created. Throws an exception if the balance is insufficient.</p>
     *
     * @param request the withdrawal request containing account number, amount, and description
     * @return a {@link TransactionResponse} containing updated account balance and transaction details
     * @throws RuntimeException if the account is not found or if balance is insufficient
     */

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
                .transactionTime(LocalDateTime.now())
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


    /**
     * Retrieves the transaction history for a given account.
     *
     * @param accountNumber the unique account number
     * @return a list of {@link TransactionDTO} containing transaction details
     * @throws RuntimeException if the account is not found
     */
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
