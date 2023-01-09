package com.banquito.transaction.service;

import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findAll(){
        return transactionRepository.findAll();
    }
}
