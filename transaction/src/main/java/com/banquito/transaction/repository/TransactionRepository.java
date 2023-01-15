package com.banquito.transaction.repository;
import com.banquito.transaction.model.Transaction;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByCodeLocalAccount(String codeLocalAccount);
    List<Transaction> findByCodeLocalAccountAndExecuteDateBetween(String codeLocalAccount, String startDate, String endDate);
}
