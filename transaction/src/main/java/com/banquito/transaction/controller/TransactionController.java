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
import com.banquito.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    //This method is only used for development purposes
    @GetMapping(value = "/test")
    public Object test(){
        /*AccountRequest accountRequest = new AccountRequest();
        return accountRequest.getAccountData("dfd4f80f8f90f1136512",
                "0b6edacd6a13797a079335ca502335a3ad");*/

        /*return AccountRequest.updateAccountBalance(
                "dfd4f80f8f90f1136512",
                "0b6edacd6a13797a079335ca502335a3ad",
                RQAccountBalance.builder()
                        .presentBalance(new BigDecimal(20))
                        .availableBalance(new BigDecimal(20))
                        .build()
        );*/

        //return Utils.computeInterestSavingsAccount(BigDecimal.valueOf(40.00), BigDecimal.valueOf(5.75));

        return Utils.computeInvestmentInterest(91, BigDecimal.valueOf(20000), BigDecimal.valueOf(2.52));
    }

    @PostMapping
    public ResponseEntity<RSFormat> createTransaction(@RequestBody RQTransaction transaction) {
        try{

            if (!Utils.validRsTransaction(transaction)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            RSTransaction response = transactionService.createTransaction(TransactionMapper.map(transaction));
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(response).build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping("/{codeUniqueTransaction}")
    public ResponseEntity<RSFormat> updateTransactionStatus(
        @PathVariable("codeUniqueTransaction") String codeUniqueTransaction, 
        @RequestBody RQStatus transactionStatus) {
        try{

            if(!Utils.hasAllAttributes(transactionStatus)||Utils.isNullEmpty(codeUniqueTransaction)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            transactionService.updateTransactionStatus(codeUniqueTransaction, transactionStatus.getStatus());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data("Transaccion actualizada correctamente").build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @GetMapping("/{codeLocalAccount}/{from}/{to}")
    public ResponseEntity<RSFormat> getTransactionBetween(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("from") LocalDateTime from,
            @PathVariable("to") LocalDateTime to) {
        try{

            if(Utils.isNullEmpty(codeLocalAccount)||Utils.isNullEmpty(from)||Utils.isNullEmpty(to)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            List<RSTransaction> transactions = transactionService.getTransactionsBetweenDate(codeLocalAccount, from, to);

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(transactions).build());

        } catch(RSRuntimeException e){
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e){
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
