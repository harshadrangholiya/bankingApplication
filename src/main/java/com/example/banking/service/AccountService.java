package com.example.banking.service;

import com.example.banking.entity.Account;
import com.example.banking.entity.Customer;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Creates a new account for a given customer.
     *
     * <p>The account number is generated as a 12-character UUID substring,
     * the initial balance is set to zero, and the creation timestamp is
     * recorded.</p>
     *
     * @param customerId  the ID of the customer for whom the account is created
     * @param accountType the type of account (e.g., "SAVINGS", "CURRENT")
     * @return the created {@link Account} entity
     * @throws RuntimeException if the customer with the given ID does not exist
     */
    public Account createAccount(Long customerId, String accountType) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = Account.builder()
                .customer(customer)
                .accountNumber(UUID.randomUUID().toString().substring(0, 12))
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        return accountRepository.save(account);
    }

    /**
     * Retrieves the balance of an account by its account number.
     *
     * @param accountNumber the unique account number
     * @return the current balance as {@link BigDecimal}
     * @throws RuntimeException if the account with the given number does not exist
     */

    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance();
    }
}
