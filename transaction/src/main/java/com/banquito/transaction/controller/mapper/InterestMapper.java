package com.banquito.transaction.controller.mapper;

import com.banquito.transaction.controller.dto.RQInterest;
import com.banquito.transaction.controller.dto.RSInterest;
import com.banquito.transaction.model.Interest;

public class InterestMapper {

    public static Interest map(RQInterest interest){

        return Interest.builder()
                .codeLocalAccount(interest.getCodeLocalAccount())
                .codeInternationalAccount(interest.getCodeInternationalAccount())
                .ear(interest.getEar())
                .availableBalance(interest.getAvailableBalance())
                .build();
    }

    public static RSInterest map(Interest interest){
        return RSInterest.builder()
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
