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
        return ResponseEntity.status(200).body("Deposit created");
    }

    @RequestMapping(value = "/interest", method = RequestMethod.POST)
    public Object interest() {
        return ResponseEntity.status(200).body("Interest created");
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public Object withdraw() {
        return ResponseEntity.status(200).body("Withdraw created");
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public Object transfer() {
        return ResponseEntity.status(200).body("Transfer created");
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public Object payment() {
        return ResponseEntity.status(200).body("Payment created");
    }

}
