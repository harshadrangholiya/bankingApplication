package com.example.banking.service;

import com.example.banking.dto.AccountDTO;
import com.example.banking.dto.CustomerAddressDTO;
import com.example.banking.dto.MonthlyTransactionReportDTO;
import com.example.banking.dto.TransactionDTO;
import com.example.banking.entity.Customer;
import com.example.banking.repository.CustomerAddressRepository;
import com.example.banking.repository.CustomerRepository;
import com.example.banking.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ReportService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private CustomerAddressRepository addressRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    /**
     * Generates a monthly transaction report for all customers.
     *
     * <p>The report includes:</p>
     * <ul>
     *     <li>Customer details (ID, username, email)</li>
     *     <li>Addresses of the customer</li>
     *     <li>Accounts owned by the customer</li>
     *     <li>Transactions for the specified month and year</li>
     *     <li>Total deposit and withdrawal amounts</li>
     * </ul>
     *
     * @param month the month for which the report is generated (1-12)
     * @param year  the year for which the report is generated
     * @return a list of {@link MonthlyTransactionReportDTO} containing the report data for each customer
     */
    public List<MonthlyTransactionReportDTO> getMonthlyReport(int month, int year) {
        log.info("Generating monthly transaction report for month={} and year={}", month, year);

        List<Customer> customers = customerRepo.findAll();
        log.info("Found {} customers", customers.size());

        return customers.stream().map(customer -> {
            log.info("Processing report for customerId={}, username={}", customer.getId(), customer.getUsername());

            // Fetch addresses
            List<CustomerAddressDTO> addresses = addressRepo.findByCustomerId(customer.getId())
                    .stream()
                    .map(addr -> CustomerAddressDTO.builder()
                            .id(addr.getId())
                            .addressLine1(addr.getAddressLine1())
                            .addressLine2(addr.getAddressLine2())
                            .city(addr.getCity())
                            .state(addr.getState())
                            .postalCode(addr.getPostalCode())
                            .country(addr.getCountry())
                            .addressType(addr.getAddressType())
                            .build())
                    .toList();
            log.info("Found {} addresses for customerId={}", addresses.size(), customer.getId());

            List<AccountDTO> accounts = customer.getAccounts().stream()
                    .map(acc -> AccountDTO.builder()
                            .id(acc.getId())
                            .accountNumber(acc.getAccountNumber())
                            .accountType(acc.getAccountType())
                            .balance(acc.getBalance())
                            .createdAt(acc.getCreatedAt())
                            .build())
                    .toList();
            log.info("Found {} accounts for customerId={}", accounts.size(), customer.getId());

            // Fetch transactions for the month
            List<TransactionDTO> transactions = transactionRepo.findByCustomerIdAndMonthAndYear(customer.getId(), month, year)
                    .stream()
                    .map(tx -> TransactionDTO.builder()
                            .id(tx.getId())
                            .accountNumber(tx.getAccount().getAccountNumber())
                            .amount(tx.getAmount())
                            .description(tx.getDescription())
                            .transactionTime(tx.getTransactionTime())
                            .type(tx.getType())
                            .build())
                    .toList();
            log.info("Found {} transactions for customerId={} in month={}", transactions.size(), customer.getId(), month);

            // Calculate totals
            BigDecimal totalDeposit = transactions.stream()
                    .filter(tx -> tx.getType().equalsIgnoreCase("DEPOSIT"))
                    .map(TransactionDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalWithdrawal = transactions.stream()
                    .filter(tx -> tx.getType().equalsIgnoreCase("WITHDRAWAL"))
                    .map(TransactionDTO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            log.info("CustomerId={} totalDeposit={} totalWithdrawal={}", customer.getId(), totalDeposit, totalWithdrawal);

            return MonthlyTransactionReportDTO.builder()
                    .customerId(customer.getId())
                    .customerName(customer.getUsername())
                    .email(customer.getEmail())
                    .addresses(addresses)
                    .transactions(transactions)
                    .accounts(accounts)
                    .totalDeposit(totalDeposit)
                    .totalWithdrawal(totalWithdrawal)
                    .build();
        }).toList();
    }
}

