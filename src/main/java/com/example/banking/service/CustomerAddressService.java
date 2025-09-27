package com.example.banking.service;

import com.example.banking.dto.CustomerAddressDTO;
import com.example.banking.entity.Customer;
import com.example.banking.entity.CustomerAddress;
import com.example.banking.repository.CustomerAddressRepository;
import com.example.banking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAddressService {

    @Autowired
    private CustomerAddressRepository addressRepo;

    @Autowired
    private CustomerRepository customerRepo;

    // Add address
    public CustomerAddressDTO addAddress(Long customerId, CustomerAddressDTO dto) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerAddress address = CustomerAddress.builder()
                .customer(customer)
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .addressType(dto.getAddressType())
                .build();

        CustomerAddress saved = addressRepo.save(address);
        return mapToDTO(saved);
    }

    // Get all addresses
    public List<CustomerAddressDTO> getAddresses(Long customerId) {
        List<CustomerAddress> addresses = addressRepo.findByCustomerId(customerId);
        return addresses.stream().map(this::mapToDTO).toList();
    }

    // Mapper
    private CustomerAddressDTO mapToDTO(CustomerAddress address) {
        return CustomerAddressDTO.builder()
                .id(address.getId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .addressType(address.getAddressType())
                .build();
    }
}
