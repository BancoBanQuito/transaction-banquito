package com.banquito.transaction.controller;

import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Transaction>> findAll() {
        List<Transaction> transactionList = this.transactionService.findAll();

        if (transactionList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionList);
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public Object deposit() {
        return "Deposit Success";
    }

    @RequestMapping(value = "/interest ", method = RequestMethod.POST)
    public Object interest() {
        return "View Interest";
    }
}
