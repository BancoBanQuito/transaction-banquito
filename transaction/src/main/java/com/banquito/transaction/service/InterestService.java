package com.banquito.transaction.service;


import com.banquito.transaction.Utils.InvesmentInterest;
import com.banquito.transaction.Utils.Messages;
import com.banquito.transaction.Utils.RSCode;
import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.controller.dto.RSInvestmentInterest;
import com.banquito.transaction.controller.dto.RSSavingsAccountInterest;
import com.banquito.transaction.controller.mapper.InterestMapper;
import com.banquito.transaction.exception.RSRuntimeException;
import com.banquito.transaction.model.Interest;
import com.banquito.transaction.repository.InterestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InterestService {

    private final InterestRepository interestRepository;

    public InterestService(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    public RSSavingsAccountInterest createSavingsAccountInterest(Interest interest) {

        //Validate account is not needed since this operation is automatic in the account module, hence it will
        //get the data directly from the DB
        BigDecimal value = Utils.computeSavingsAccountInterest(interest.getAvailableBalance(), interest.getEar());

        //Generated interest must be at least 1 cent
        if (value.compareTo(BigDecimal.valueOf(0.01)) == -1) {
            throw new RSRuntimeException(Messages.INTEREST_IS_TOO_LOW, RSCode.BAD_REQUEST);
        }

        interest.setCodeUniqueInterest(Utils.generateAlphanumericCode(64));
        interest.setValue(value);
        interest.setExecuteDate(Utils.currentDate());

        try {
            this.interestRepository.save(interest);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.INTEREST_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }

        return InterestMapper.map(interest);
    }

    public List<RSSavingsAccountInterest> getInterestBetweenDates(String codeLocalAccount, LocalDateTime from, LocalDateTime to) {
        List<Interest> dbInterests = interestRepository.findByCodeLocalAccountAndExecuteDateBetween(codeLocalAccount, from, to);
        List<RSSavingsAccountInterest> interests = new ArrayList<>();
        RSSavingsAccountInterest interest;

        for (Interest dbInterest : dbInterests) {
            interest = InterestMapper.map(dbInterest);
            interests.add(interest);
        }

        return interests;
    }

    public RSInvestmentInterest getInvestmentInterest(String codeLocalAccount, Integer days, BigDecimal capital, BigDecimal ear) {

        InvesmentInterest invesmentInterest = Utils.computeInvestmentInterest(days, capital, ear);

        return RSInvestmentInterest.builder()
                .codeLocalAccount(codeLocalAccount)
                .rawInterest(invesmentInterest.getRawInterest())
                .retention(invesmentInterest.getRetention())
                .netInterest(invesmentInterest.getNetInterest())
                .build();
    }

}
