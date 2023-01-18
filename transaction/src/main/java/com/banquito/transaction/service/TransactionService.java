package com.banquito.transaction.service;

import com.banquito.transaction.Utils.Messages;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.Utils.Status;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final BigDecimal MINIMUM_VALUE = new BigDecimal(10);

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {


        String codeUniqueTransaction = Utils.generateAlphanumericCode(64);
        transaction.setCodeUniqueTransaction(codeUniqueTransaction);
        transaction.setCreateDate(Utils.currentDate());

        if (retriveStatus(transaction.getCodeUniqueTransaction()).equals("ACTIVO")) {
            if (retriveBalance(transaction.getCodeUniqueTransaction()).compareTo(MINIMUM_VALUE) == -1) {
                throw new RSRuntimeException(Messages.INSUFFICIENT_BALANCE, RSCode.BAD_REQUEST);
            } else if (retriveBalance(transaction.getCodeUniqueTransaction()).compareTo(MINIMUM_VALUE) == 1) {
                transaction.setStatus(Status.SUCCESSFUL.code);
                transaction.setExecuteDate(Utils.currentDate());
            }
        } else if (retriveBalance(transaction.getCodeUniqueTransaction()).equals("BLOQUEADA")) {
            transaction.setStatus(Status.PENDING.code);
        } else if (retriveBalance(transaction.getCodeUniqueTransaction()).equals("SUSPENDIDO")) {
            transaction.setStatus(Status.DECLINED.code);
        } else {
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_FOUND_BY_CODE, RSCode.NOT_FOUND);
        }

        try {
            this.transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.TRANSACTION_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }

        return transaction;
    }

    @Transactional
    public void updateTransaction(String codeUniqueTransaction, String newStatus){
        Optional<Transaction> opTransaction = this.transactionRepository.findByCodeUniqueTransaction(codeUniqueTransaction);

        if(!opTransaction.isPresent()){
            throw new RSRuntimeException(Messages.TRASACTION_NOT_FOUND_BY_CODE, RSCode.NOT_FOUND);
        }

        Transaction transaction = opTransaction.get();
        transaction.setStatus(newStatus);
        try{
            this.transactionRepository.save(transaction);
        } catch (Exception e){
            throw new RSRuntimeException(Messages.TRANSACTION_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**********************************/
    private String retriveStatus(String codeLocalAccount) {
        String status = "ACTIVO";
        return status;
    }

    private BigDecimal retriveBalance(String codeLocalAccount) {
        BigDecimal present_balance = new BigDecimal(50);
        return present_balance;
    }

    /**********************************/

    public List<RSTransaction> findAllTransactionByClient(String codeLocalAccount){
        List<RSTransaction> rsTransactions = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        transactions = this.transactionRepository.findByCodeLocalAccount(codeLocalAccount);
        if(transactions.size() < 1){
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_FOUND_BY_CODE, RSCode.NOT_FOUND);
        }

        try{
            transactions.forEach(transaction -> {
                Optional<Transaction> optionalTransaction = this.transactionRepository.findById(codeLocalAccount);
                if(optionalTransaction.isPresent()){
                    RSTransaction rsTransaction = RSTransaction.builder()                    
                        .codeUniqueTransaction(codeLocalAccount)
                        .build(); 
                    rsTransactions.add(rsTransaction);
                }
            });
        } catch (Exception e){
            throw new RSRuntimeException(Messages.INTERNAL_ERROR, RSCode.INTERNAL_SERVER_ERROR);
        }
        return rsTransactions;
    }


}
