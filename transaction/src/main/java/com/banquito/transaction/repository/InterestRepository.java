package com.banquito.transaction.repository;

import com.banquito.transaction.model.Interest;
import com.banquito.transaction.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface InterestRepository extends MongoRepository<Interest, String> {

    @Query("{'codeLocalAccount': ?0,'executeDate' : { $gt: ?1, $lt: ?2 } }")
    List<Interest> findByCodeLocalAccountAndExecuteDateBetween(String name, LocalDateTime from, LocalDateTime to);
}
