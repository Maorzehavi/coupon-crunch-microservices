package com.maorzehavi.purchaseservice.repository;

import com.maorzehavi.purchaseservice.model.entity.CustomerInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerInventoryRepository extends MongoRepository<CustomerInventory, Long> {
}
