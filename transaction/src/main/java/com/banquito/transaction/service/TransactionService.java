package com.banquito.transaction.service;

import com.banquito.transaction.Utils.BankUtils;
import com.banquito.transaction.config.RSCode;
import com.banquito.transaction.config.TransactionStatusCode;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.errors.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final String NOT_FOUND_ACCOUNT = "No existe la cuenta ingresada";
    private final String INSUFFICIENT_BALANCE = "Saldo insuficiente";
    private final String NOT_ENOUGH_PARAM = "No existen los parámetros suficientes";
    private final String INTERNAL_ERROR = "Ha ocurrido un error";

    private final BigDecimal MINIMUM_VALUE = new BigDecimal(10);

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.equals(null)) {
            throw new RSRuntimeException(this.NOT_ENOUGH_PARAM, RSCode.NOT_FOUND);
        }

        String codeUniqueTransaction = BankUtils.RandomNumber.generateCode(64);
        transaction.setCodeUniqueTransaction(codeUniqueTransaction);
        transaction.setCreateDate(LocalDateTime.now());        
        if(retriveStatus(transaction.getCodeUniqueTransaction()).equals("ACTIVO")){
            if (retriveBalance(transaction.getCodeUniqueTransaction()).compareTo(MINIMUM_VALUE) == -1) {
                throw new RSRuntimeException(this.INSUFFICIENT_BALANCE, RSCode.INSUFFICIENT_BALANCE);
            } else if (retriveBalance(transaction.getCodeUniqueTransaction()).compareTo(MINIMUM_VALUE) == 1) {
                transaction.setStatus(TransactionStatusCode.SUCCESFUL.code);
                transaction.setExecuteDate(LocalDateTime.now());
            }
        } else if(retriveBalance(transaction.getCodeUniqueTransaction()).equals("BLOQUEADA")){
            transaction.setStatus(TransactionStatusCode.PENDING.code);
        } else if(retriveBalance(transaction.getCodeUniqueTransaction()).equals("SUSPENDIDO")){
            transaction.setStatus(TransactionStatusCode.DECLINED.code);
        } else {
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
        }
        
        try{
            this.transactionRepository.save(transaction);
        } catch (Exception e){
            throw new RSRuntimeException(e.getMessage(), RSCode.INTERNAL_ERROR_SERVER);
        }
        
        return transaction;
    }

    @Transactional
    public void updateTransaction(String codeUniqueTransaction, String newStatus){
        Optional<Transaction> optional = this.transactionRepository.findByCodeUniqueTransaction(codeUniqueTransaction);
        if(optional.isPresent()){
            Transaction transaction = optional.get();
            transaction.setStatus(newStatus);
            try{
                this.transactionRepository.save(transaction);
            } catch (Exception e){
                throw new RSRuntimeException(e.getMessage(), RSCode.INTERNAL_ERROR_SERVER);
            }
        } else {
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
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
        if(transactions.size() <= 0){
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
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
            throw new RSRuntimeException(this.INTERNAL_ERROR, RSCode.INTERNAL_ERROR_SERVER);
        }
        return rsTransactions;
    }


}
