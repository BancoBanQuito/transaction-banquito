package com.banquito.transaction.service;

import com.banquito.transaction.Utils.BankUtils;
import com.banquito.transaction.config.RSCode;
import com.banquito.transaction.config.TransactionStatusCode;
import com.banquito.transaction.controller.dto.RSCreateTransaction;
import com.banquito.transaction.errors.RSRuntimeException;
import com.banquito.transaction.model.Transaction;
import com.banquito.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final String FAILURE_TRANSACTION = "No existe la cuenta del destinatario";
    private final String NOT_FOUND_ACCOUNT = "No existe la cuenta ingresada";
    private final String INSUFFICIENT_BALANCE = "Saldo insuficiente";
    private final String NOT_ENOUGH_PARAM = "No existen los parámetros suficientes";
    private final String INTERNAL_ERROR = "Ha ocurrido un error";

    private final BigDecimal MINIMUM_VALUE = new BigDecimal(1);

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {

        if (transaction.equals(null)) {
            throw new RSRuntimeException(this.NOT_ENOUGH_PARAM, RSCode.NOT_FOUND);
        }

        String codeUniqueTransaction = BankUtils.RandomNumber.generateCode(64);
        Long numVersion = 0000000000L;
        transaction.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        switch (retriveStatus(codeUniqueTransaction)) {
            case "ACTIVO": {
                if (retriveBalance(codeUniqueTransaction).compareTo(MINIMUM_VALUE) == -1) {
                    throw new RSRuntimeException(this.INSUFFICIENT_BALANCE, RSCode.INSUFFICIENT_BALANCE);
                } else if (retriveBalance(codeUniqueTransaction).compareTo(MINIMUM_VALUE) == 1) {
                    transaction.setStatus(TransactionStatusCode.SUCCESFUL.code);
                    transaction.setExecuteDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                }
                break;
            }
            case "BLOQUEADA":
                transaction.setStatus(TransactionStatusCode.PENDING.code);
                break;
            case "SUSPENDIDO":
                transaction.setStatus(TransactionStatusCode.DECLINED.code);
                break;
            default:
                throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
        }

        transaction.setCodeUniqueTransaction(codeUniqueTransaction);
        transaction.setVersion(numVersion++);

        Transaction.builder()
                .status(transaction.getStatus())
                .codeUniqueTransaction(codeUniqueTransaction)
                .createDate(transaction.getCreateDate())
                .executeDate(transaction.getExecuteDate())
                .version(transaction.getVersion())
                .build();
        
        try{
            this.transactionRepository.save(transaction);
        } catch (Exception e){
            throw new RSRuntimeException(this.FAILURE_TRANSACTION, RSCode.INTERNAL_ERROR_SERVER);
        }
        return transaction;
    }

    @Transactional
    public void updateTransaction(Transaction transaction){
        switch (retriveStatus(transaction.getCodeUniqueTransaction())) {
            case "ACTIVO": {
                if (retriveBalance(transaction.getCodeUniqueTransaction()).compareTo(MINIMUM_VALUE) == -1) {
                    throw new RSRuntimeException(this.INSUFFICIENT_BALANCE, RSCode.INSUFFICIENT_BALANCE);
                } else if (retriveBalance(transaction.getCodeUniqueTransaction()).compareTo(MINIMUM_VALUE) == 1) {
                    transaction.setStatus(TransactionStatusCode.SUCCESFUL.code);
                    transaction.setExecuteDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                }
                break;
            }
            case "BLOQUEADA":
                transaction.setStatus(TransactionStatusCode.PENDING.code);
                break;
            case "SUSPENDIDO":
                transaction.setStatus(TransactionStatusCode.DECLINED.code);
                break;
            default:
                throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
        }
    }

    /**********************************/
    private String retriveStatus(String codeAccount) {
        String status = "ACTIVO";
        return status;
    }

    private BigDecimal retriveBalance(String codeAccount) {
        BigDecimal present_balance = new BigDecimal(50);
        return present_balance;
    }

    /**********************************/

    public List<RSCreateTransaction> findAllTransactionByClient(String codeAccount){
        List<RSCreateTransaction> rsTransactions = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();

        transactions = this.transactionRepository.findByCodeAccount(codeAccount);
        if(transactions.size() <= 0){
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
        }

        try{
            transactions.forEach(transaction -> {
                Optional<Transaction> optionalTransaction = this.transactionRepository.findById(codeAccount);
                if(optionalTransaction.isPresent()){
                    RSCreateTransaction rsTransaction = RSCreateTransaction.builder()
                        .movement(transaction.getMovement())
                        .type(transaction.getType())
                        .concept(transaction.getConcept())
                        .value(transaction.getValue())
                        .codeLocalAccount(transaction.getCodeLocalAccount())                    
                        .codeUniqueTransaction(codeAccount)
                        .executeDate(transaction.getExecuteDate())
                        .recipientAccountNumber(transaction.getRecipientAccountNumber())
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
