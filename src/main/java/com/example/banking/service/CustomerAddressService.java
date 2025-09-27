package com.example.banking.service;

import com.example.banking.dto.CustomerAddressDTO;
import com.example.banking.entity.Customer;
import com.example.banking.entity.CustomerAddress;
import com.example.banking.repository.CustomerAddressRepository;
import com.example.banking.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerAddressService {

    @Autowired
    private CustomerAddressRepository addressRepo;

    @Autowired
    private CustomerRepository customerRepo;

    /**
     * Adds a new address for a specific customer.
     *
     * <p>Throws {@link RuntimeException} if the customer with the given ID
     * does not exist.</p>
     *
     * @param customerId the ID of the customer
     * @param dto        the address details to add
     * @return the saved {@link CustomerAddressDTO} object
     * @throws RuntimeException if the customer is not found
     */
    public CustomerAddressDTO addAddress(Long customerId, CustomerAddressDTO dto) {
        log.info("Adding address for customerId={}", customerId);

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with id={}", customerId);
                    return new RuntimeException("Customer not found");
                });

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
        log.info("Address added successfully: addressId={} for customerId={}", saved.getId(), customerId);
        return mapToDTO(saved);
    }

    /**
     * Retrieves all addresses for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of {@link CustomerAddressDTO} objects
     */
    public List<CustomerAddressDTO> getAddresses(Long customerId) {
        log.info("Fetching addresses for customerId={}", customerId);
        List<CustomerAddress> addresses = addressRepo.findByCustomerId(customerId);
        log.info("Found {} addresses for customerId={}", addresses.size(), customerId);
        return addresses.stream().map(this::mapToDTO).toList();
    }

    /**
     * Maps a {@link CustomerAddress} entity to a {@link CustomerAddressDTO}.
     *
     * @param address the entity to map
     * @return the corresponding DTO
     */
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
