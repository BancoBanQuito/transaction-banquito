package com.banquito.transaction.controller.mapper;

import com.banquito.transaction.controller.dto.RQCreateTransaction;
import com.banquito.transaction.controller.dto.RSCreateTransaction;
import com.banquito.transaction.model.Transaction;

public class TransactionMapper {
    public static Transaction map(RQCreateTransaction transaction){
        return Transaction.builder()
        .type(transaction.getType())
        .concept(transaction.getConcept())
        .description(transaction.getDescripcion())
        .value(transaction.getValue())
        .codeInternationalAccount(transaction.getCodeInternationalAccount())
        .codeLocalAccount(transaction.getCodeLocalAccount())
        .recipientAccountNumber(transaction.getRecipientAccountNumber())
        .recipientBank(transaction.getRecipientBank())
        .recipientType(transaction.getRecipientType())
        .codeUniqueTransaction(null)
        .build();
    }

    public static RSCreateTransaction map(Transaction transaction){
        return RSCreateTransaction.builder()
        .codeUniqueTransaction(transaction.getCodeUniqueTransaction())
        .build();
    }

}
