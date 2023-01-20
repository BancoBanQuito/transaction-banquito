package com.banquito.transaction.repository;
import com.banquito.transaction.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByCodeLocalAccount(String codeLocalAccount);
    Optional<Transaction> findByCodeUniqueTransaction(String codeUniqueTransaction);
    @Query("{'codeLocalAccount': ?0,'executeDate' : { $gt: ?1, $lt: ?2 } }")
    List<Transaction> findByCodeLocalAccountAndExecuteDateBetween(String name, LocalDateTime from, LocalDateTime to);
}
