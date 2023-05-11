package com.maorzehavi.purchaseservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "id_generator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdGenerator {
    @Id
    private String collectionName;
    private Long nextId ;

}
