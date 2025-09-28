package com.example.banking.service;

import com.example.banking.entity.Customer;
import com.example.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Fetch all customers from the database.
     *
     * @return list of customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Fetch a customer by ID.
     *
     * @param id customer ID
     * @return Customer entity if found, else null
     */
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
