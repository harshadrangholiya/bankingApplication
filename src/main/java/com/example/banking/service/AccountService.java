package com.example.banking.service;

import com.example.banking.dto.AccountResponse;
import com.example.banking.entity.Account;
import com.example.banking.entity.Customer;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
        log.info("Starting account creation for customerId={}, accountType={}", customerId, accountType);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with id={}", customerId);
                    return new RuntimeException("Customer not found");
                });

        Account account = Account.builder()
                .customer(customer)
                .accountNumber(UUID.randomUUID().toString().substring(0, 12))
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: accountNumber={}, customerId={}", savedAccount.getAccountNumber(), customerId);

        return savedAccount;
    }

    /**
     * Retrieves the balance of an account by its account number.
     *
     * @param accountNumber the unique account number
     * @return the current balance as {@link BigDecimal}
     * @throws RuntimeException if the account with the given number does not exist
     */
    public BigDecimal getBalance(String accountNumber) {
        log.info("Fetching balance for accountNumber={}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.error("Account not found with accountNumber={}", accountNumber);
                    return new RuntimeException("Account not found");
                });

        log.info("Balance retrieved for accountNumber={}: {}", accountNumber, account.getBalance());
        return account.getBalance();
    }

    //get All Accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Get paginated accounts
    public Page<AccountResponse> getAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Account> accountPage = accountRepository.findAll(pageable);

        // Map Account → AccountResponse
        List<AccountResponse> accountResponses = accountPage.stream().map(account ->
                AccountResponse.builder()
                        .id(account.getId())
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .balance(account.getBalance())
                        .customerId(account.getCustomer().getId())
                        .customerName(account.getCustomer().getFullName())
                        .build()
        ).toList();

        // Return new PageImpl to keep pagination info
        return new PageImpl<>(accountResponses, pageable, accountPage.getTotalElements());
    }

}
