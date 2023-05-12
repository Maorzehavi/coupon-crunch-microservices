package com.maorzehavi.purchaseservice.repository;

import com.maorzehavi.purchaseservice.model.entity.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends MongoRepository<Purchase, Long> {
    Optional<List<Purchase>> findAllByCustomerIdOrderByTimestamp(Long customerId);
}
