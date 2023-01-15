package com.banquito.transaction.controller;

import com.banquito.config.RSCode;
import com.banquito.config.ResponseFormat;
import com.banquito.transaction.controller.dto.RQCreateTransaction;
import com.banquito.transaction.controller.dto.RSCreateTransaction;
import com.banquito.transaction.controller.mapper.TransactionMapper;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.service.TransactionService;

import jakarta.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ResponseFormat> createTransaction(
        @RequestBody RQCreateTransaction transaction){
            try{
                Transaction savedTransaction = transactionService.createTransaction(TransactionMapper.map(transaction));
                RSCreateTransaction responseTransaction = TransactionMapper.map(savedTransaction);
                return ResponseEntity.status(RSCode.CREATED.code).body(ResponseFormat.builder().message("Success").data(responseTransaction.getCodeUniqueTransaction()).build());
            } catch (RSRuntimeException e){

            }
        } 

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Transaction>> findAll() {
        List<Transaction> transactionList = this.transactionService.findAll();

        if (transactionList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionList);
    }

    @PostMapping
    public Object insert() {
        return ResponseEntity.status(201).body("Success");
    }

    @PatchMapping
    public Object deposit() {
        return ResponseEntity.status(200).body("Deposit created");
    }

}
