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

    Optional<Transaction> findByCodeUniqueTransaction(String codeUniqueTransaction);
    @Query("{'codeLocalAccount': ?0,'executeDate' : { $gt: ?1, $lt: ?2 } }")
    List<Transaction> findByCodeLocalAccountAndExecuteDateBetween(String codeLocalAccount, LocalDateTime from, LocalDateTime to);

    @Query("{'codeLocalAccount': ?0, 'type': ?1 ,'executeDate' : { $gt: ?2, $lt: ?3 } }")
    List<Transaction> findByCodeLocalAccountAndTypeAndExecuteDateBetween(String codeLocalAccount, String type, LocalDateTime from, LocalDateTime to);
}
