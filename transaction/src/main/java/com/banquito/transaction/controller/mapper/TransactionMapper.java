package com.banquito.transaction.controller.mapper;

import com.banquito.transaction.controller.dto.RQTransaction;
import com.banquito.transaction.controller.dto.RSTransaction;
import com.banquito.transaction.model.Transaction;

public class TransactionMapper {
    public static Transaction map(RQTransaction transaction){
        return Transaction.builder()
            .movement(transaction.getMovement())
            .type(transaction.getType())
            .concept(transaction.getConcept())
            .description(transaction.getDescription())
            .value(transaction.getValue())
            .codeInternationalAccount(transaction.getCodeInternationalAccount())
            .codeLocalAccount(transaction.getCodeLocalAccount())
            .recipientAccountNumber(transaction.getRecipientAccountNumber())
            .recipientBank(transaction.getRecipientBank())
            .recipientType(transaction.getRecipientType())
            .build();
    }

    public static RSTransaction map(Transaction transaction){
        return RSTransaction.builder()
                .codeUniqueTransaction(transaction.getCodeUniqueTransaction())
                .movement(transaction.getMovement())
                .codeLocalAccount(transaction.getCodeLocalAccount())
                .type(transaction.getType())
                .concept(transaction.getConcept())
                .executeDate(transaction.getExecuteDate())
                .value(transaction.getValue())
                .presentBalance(transaction.getPresentBalance())
                .availableBalance(transaction.getAvailableBalance())
                .build();
    }

}
