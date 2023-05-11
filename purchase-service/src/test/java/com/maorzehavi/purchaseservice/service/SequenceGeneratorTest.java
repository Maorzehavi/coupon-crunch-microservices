package com.maorzehavi.purchaseservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SequenceGeneratorTest {

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Test
    void generateNextId() {
        Long purchases = sequenceGenerator.generateNextId("purchases");
        System.out.println(purchases);
    }
}