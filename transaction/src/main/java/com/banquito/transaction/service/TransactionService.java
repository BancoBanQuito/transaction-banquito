package com.banquito.transaction.service;

import com.banquito.transaction.Utils.Messages;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import com.banquito.transaction.request.AccountRequest;
import com.banquito.transaction.request.dto.RQAccountBalance;
import com.banquito.transaction.request.dto.RSAccount;
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

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        BigDecimal newAvailableBalance;
        BigDecimal newPresentBalance;

        transaction.setCreateDate(Utils.currentDate());

        //Validate data
        RSAccount account = AccountRequest
                .getAccountData(transaction.getCodeLocalAccount(), transaction.getCodeInternationalAccount());

        //Check if account exist
        if(account == null){
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_FOUND_BY_CODE, RSCode.NOT_FOUND);
        }

        //Check if account is active
        if(!account.getStatus().equals("ACT")){
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_ACTIVE, RSCode.FORBIDEN);
        }

        //Check movement Type
        if(transaction.getMovement().equals("NOTA DEBITO")){

            //Check if account has enough balance
            if(transaction.getValue().compareTo(account.getAvailableBalance()) == 1){
                throw new RSRuntimeException(Messages.INSUFFICIENT_BALANCE , RSCode.FORBIDEN);
            }else {
                BigDecimal difference = account.getAvailableBalance().subtract(transaction.getValue());
                if(new BigDecimal(1).compareTo(difference) == 1){
                    throw new RSRuntimeException(Messages.INSUFFICIENT_BALANCE , RSCode.FORBIDEN);
                }
            }

            //Compute new balances
            newAvailableBalance = account.getAvailableBalance().subtract(transaction.getValue());
            newPresentBalance = account.getPresentBalance().subtract(transaction.getValue());

        }else if(transaction.getMovement().equals("NOTA CREDITO")){

            //Compute new balances
            newAvailableBalance = account.getAvailableBalance().add(transaction.getValue());
            newPresentBalance = account.getPresentBalance().add(transaction.getValue());

        }else {
            throw new RSRuntimeException(Messages.INVALID_MOVEMENT, RSCode.FORBIDEN);
        }

        //Send update request
        Boolean update = AccountRequest.updateAccountBalance(
                transaction.getCodeLocalAccount(),
                transaction.getCodeInternationalAccount(),
                RQAccountBalance.builder()
                        .availableBalance(newAvailableBalance)
                        .presentBalance(newPresentBalance)
                        .build()
        );

        //Check if update was successful
        if(!update){
            throw new RSRuntimeException(Messages.TRANSACTION_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }

        //Set missing attributes
        transaction.setCodeUniqueTransaction(Utils.generateAlphanumericCode(64));
        transaction.setExecuteDate(Utils.currentDate());
        transaction.setAvailableBalance(newAvailableBalance);
        transaction.setPresentBalance(newPresentBalance);

        //Save transaction
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
            throw new RSRuntimeException(Messages.TRANSACTION_NOT_FOUND_BY_CODE, RSCode.NOT_FOUND);
        }

        Transaction transaction = opTransaction.get();
        transaction.setStatus(newStatus);
        try{
            this.transactionRepository.save(transaction);
        } catch (Exception e){
            throw new RSRuntimeException(Messages.TRANSACTION_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

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
