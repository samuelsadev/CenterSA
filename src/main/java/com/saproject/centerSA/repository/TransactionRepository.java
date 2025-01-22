package com.saproject.centerSA.repository;

import com.saproject.centerSA.dto.TransactionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionRecord, String> {
}
