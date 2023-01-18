package com.banquito.transaction.controller;
import com.banquito.transaction.config.RSCode;
import com.banquito.transaction.config.ResponseFormat;
import com.banquito.transaction.controller.dto.RQStatus;
import com.banquito.transaction.controller.dto.RQTransaction;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.controller.mapper.TransactionMapper;
import com.banquito.transaction.errors.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.service.TransactionService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ResponseFormat> createTransaction(@RequestBody RQTransaction transaction) {
        try{
            Transaction savedTransaction = transactionService.createTransaction(TransactionMapper.map(transaction));
            RSTransaction responseTransaction = TransactionMapper.map(savedTransaction);
            return ResponseEntity.status(RSCode.CREATED.code).body(ResponseFormat.builder().message("Success").data(responseTransaction).build());
        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode()).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e){
            return ResponseEntity.status(500).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping("/{codeUniqueTransaction}")
    public ResponseEntity<ResponseFormat> updateTransaction(
        @PathVariable("codeUniqueTransaction") String codeUniqueTransaction, 
        @RequestBody RQStatus transactionStatus) {
        try{
            this.transactionService.updateTransaction(codeUniqueTransaction, transactionStatus.getStatus());
            return ResponseEntity.status(RSCode.CREATED.code).body(ResponseFormat.builder().message("Success").build());
        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode()).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e){
            return ResponseEntity.status(500).body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
        
    }

}
