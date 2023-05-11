package com.maorzehavi.customerservice.controller;

import com.maorzehavi.customerservice.model.dto.request.CustomerRequest;
import com.maorzehavi.customerservice.model.dto.response.CustomerResponse;
import com.maorzehavi.customerservice.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
        try{
            return customerService.createCustomer(request).map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create customer"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerRequest request, HttpServletRequest httpRequest) {
        try {
            return customerService.updateCustomer(id, request,httpRequest).map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update customer"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CustomerResponse> deleteCustomer(@PathVariable Long id,HttpServletRequest httpRequest) {
        return customerService.deleteCustomer(id,httpRequest).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete customer"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Long> getCustomerIdByUserId(@PathVariable Long userId) {
        return customerService.getCustomerIdByUserId(userId).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with user id " + userId + " not found"));
    }


}
