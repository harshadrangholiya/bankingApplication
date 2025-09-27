package com.example.banking.controller;

import com.example.banking.dto.ApiResponse;
import com.example.banking.dto.CustomerAddressDTO;
import com.example.banking.service.CustomerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}")
public class CustomerAddressController {

    @Autowired
    private CustomerAddressService addressService;

    /**
     * Adds a new address for the specified customer.
     *
     * <p>The request body should contain the details of the address
     * to be added. If successful, the newly added address is returned
     * in the response.</p>
     *
     * @param customerId the ID of the customer
     * @param dto        the address details to add
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     * with the added address or an error message
     */
    @PostMapping("/addAddress")
    public ResponseEntity<ApiResponse<CustomerAddressDTO>> addAddress(
            @PathVariable Long customerId,
            @RequestBody CustomerAddressDTO dto) {
        try {
            CustomerAddressDTO saved = addressService.addAddress(customerId, dto);

            ApiResponse<CustomerAddressDTO> response = ApiResponse.<CustomerAddressDTO>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Address added successfully")
                    .data(saved)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception ex) {
            ApiResponse<CustomerAddressDTO> errorResponse = ApiResponse.<CustomerAddressDTO>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to add address: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Retrieves all addresses for the specified customer.
     *
     * <p>Returns a list of {@link CustomerAddressDTO} associated with
     * the customer. If an error occurs, an appropriate error response
     * is returned.</p>
     *
     * @param customerId the ID of the customer
     * @return a {@link ResponseEntity} containing an {@link ApiResponse}
     * with the list of addresses or an error message
     */
    @GetMapping("/getAddresses")
    public ResponseEntity<ApiResponse<List<CustomerAddressDTO>>> getAddresses(@PathVariable Long customerId) {
        try {
            List<CustomerAddressDTO> addresses = addressService.getAddresses(customerId);

            ApiResponse<List<CustomerAddressDTO>> response = ApiResponse.<List<CustomerAddressDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Addresses fetched successfully")
                    .data(addresses)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<List<CustomerAddressDTO>> errorResponse = ApiResponse.<List<CustomerAddressDTO>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch addresses: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
