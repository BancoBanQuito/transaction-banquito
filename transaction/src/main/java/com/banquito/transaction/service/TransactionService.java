package com.banquito.transaction.service;

import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
    public void deposit() {
    }

    @Transactional
    public void interest() {
    }

    @Transactional
    public void withdraw() {
    }

    @Transactional
    public void transfer() {
    }

    @Transactional
    public void payment() {
    }

}
