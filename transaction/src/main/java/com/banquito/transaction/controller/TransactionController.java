package com.banquito.transaction.controller;

import com.banquito.transaction.Utils.Messages;
import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.Utils.RSFormat;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.controller.dto.RQStatus;
import com.banquito.transaction.controller.dto.RQTransaction;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.controller.mapper.TransactionMapper;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<RSFormat> createTransaction(@RequestBody RQTransaction transaction) {
        try{

            if (!Utils.hasAllAttributes(transaction)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            Transaction savedTransaction = transactionService.createTransaction(TransactionMapper.map(transaction));
            RSTransaction responseTransaction = TransactionMapper.map(savedTransaction);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(responseTransaction).build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping("/{codeUniqueTransaction}")
    public ResponseEntity<RSFormat> updateTransaction(
        @PathVariable("codeUniqueTransaction") String codeUniqueTransaction, 
        @RequestBody RQStatus transactionStatus) {
        try{

            if(!Utils.hasAllAttributes(transactionStatus)||Utils.isNullEmpty(codeUniqueTransaction)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            transactionService.updateTransaction(codeUniqueTransaction, transactionStatus.getStatus());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
