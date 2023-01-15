package com.banquito.transaction.service;

import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void createTransaction(Transaction transaction){
        /*Implementar un switch para los casos del estado:  exitoso, pendiente, rechazado*/
        transaction.setStatus("exitoso");
        transaction.setCodeUniqueTransaction(null);
        transaction.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        transaction.setExecuteDate(null);
        transaction.setVersion(null);
    }

}
