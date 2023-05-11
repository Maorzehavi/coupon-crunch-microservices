package com.maorzehavi.purchaseservice.service;

import com.maorzehavi.purchaseservice.model.entity.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class SequenceGenerator {

    private final MongoOperations mongoOperations;

    @Autowired
    public SequenceGenerator(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public Long generateNextId(String collectionName) {
        long nextId;
        Query query = new Query(Criteria.where("collectionName").is(collectionName));
        Update update = new Update().inc("nextId", 1);
        IdGenerator idGenerator = mongoOperations.findAndModify(query, update,
                FindAndModifyOptions.options().returnNew(true).upsert(true), IdGenerator.class);
        nextId = idGenerator.getNextId();
        return nextId;
    }
}
