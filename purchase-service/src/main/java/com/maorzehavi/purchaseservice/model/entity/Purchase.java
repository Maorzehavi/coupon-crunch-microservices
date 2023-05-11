package com.maorzehavi.purchaseservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.List;

@Document(collection = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
    @Id
    private Long id;

    private Long customerId;

    private Timestamp timestamp;

    private Double totalPrice;

    private Integer numberOfCoupons;

    private List<Long> couponIds;

}
