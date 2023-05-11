package com.maorzehavi.customerservice.service;

import com.maorzehavi.customerservice.model.dto.request.CustomerRequest;
import com.maorzehavi.customerservice.model.dto.response.CustomerResponse;
import com.maorzehavi.customerservice.model.entity.Customer;
import com.maorzehavi.customerservice.repository.CustomerRepository;
import com.maorzehavi.customerservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;


    public Optional<CustomerResponse> getCustomer(Long id) {
        return customerRepository.findById(id).map(this::mapToCustomerResponse);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToCustomerResponse).collect(Collectors.toList());
    }

    public Optional<CustomerResponse> createCustomer(CustomerRequest request) {
        Customer customer = mapToCustomer(request);
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
        }
        var userId = restTemplate.postForObject("http://identity-service/api/v1/users?clientType=CUSTOMER", request.getUser(), String.class);
        if (userId == null) {
            throw new RuntimeException("Failed to create user");
        }
        customer.setUserId(Long.parseLong(userId));
        customerRepository.save(customer);
        return Optional.of(mapToCustomerResponse(customer));
    }

    public Optional<CustomerResponse> updateCustomer(Long id, CustomerRequest request, HttpServletRequest httpRequest) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer with id " + id + " not found"));
        jwtUtil.validateUser(customer.getUserId(), httpRequest);
        var isEmailTaken = restTemplate.getForEntity("http://identity-service/api/v1/users/taken?userId="+customer.getUserId()+"&email=" + request.getUser().getEmail(), Boolean.class);
        if (Boolean.TRUE.equals(isEmailTaken.getBody()) || !customer.getEmail().equals(request.getUser().getEmail()) && customerRepository.existsByEmail(request.getUser().getEmail())) {
            throw new RuntimeException("User with email " + request.getUser().getEmail() + " already exists");
        }
        restTemplate.put("http://identity-service/api/v1/users/" + customer.getUserId(), request.getUser());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getUser().getEmail());
        customerRepository.save(customer);
        return Optional.of(mapToCustomerResponse(customer));
    }

    public Optional<CustomerResponse> deleteCustomer(Long id,HttpServletRequest httpRequest) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer with id " + id + " not found"));
        jwtUtil.validateUser(customer.getUserId(), httpRequest);
        restTemplate.delete("http://identity-service/api/v1/users/" + customer.getUserId());
        customerRepository.delete(customer);
        return Optional.of(mapToCustomerResponse(customer));
    }

    public Optional<Long> getCustomerIdByUserId(Long userId) {
        return customerRepository.findIdByUserId(userId);
    }

    private Customer mapToCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getUser().getEmail())
                .build();
    }

    private CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .userId(customer.getUserId())
                .build();
    }
}
