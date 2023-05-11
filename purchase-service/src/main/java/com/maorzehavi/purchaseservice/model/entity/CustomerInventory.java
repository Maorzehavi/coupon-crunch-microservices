package com.maorzehavi.purchaseservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customer_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInventory {

    @Id
    private Long customerId;

    private List<Long> couponIds;


}
