package com.maorzehavi.purchaseservice.service;

import com.maorzehavi.purchaseservice.model.entity.CustomerInventory;
import com.maorzehavi.purchaseservice.repository.CustomerInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerInventoryService {

    private final CustomerInventoryRepository customerInventoryRepository;

    public CustomerInventory saveCustomerInventory(Long customerId,Long ... couponIds) {
        CustomerInventory customerInventory = CustomerInventory.builder()
                .customerId(customerId)
                .couponIds(List.of(couponIds))
                .build();
        return customerInventoryRepository.save(customerInventory);
    }

    public CustomerInventory getCustomerInventory(Long customerId) {
        return customerInventoryRepository.findById(customerId).orElse(null);
    }

    public List<CustomerInventory> getAllCustomerInventories() {
        return customerInventoryRepository.findAll();
    }
}
