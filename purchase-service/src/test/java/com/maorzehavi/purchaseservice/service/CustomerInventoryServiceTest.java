package com.maorzehavi.purchaseservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerInventoryServiceTest {

    @Autowired
    private CustomerInventoryService customerInventoryService;

    @Test
    void saveCustomerInventory() {
        System.out.println(customerInventoryService.saveCustomerInventory(2L, 1L, 1L,2L, 3L));
    }

    @Test
    void getAllCustomerInventories() {
    }
}