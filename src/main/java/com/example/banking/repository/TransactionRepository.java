package com.example.banking.repository;

import com.example.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId " +
            "AND MONTH(t.transactionTime) = :month AND YEAR(t.transactionTime) = :year")
    List<Transaction> findByCustomerIdAndMonthAndYear(@Param("customerId") Long customerId,
                                                      @Param("month") int month,
                                                      @Param("year") int year);

}
