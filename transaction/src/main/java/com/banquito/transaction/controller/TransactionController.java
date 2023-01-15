package com.banquito.transaction.controller;
import com.banquito.transaction.config.RSCode;
import com.banquito.transaction.config.ResponseFormat;
import com.banquito.transaction.controller.dto.RQCreateTransaction;
import com.banquito.transaction.controller.dto.RSCreateTransaction;
import com.banquito.transaction.controller.mapper.TransactionMapper;
import com.banquito.transaction.errors.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ResponseFormat> createTransaction(@RequestBody RQCreateTransaction transaction) {
        try{
            Transaction savedTransaction = transactionService.createTransaction(TransactionMapper.map(transaction));
            RSCreateTransaction responseTransaction = TransactionMapper.map(savedTransaction);
            return ResponseEntity.status(RSCode.CREATED.code).body(ResponseFormat.builder().message("Success").data(responseTransaction).build());
        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode()).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e){
            return ResponseEntity.status(500).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping
    public ResponseEntity<ResponseFormat> updateTransaction(@RequestBody RQCreateTransaction transaction) {
        try{
            this.transactionService.updateTransaction(TransactionMapper.map(transaction));
            return ResponseEntity.status(RSCode.CREATED.code).body(ResponseFormat.builder().message("Success").data(transaction.getStatus()).build());
        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode()).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e){
            return ResponseEntity.status(500).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
