package com.banquito.transaction.controller;

import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.Utils.RSFormat;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.controller.dto.RQStatus;
import com.banquito.transaction.controller.dto.RQTransaction;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.controller.mapper.TransactionMapper;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<RSFormat<RSTransaction>> createTransaction(@RequestBody RQTransaction transaction) {
        try{

            if (!Utils.validRqTransaction(transaction)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            RSTransaction response = transactionService.createTransaction(TransactionMapper.map(transaction));
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<RSTransaction>builder().message("Success").data(response).build());

        } catch (RSRuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{codeUniqueTransaction}")
    public ResponseEntity<RSFormat<String>> updateTransactionStatus(
        @PathVariable("codeUniqueTransaction") String codeUniqueTransaction, 
        @RequestBody RQStatus transactionStatus) {
        try{

            if(!Utils.hasAllAttributes(transactionStatus)||Utils.isNullEmpty(codeUniqueTransaction)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            transactionService.updateTransactionStatus(codeUniqueTransaction, transactionStatus.getStatus());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<String>builder().message("Success").data("Transaccion actualizada correctamente").build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{codeLocalAccount}/{from}/{to}")
    public ResponseEntity<RSFormat<List<RSTransaction>>> getTransactionBetween(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("from") LocalDateTime from,
            @PathVariable("to") LocalDateTime to) {
        try{

            if(Utils.isNullEmpty(codeLocalAccount)||Utils.isNullEmpty(from)||Utils.isNullEmpty(to)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            List<RSTransaction> transactions = transactionService.getTransactionsBetweenDate(codeLocalAccount, from, to);

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<List<RSTransaction>>builder().message("Success").data(transactions).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{codeLocalAccount}/type/{type}/{from}/{to}")
    public ResponseEntity<RSFormat<List<RSTransaction>>> getTransactionByTypeBetween(
            @PathVariable("type") String type,
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("from") LocalDateTime from,
            @PathVariable("to") LocalDateTime to) {
        try{

            if(Utils.isNullEmpty(type) || Utils.isNullEmpty(codeLocalAccount)||Utils.isNullEmpty(from)||Utils.isNullEmpty(to)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            if(!Utils.validTransactionType(type)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            List<RSTransaction> transactions = transactionService.getTransactionsByTypeBetweenDate
                    (codeLocalAccount, type, from, to);

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<List<RSTransaction>>builder().message("Success").data(transactions).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
