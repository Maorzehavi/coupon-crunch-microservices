package com.maorzehavi.purchaseservice.repository;

import com.maorzehavi.purchaseservice.model.entity.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase, Long> {
}
