package com.banquito.transaction.controller.mapper;

import com.banquito.transaction.controller.dto.RQSavingsAccountInterest;
import com.banquito.transaction.controller.dto.RSSavingsAccountInterest;
import com.banquito.transaction.model.Interest;

public class InterestMapper {

    public static Interest map(RQSavingsAccountInterest interest){

        return Interest.builder()
                .codeLocalAccount(interest.getCodeLocalAccount())
                .codeInternationalAccount(interest.getCodeInternationalAccount())
                .ear(interest.getEar())
                .availableBalance(interest.getAvailableBalance())
                .build();
    }

    public static RSSavingsAccountInterest map(Interest interest){
        return RSSavingsAccountInterest.builder()
                .codeLocalAccount(interest.getCodeLocalAccount())
                .codeInternationalAccount(interest.getCodeInternationalAccount())
                .codeUniqueInterest(interest.getCodeUniqueInterest())
                .ear(interest.getEar())
                .availableBalance(interest.getAvailableBalance())
                .value(interest.getValue())
                .executeDate(interest.getExecuteDate())
                .build();
    }
}
